package com.sdremthix.com.gridview.domain;

public final class GridProperties {

    final GridLine gridLine;

    final int columns;

    final int rows;

    final boolean drawGrid;

    final boolean snapToGrid;

    final boolean renderToImage;

    final boolean squareCells;

    public GridProperties(GridLine gridLine, int columns, int rows, boolean drawGrid, boolean snapToGrid, boolean renderToImage, boolean squareCells) {
        this.gridLine = gridLine;
        this.columns = columns;
        this.rows = rows;
        this.drawGrid = drawGrid;
        this.snapToGrid = snapToGrid;
        this.renderToImage = renderToImage;
        this.squareCells = squareCells;
    }

    /**
     * Getters.
     */

    public GridLine getGridLine() {
        return gridLine;
    }

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
}
