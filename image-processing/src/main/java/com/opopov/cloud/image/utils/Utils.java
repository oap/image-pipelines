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

package com.opopov.cloud.image.utils;

import com.opopov.cloud.image.service.IndexMap;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.CompletableFuture;

/**
 * Created by elx01 on 3/20/17.
 */
@Component
public class Utils {
    public static <T> CompletableFuture<T> fromListenableFuture(ListenableFuture<T> listenable) {
        CompletableFuture<T> completable = new CompletableFuture<T>() {

            @Override
            public boolean isCancelled() {
                return listenable.isCancelled();
            }

            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                //delegate cancel to the wrapped listenable
                boolean cancelledStatus = listenable.cancel(mayInterruptIfRunning);
                super.cancel(mayInterruptIfRunning);
                return cancelledStatus;
            }
        };

        //now delegate the callbacks
        ListenableFutureCallback<T> callback = new ListenableFutureCallback<T>() {
            @Override
            public void onFailure(Throwable ex) {
                //delegate exception
                completable.completeExceptionally(ex);
            }

            @Override
            public void onSuccess(T result) {
                //delegate success
                completable.complete(result);
            }
        };

        listenable.addCallback(callback);

        return completable;

    }

    private static IndexMap createIndexesMap(int columns, int rows) {
        return new IndexMap(columns * rows);
    }
}
