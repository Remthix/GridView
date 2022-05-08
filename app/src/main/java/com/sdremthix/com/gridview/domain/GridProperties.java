package com.sdremthix.com.gridview.domain;

public final class GridProperties {

    final int MAX_COLUMNS = 50;
    final int MAX_ROWS = 50;

    /**
     * Cell width ass a factor of the embedding view width and height.
     */
    final int MAX_CELL_WIDTH = 20;

    final int columns;

    final int rows;

    final boolean drawGrid;

    final boolean snapToGrid;

    final boolean renderToImage;

    final boolean squareCells;

    private final GridPaint gridPaint;

    public GridProperties(int columns, int rows, boolean drawGrid, boolean snapToGrid, boolean renderToImage, boolean squareCells, GridPaint gridPaint) throws IllegalArgumentException {

        if (columns <= 0 || rows <= 0) {
            throw new IllegalArgumentException(GridProperties.class.getSimpleName() + ": number for either rows or columns must be greater than 0!");
        }

        if (columns > MAX_COLUMNS) {
            throw new IllegalArgumentException(GridProperties.class.getSimpleName() + ": Number of columns cannot exceed: " + MAX_COLUMNS);
        }

        if (rows > MAX_ROWS) {
            throw new IllegalArgumentException(GridProperties.class.getSimpleName() + ": Number of rows cannot exceed: " + MAX_ROWS);
        }
        this.columns = columns;
        this.rows = rows;
        this.drawGrid = drawGrid;
        this.snapToGrid = snapToGrid;
        this.renderToImage = renderToImage;
        this.squareCells = squareCells;
        this.gridPaint = gridPaint;
    }

    /**
     * Getters.
     */

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public boolean isDrawGrid() {
        return drawGrid;
    }

    public boolean isSnapToGrid() {
        return snapToGrid;
    }

    public boolean isRenderToImage() {
        return renderToImage;
    }

    public boolean isSquareCells() {
        return squareCells;
    }

    public int snapToGridPercentage() {
        return 5;
    }

    public int getCellWidthPercentage() {
        return MAX_CELL_WIDTH;
    }

    public GridPaint getGridPaint() {
        return gridPaint;
    }
}
