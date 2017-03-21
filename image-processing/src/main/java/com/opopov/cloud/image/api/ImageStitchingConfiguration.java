package com.opopov.cloud.image.api;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by elx01 on 3/20/17.
 */
public class ImageStitchingConfiguration {
    private int sourceWidth;
    private int sourceHeight;
    private int rowCount;
    private int columnCount;
    private String outputFormat = "png"; //jpg, png, etc.
    private List<String> urlList = new LinkedList<>();

    @Override
    public String toString() {
        return "ImageStitchingConfiguration{" +
                "sourceWidth=" + sourceWidth +
                ", sourceHeight=" + sourceHeight +
                ", rowCount=" + rowCount +
                ", columnCount=" + columnCount +
                ", outputFormat='" + outputFormat + '\'' +
                ", urlList=" + urlList +
                '}';
    }

    public int getSourceWidth() {
        return sourceWidth;
    }

    public void setSourceWidth(int sourceWidth) {
        this.sourceWidth = sourceWidth;
    }

    public int getSourceHeight() {
        return sourceHeight;
    }

    public void setSourceHeight(int sourceHeight) {
        this.sourceHeight = sourceHeight;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }


}
