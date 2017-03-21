package com.opopov.cloud.image.service;

import java.awt.image.BufferedImage;

/**
 * Created by elx01 on 3/20/17.
 */
public class DecodedImage {
    private BufferedImage image; //actualPayload
    private int imageIndex; //imageIndex within the list supplied by the client

    public DecodedImage(BufferedImage image, int imageIndex) {
        this.image = image;
        this.imageIndex = imageIndex;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public int getImageIndex() {
        return imageIndex;
    }

    public void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
    }
}
