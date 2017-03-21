package com.opopov.cloud.image.service;

import com.opopov.cloud.image.api.ImageStitchingConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Created by elx01 on 3/21/17.
 */
public interface ImageStitchingService {
    DeferredResult<ResponseEntity<?>> getStitchedImage(@RequestBody ImageStitchingConfiguration config);
}
