package com.sdremthix.com.gridview.domain;

/**
 * Grid visual class for defining the grid line attributes.
 */
public final class GridPaint {

    private final int MAX_LINE_WIDTH = 15;
    private final int MIN_LINE_WIDTH = 1;

    private final int lineWidth;

    /**
     * Hex color value.
     */
    private final String color;

    /**
     * Default constructor for generating default values.
     */
    public GridPaint() {
        lineWidth = 3;
        color = "#808080";
    }

    /**
     * Attribute constructor.
     *
     * @param lineWidth The grid line width value. Min @link MIN_LINE_WIDTH and max width @link MAX_LINE_WIDTH
     * @param color     The grid color value.
     */
    public GridPaint(int lineWidth, String color) {
        if (lineWidth < MIN_LINE_WIDTH) {
            lineWidth = MIN_LINE_WIDTH;
        } else if (lineWidth > MAX_LINE_WIDTH) {
            lineWidth = MAX_LINE_WIDTH;
        }
        this.lineWidth = lineWidth;
        this.color = color;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public String getColor() {
        return color;
    }
}
