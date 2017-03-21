package com.opopov.cloud.image.service;

import com.opopov.cloud.image.utils.Utils;
import com.opopov.cloud.image.api.ImageStitchingConfiguration;
import com.opopov.cloud.image.exception.ImageLoadTimeoutException;
import com.opopov.cloud.image.exception.ImageWriteException;
import com.opopov.cloud.image.exception.SourceImageLoadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * Created by elx01 on 3/20/17.
 */
@org.springframework.stereotype.Component
public class ImageStitchingServiceImpl implements ImageStitchingService {

    private static final String content = "Test";
    @Autowired
    Utils utils;

    @Autowired
    AsyncRestTemplate remoteResource;


    @Autowired
    Validator validator;


    public ImageStitchingServiceImpl() {
        super();


    }

    private Optional<DecodedImage> decompressImage(int imageIndex, IndexMap indexMap, ResponseEntity<byte[]> resp) {
        Optional<BufferedImage> image = decompressImage(resp);
        Optional<DecodedImage> result =
                image.isPresent() ? Optional.of(new DecodedImage(image.get(), imageIndex)) : Optional.empty();
        indexMap.put(imageIndex, result);
        return result;
    }

    private Optional<BufferedImage> decompressImage(ResponseEntity<byte[]> resp) {
        if (resp.getStatusCode() != HttpStatus.OK) {
            return Optional.empty();
        }

        byte[] compressedBytes = resp.getBody();
        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(compressedBytes));
            return Optional.of(image);
        } catch (IOException e) {
            return Optional.empty();
        }
    }


    @Override
    public DeferredResult<ResponseEntity<?>> getStitchedImage(@RequestBody ImageStitchingConfiguration config) {

        validator.validateConfig(config);

        List<ListenableFuture<ResponseEntity<byte[]>>> futures =
                config.getUrlList().stream().map(
                        url -> remoteResource
                                .getForEntity(url, byte[].class)
                ).collect(Collectors.toList());


        //wrap the listenable futures into the completable futures
        //writing loop in pre-8 style, since it would be more concise compared to stream api in this case
        CompletableFuture[] imageFutures = new CompletableFuture[futures.size()];
        int taskIndex = 0;
        IndexMap indexMap = new IndexMap(config.getRowCount() * config.getColumnCount());
        for (ListenableFuture<ResponseEntity<byte[]>> f : futures) {
            imageFutures[taskIndex] = imageDataFromResponse(taskIndex, indexMap, utils.fromListenableFuture(f));
            taskIndex++;
        }

        CompletableFuture<Void> allDownloadedAndDecompressed = CompletableFuture.allOf(imageFutures);

        //Synchronous part - start - writing decompressed bytes to the large image
        final int DOWNLOAD_AND_DECOMPRESS_TIMEOUT = 30; //30 seconds for each of the individual tasks
        DeferredResult<ResponseEntity<?>> response = new DeferredResult<>();
        boolean allSuccessful = false;
        byte[] imageBytes = null;
        try {
            Void finishResult = allDownloadedAndDecompressed.get(
                    DOWNLOAD_AND_DECOMPRESS_TIMEOUT,
                    TimeUnit.SECONDS
            );

            imageBytes = combineImagesIntoStitchedImage(config, indexMap);

            HttpHeaders headers = new HttpHeaders();
            headers.setCacheControl(CacheControl.noCache().getHeaderValue());
            headers.setContentType(MediaType.IMAGE_JPEG);
            allSuccessful = true;
        } catch (InterruptedException | ExecutionException e) {
            // basically either download or decompression of the source image failed
            // just skip it then, we have no image to show
            response.setErrorResult(
                    new SourceImageLoadException("Unable to load and decode one or more source images", e)
            );
        } catch (TimeoutException e) {
            //send timeout response, via ImageLoadTimeoutException
            response.setErrorResult(
                    new ImageLoadTimeoutException(
                            String.format("Some of the images were not loaded and decoded before timeout of %d seconds",
                                    DOWNLOAD_AND_DECOMPRESS_TIMEOUT), e

                    )
            );
        } catch (IOException e) {
            response.setErrorResult(new ImageWriteException("Error writing image into output buffer", e));
        }

        //Synchronous part - end

        if (!allSuccessful) {
            //shoud not get here, some unknown error
            response.setErrorResult(
                    new ImageLoadTimeoutException(
                            "Unknown error", new RuntimeException("Something went wrong")

                    )
            );

            return response;
        }


        ResponseEntity<?> successResult = ResponseEntity.ok(imageBytes);
        response.setResult(successResult);

        return response;

    }

    private byte[] combineImagesIntoStitchedImage(@RequestBody ImageStitchingConfiguration config, IndexMap indexMap) throws IOException {
        int tileWidth = config.getSourceWidth();
        int tileHeight = config.getSourceHeight();

        //we are creating this big image in memory for the very short time frame, just when
        //all source data has been downloaded and decoded
        BufferedImage image = new BufferedImage(
                config.getRowCount() * tileWidth, config.getColumnCount() * tileHeight,
                BufferedImage.TYPE_4BYTE_ABGR);


        Graphics g = image.getGraphics();
        int indexOfTileInList = 0;
        for (int i = 0; i < config.getRowCount(); i++) {
            for (int j = 0; j < config.getColumnCount(); j++) {
                Optional<DecodedImage> decoded = indexMap.get(indexOfTileInList++);
                if (decoded != null) {
                    if (decoded.isPresent()) {
                        g.drawImage(decoded.get().getImage(), i * config.getSourceWidth(), j * config.getSourceHeight(), null);
                    }
                }
            }
        }

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        image.flush();
        ImageIO.write(image, config.getOutputFormat(), buffer);
        g.dispose();

        return buffer.toByteArray();
    }

    private CompletableFuture<Optional<DecodedImage>> imageDataFromResponse(
            int imageIndex,
            IndexMap indexMap,
            CompletableFuture<ResponseEntity<byte[]>> response
    ) {
        return response.thenApply(bytes -> decompressImage(imageIndex, indexMap, bytes));
    }


}

