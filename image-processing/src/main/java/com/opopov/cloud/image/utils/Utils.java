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
