package com.sdremthix.com.gridview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sdremthix.com.gridview.domain.GridLine;
import com.sdremthix.com.gridview.domain.GridProperties;
import com.sdremthix.com.gridview.domain.KDSearchTree;
import com.sdremthix.com.gridview.domain.LinePoint;
import com.sdremthix.com.gridview.domain.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ObjectDraw extends View {
    public static final String TAG = "SRKI";
    private float mPosX, mPosY, xMove, yMove;
    private int pointerID;
    private boolean isMoving;


    @Nullable
    private Bitmap bitmapImage;

    private Paint bitmapPaint;

    private final Paint gridPaint = new Paint();

    @Nullable
    private GridProperties gridProperties;

    private KDSearchTree searchTree = new KDSearchTree(2);
    private Map<LinePoint, Pair<GridLine, GridLine>> grid = new HashMap<>();


    public ObjectDraw(Context context) {
        super(context);
        init();
    }

    public ObjectDraw(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ObjectDraw(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ObjectDraw(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (this.gridProperties != null && this.gridProperties.isDrawGrid()) {

            drawGridOnCanvas(this.gridProperties, canvas);
        }

        if (bitmapImage != null) {
            canvas.drawBitmap(bitmapImage, mPosX, mPosY, this.bitmapPaint);
        }

    }

    private void init() {

        this.bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public final void setBitmapImage(@NonNull Bitmap bitmapImage) {
        this.bitmapImage = bitmapImage;

    }

    public void drawGrid(@NonNull GridProperties gridProperties) {
        this.gridProperties = gridProperties;
        this.grid.clear();
        this.searchTree.clear();
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                final int pointerIndex = event.getActionIndex();
                final float x = event.getX(pointerIndex);
                final float y = event.getY(pointerIndex);

                xMove = x;
                yMove = y;
                pointerID = event.getPointerId(0);
                isMoving = false;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = event.findPointerIndex(pointerID);
                isMoving = true;

                if (pointerIndex != -1) {
                    final float x = event.getX(pointerIndex);
                    final float y = event.getY(pointerIndex);
                    final float dx = x - xMove;
                    final float dy = y - yMove;

                    mPosX += dx;
                    mPosY += dy;

                    invalidate();
                    xMove = x;
                    yMove = y;
                }
                break;
            }
            case MotionEvent.ACTION_UP:
                isMoving = false;
                break;
            case MotionEvent.ACTION_POINTER_UP: {
                final int pointerIndex = event.getActionIndex();
                final int pointID = event.getPointerId(pointerIndex);
                if (pointID == pointerID) {
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    xMove = event.getX(newPointerIndex);
                    yMove = event.getY(newPointerIndex);
                    pointerID = event.getPointerId(newPointerIndex);
                }
                isMoving = false;
            }
        }
        return true;
    }

    private void drawGridOnCanvas(@NonNull final GridProperties gridProperties, final Canvas canvas) {
        gridPaint.setStrokeWidth(3);
        gridPaint.setColor(Color.RED);
        //Calculate new grid
        if (grid.isEmpty()) {
            int width = getWidth();
            int height = getHeight();
            List<GridLine> tempLineData;
            if (gridProperties.isSquareCells()) {
                tempLineData = generateSquareCells(width, height, width / gridProperties.getCellWidthPercentage(), gridPaint);
            } else {
                tempLineData = generateColumnsAndRowsGrid(gridProperties.getColumns(), gridProperties.getRows(), width, height, gridPaint);
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
        //draw grid
        for (Map.Entry<LinePoint, Pair<GridLine, GridLine>> mapEntry : grid.entrySet()) {
            //Horizontal
            canvas.drawLine(mapEntry.getValue().getFirst().getStartPoint().getXPos(), mapEntry.getValue().getFirst().getStartPoint().getYPos(), mapEntry.getValue().getFirst().getEndPoint().getXPos(), mapEntry.getValue().getFirst().getEndPoint().getYPos(), gridPaint);
            //Vertical
            canvas.drawLine(mapEntry.getValue().getSecond().getStartPoint().getXPos(), mapEntry.getValue().getSecond().getStartPoint().getYPos(), mapEntry.getValue().getSecond().getEndPoint().getXPos(), mapEntry.getValue().getSecond().getEndPoint().getYPos(), gridPaint);
        }


        if (gridProperties.isSnapToGrid() && !isMoving) {
            Log.d(TAG, "onDraw: " + searchTree.toString());

            LinePoint isSnap = new LinePoint(mPosX, mPosY);
            final KDSearchTree.Node nearestPoint = searchTree.findNearestNeighbor(new KDSearchTree.NodePoint(Arrays.asList(mPosX, mPosY)));
            if (nearestPoint != null && isInThreshold(nearestPoint.getNodePoint(), mPosX, mPosY)) {
                mPosX = nearestPoint.getNodePoint().get(0);
                mPosY = nearestPoint.getNodePoint().get(1);
            }

//                if (grid.containsKey(isSnap)) {
//                    Log.d("SRKI", "onDraw: MRSKBJKDHkjGHDKJGFEJ");
//                    Pair<GridLine, GridLine> lines = grid.get(isSnap);
//                    mPosX = lines.getFirst().getStartPoint().getXPos();
//                    mPosY = lines.getSecond().getStartPoint().getYPos();
//                }
        }
    }

    private List<GridLine> generateSquareCells(final int width, final int height, final int cellWidth, @NonNull Paint gridPaint) {
        int columns = width / cellWidth;
        int rows = height / cellWidth;

        return generateColumnsAndRowsGrid(columns, rows, width, height, gridPaint);
    }

    private List<GridLine> generateColumnsAndRowsGrid(final int numColumns, final int numRows, final int width, final int height, @NonNull Paint gridPaint) {
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
                GridLine horizontal = new GridLine(horizontalStart, horizontalEnd, "" + gridPaint.getColor(), gridPaint.getStrokeWidth());

                //draw vertical
                LinePoint verticalStart = new LinePoint(startWidth, 0);
                LinePoint verticalEnd = new LinePoint(startWidth, height);
                GridLine vertical = new GridLine(verticalStart, verticalEnd, "" + gridPaint.getColor(), gridPaint.getStrokeWidth());

                tempLineData.add(horizontal);
                tempLineData.add(vertical);

                //Update spacings
                startWidth += spacingWidth;
                startHeight += spacingHeight;
            }
        }

        return tempLineData;
    }

    private boolean isInThreshold(@NonNull KDSearchTree.NodePoint nodePoint, float x, float y) {
        Log.d(TAG, "isInThreshold: " + x);
        final float xThreshold = x + (x / 100 * 10);
        Log.d(TAG, "isInThreshold: " + xThreshold);

        Log.d(TAG, "isInThreshold: " + y);
        final float yThreshold = y + (y / 100 * 10);
        Log.d(TAG, "isInThreshold: " + yThreshold);

        return nodePoint.get(0) <= xThreshold && nodePoint.get(1) <= yThreshold;
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
}
