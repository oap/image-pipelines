package com.opopov.cloud.image.service;

import com.opopov.cloud.image.api.ImageStitchingConfiguration;
import com.opopov.cloud.image.exception.InvalidImageConfigException;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by elx01 on 3/21/17.
 */
@Component
public class Validator {
    //contains allowed image formats
    Set<String> supportedImageWriteFormats = ConcurrentHashMap.newKeySet();


    public Validator() {
        supportedImageWriteFormats.addAll(Arrays.asList(ImageIO.getWriterFormatNames()));
    }

    public void validateConfig(ImageStitchingConfiguration config) throws InvalidImageConfigException {
        if (config.getUrlList().size() !=
                config.getRowCount() * config.getColumnCount()) {
            throw new InvalidImageConfigException(
                    String.format(
                            "The list of the URLs must contain %d elements, but it contains %d elements",
                            config.getRowCount() * config.getColumnCount(),
                            config.getUrlList().size()
                    )
            );
        }

        if (config.getOutputFormat() == null || !supportedImageWriteFormats.contains(config.getOutputFormat())) {
            throw new InvalidImageConfigException("The specified format " + config.getOutputFormat() + " is invalid" +
                    " please, specify one of the supported formats - " + String.join(", ", supportedImageWriteFormats)
            );
        }


    }
}
