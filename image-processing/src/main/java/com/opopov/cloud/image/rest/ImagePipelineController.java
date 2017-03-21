package com.opopov.cloud.image.rest;

/**
 * Created by elx01 on 3/20/17.
 */

import com.opopov.cloud.image.api.ImageStitchingConfiguration;
import com.opopov.cloud.image.service.ImageStitchingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
public class ImagePipelineController {

    @Autowired
    ImageStitchingServiceImpl imageStitchingService;


    public ImagePipelineController() {
        super();
    }

    // post is necessary here, since the amount of the configurable resources might not fit the GET length limit
    @RequestMapping(method = RequestMethod.POST, value = "/stitched-image")
    public DeferredResult<ResponseEntity<?>> getStitchedImage(@RequestBody ImageStitchingConfiguration config) {
        return imageStitchingService.getStitchedImage(config);
    }

}