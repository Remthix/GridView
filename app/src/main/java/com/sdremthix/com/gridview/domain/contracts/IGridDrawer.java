package com.sdremthix.com.gridview.domain.contracts;

import com.sdremthix.com.gridview.domain.GridLine;
import com.sdremthix.com.gridview.domain.entities.LinePoint;
import com.sdremthix.com.gridview.domain.entities.Pair;

import java.util.Map;

/**
 * Domain contract for the GridDrawer component.
 */
public interface IGridDrawer {

    Map<LinePoint, Pair<GridLine, GridLine>> generateGrid(final int width, final int height);

    boolean isDrawGridEnabled();

    boolean isSnapToGridEnabled();

    LinePoint snapToGrid(float mPosX, float mPosY);

    int getLineWidth();

    String getLineColor();
}
