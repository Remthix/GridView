package com.sdremthix.com.gridview.domain;

import com.sdremthix.com.gridview.search.algorithm.KDSearchTree;
import com.sdremthix.com.gridview.domain.contracts.IGridDrawer;
import com.sdremthix.com.gridview.domain.entities.LinePoint;
import com.sdremthix.com.gridview.domain.entities.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Grid view component generator.
 */
public final class GridDrawer implements IGridDrawer {

    private final GridProperties gridProperties;


    private final KDSearchTree searchTree = new KDSearchTree(2);
    private final Map<LinePoint, Pair<GridLine, GridLine>> grid = new HashMap<>();

    public GridDrawer(GridProperties gridProperties) {
        this.gridProperties = gridProperties;
        this.grid.clear();
        this.searchTree.clear();
    }

    public Map<LinePoint, Pair<GridLine, GridLine>> generateGrid(final int width, final int height) {
        //Calculate new grid
        if (grid.isEmpty()) {

            List<GridLine> tempLineData;
            if (gridProperties.isSquareCells()) {
                tempLineData = generateSquareCells(width, height, width / gridProperties.getCellWidthPercentage(), gridProperties.getGridPaint());
            } else {
                tempLineData = generateColumnsAndRowsGrid(gridProperties.getColumns(), gridProperties.getRows(), width, height, gridProperties.getGridPaint());
            }

            for (GridLine gridLine : tempLineData) {
                for (GridLine otherLine : tempLineData) {
                    if (gridLine != otherLine) {
                        //calculate intersection point
                        LinePoint intersection = getIntersectionPoint(gridLine.getStartPoint(), gridLine.getEndPoint(), otherLine.getStartPoint(), otherLine.getEndPoint());
                        if (intersection != null) {
                            final KDSearchTree.Node node = new KDSearchTree.Node(Arrays.asList(intersection.getXPos(), intersection.getYPos()));
                            //store line intersection in map
                            grid.put(intersection, new Pair<>(gridLine, otherLine));
                            searchTree.add(node);
                        }
                    }
                }
            }
        }

        return grid;
    }

    public boolean isDrawGridEnabled() {
        return this.gridProperties.isDrawGrid();
    }

    public boolean isSnapToGridEnabled() {
        return this.gridProperties.isSnapToGrid();
    }

    /**
     * Find nearest point to the coordinates passed and if any return the updated grid position.
     *
     * @param mPosX x coordinate position.
     * @param mPosY y coordinate position.
     * @return A new grid position based on the updated coordinates.
     */
    public LinePoint snapToGrid(float mPosX, float mPosY) {
        final KDSearchTree.Node nearestPoint = searchTree.findNearestNeighbor(new KDSearchTree.NodePoint(Arrays.asList(mPosX, mPosY)));
        if (nearestPoint != null && isInThreshold(nearestPoint.getNodePoint(), mPosX, mPosY)) {
            mPosX = nearestPoint.getNodePoint().get(0);
            mPosY = nearestPoint.getNodePoint().get(1);
        }

        return new LinePoint(mPosX, mPosY);
    }

    public int getLineWidth() {
        return this.gridProperties.getGridPaint().getLineWidth();
    }

    public String getLineColor() {
        return this.gridProperties.getGridPaint().getColor();
    }

    private List<GridLine> generateSquareCells(final int width, final int height, final int cellWidth, final GridPaint gridPaint) {
        int columns = width / cellWidth;
        int rows = height / cellWidth;

        return generateColumnsAndRowsGrid(columns, rows, width, height, gridPaint);
    }

    private List<GridLine> generateColumnsAndRowsGrid(final int numColumns, final int numRows, final int width, final int height, final GridPaint gridPaint) {
        List<GridLine> tempLineData = new ArrayList<>();
        int spacingWidth = width / numColumns;
        int spacingHeight = height / numRows;

        int startWidth = 0;
        int startHeight = 0;
        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numRows; j++) {

                //draw horizontal
                LinePoint horizontalStart = new LinePoint(0, startHeight);
                LinePoint horizontalEnd = new LinePoint(width, startHeight);
                GridLine horizontal = new GridLine(horizontalStart, horizontalEnd, "" + gridPaint.getColor(), gridPaint.getLineWidth());

                //draw vertical
                LinePoint verticalStart = new LinePoint(startWidth, 0);
                LinePoint verticalEnd = new LinePoint(startWidth, height);
                GridLine vertical = new GridLine(verticalStart, verticalEnd, "" + gridPaint.getColor(), gridPaint.getLineWidth());

                tempLineData.add(horizontal);
                tempLineData.add(vertical);

                //Update spacings
                startWidth += spacingWidth;
                startHeight += spacingHeight;
            }
        }

        return tempLineData;
    }

    /**
     * P(li,l2)
     *
     * @param l1start x1,y1
     * @param l1end   x2,y2
     * @param l2start x3,y3
     * @param l2end   x4,y4
     * @return
     */
    private LinePoint getIntersectionPoint(LinePoint l1start, LinePoint l1end, LinePoint l2start, LinePoint l2end) {

        float x1 = l1start.getXPos();
        float y1 = l1start.getYPos();
        float x2 = l1end.getXPos();
        float y2 = l1end.getYPos();
        float x3 = l2start.getXPos();
        float y3 = l2start.getYPos();
        float x4 = l2end.getXPos();
        float y4 = l2end.getYPos();

        //p0_x = x1
        //p1_x = x2
        //p2_x = x3
        //p3_x = x4

        //p0_y = y1
        //p1_y = y2
        //p2_y = y3
        //p3_y = y4

        float s1_x, s1_y, s2_x, s2_y;

        s1_x = x2 - x1;
        s1_y = y2 - y1;
        s2_x = x4 - x3;
        s2_y = y4 - y3;

        float s, t;
        float v = -s2_x * s1_y + s1_x * s2_y;
        if (v == 0) {
            return null;
        }
        s = (-s1_y * (x1 - x3) + s1_x * (y1 - y3)) / v;
        t = (s2_x * (y1 - y3) - s2_y * (x1 - x3)) / v;

        if (s >= 0 && s <= 1 && t >= 0 && t <= 1) {
            // Collision detected
            float x = x1 + (t * s1_x);
            float y = y1 + (t * s1_y);
            return new LinePoint(x, y);
        }

        return null;
    }

    /**
     * Check if the coordinate position fall into the threshold value (currently 10% of the width or height value).
     *
     * @param nodePoint The nearest search coordinate point.
     * @param x         The current x position.
     * @param y         The current y position.
     * @return true if in threshold bounds and false if otherwise.
     */
    private boolean isInThreshold(KDSearchTree.NodePoint nodePoint, float x, float y) {
        final float xThreshold = x + (x / 100 * 10);
        final float yThreshold = y + (y / 100 * 10);

        return nodePoint.get(0) <= xThreshold && nodePoint.get(1) <= yThreshold;
    }
}
