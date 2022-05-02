package com.sdremthix.com.gridview.domain;

/**
 * Grid canvas x, y positions.
 */
public final class GridPosition {

    private final float xPos;
    private final float yPos;

    public GridPosition(float xPos, float yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public float getXPos() {
        return xPos;
    }

    public float getYPos() {
        return yPos;
    }
}
