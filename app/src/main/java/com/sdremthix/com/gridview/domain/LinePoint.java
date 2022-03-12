package com.sdremthix.com.gridview.domain;

/**
 * Single line point.
 */
public final class LinePoint {
    private final float xPos;
    private final float yPos;

    public LinePoint(float xPos, float yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    /**
     * Getters.
     */

    public float getXPos() {
        return xPos;
    }

    public float getYPos() {
        return yPos;
    }
}
