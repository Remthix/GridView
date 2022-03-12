package com.sdremthix.com.gridview.domain;

/**
 * Grid line data.
 */
public final class GridLine {
    private final LinePoint startPoint;
    private final LinePoint endPoint;

    private final String color;

    private final float thickness;

    public GridLine(LinePoint startPoint, LinePoint endPoint, String color, float thickness) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.color = color;
        this.thickness = thickness;
    }

    /**
     * Getters.
     */


    public LinePoint getStartPoint() {
        return startPoint;
    }

    public LinePoint getEndPoint() {
        return endPoint;
    }


    public String getColor() {
        return color;
    }

    public float getThickness() {
        return thickness;
    }
}
