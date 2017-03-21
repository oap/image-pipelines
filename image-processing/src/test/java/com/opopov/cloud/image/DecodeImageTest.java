package com.opopov.cloud.image;

/**
 * Created by elx01 on 3/20/17.
 */

import com.opopov.cloud.image.api.ImageStitchingConfiguration;
import com.opopov.cloud.image.service.ImageStitchingService;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.request.async.DeferredResult;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DecodeImageTest {

    public static final int TIMEOUT_MILLIS = 5000;
    @Autowired
    private ImageStitchingService service;

    @Test
    public void testLoadImage() throws Exception {
        ImageStitchingConfiguration config = new ImageStitchingConfiguration();
        config.setRowCount(3);
        config.setColumnCount(3);
        config.setSourceHeight(256);
        config.setSourceWidth(256);

        String[][] imageUrls = new String[][]{
                //rows left to right, top to bottom
                {"z15/9/x9650/5/y5596", "z15/9/x9650/5/y5597", "z15/9/x9650/5/y5598"},
                {"z15/9/x9651/5/y5596", "z15/9/x9651/5/y5597", "z15/9/x9651/5/y5598"},
                {"z15/9/x9652/5/y5596", "z15/9/x9652/5/y5597", "z15/9/x9652/5/y5598"}
        };


        String pattern = "http://www.opopov.com/osmtopo1/%s.png";
        for (String[] row : imageUrls) {
            for (String cell : row) {
                config.getUrlList().add(String.format(pattern, cell));
            }
        }

        final AtomicReference<byte[]> buffer = new AtomicReference<>();
        DeferredResult<ResponseEntity<?>> result = service.getStitchedImage(config);
        result.setResultHandler(
                new DeferredResult.DeferredResultHandler() {
                    @Override
                    public void handleResult(Object result) {
                        ResponseEntity<byte[]> responseEntity = (ResponseEntity<byte[]>) result;
                        buffer.set(responseEntity.getBody());

                    }
                }
        );

        Thread.sleep(TIMEOUT_MILLIS);


        InputStream is = getClass().getResourceAsStream("/horizontal-stitched-test-1-frame.png");
        byte[] expectedBytes = IOUtils.toByteArray(is);
//        Uncomment the lines below to see the generated stitched image
//        FileOutputStream fos = new FileOutputStream("/tmp/horizontal-stitched-test-1-frame.png");
//        IOUtils.write(buffer.get(), fos);
//        fos.close();

        Assert.assertTrue("Image data of stitched PNG image", Arrays.equals(expectedBytes, buffer.get()));

    }

}

