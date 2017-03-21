package com.opopov.cloud.image.service;

import com.opopov.cloud.image.service.DecodedImage;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Contains a map, where the key is the id of the cell,
 * and the value is the java.awt.Image containing the decompressed image data
 * <p>
 * | 1 | 2  | 3 |
 * | 4 | 5  | 6 |
 * | 7 | 8  | 9 |
 */
public class IndexMap extends ConcurrentHashMap<Integer, Optional<DecodedImage>> implements Serializable {
    public IndexMap(int initialCapacity) {
        super(initialCapacity);
    }
}
