/*
 * MIT License
 *
 * Copyright (c) 2017 Oleg Popov <github@opopov.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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