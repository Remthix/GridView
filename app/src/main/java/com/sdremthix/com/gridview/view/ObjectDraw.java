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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class ObjectDraw extends View {
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
        if (this.gridProperties != null) {

            gridPaint.setStrokeWidth(3);
            gridPaint.setColor(Color.RED);
            if (grid.isEmpty()) {
                int width = getWidth();
                int height = getHeight();

                int spacingWidth = width / this.gridProperties.getColumns();
                int spacingHeight = height / this.gridProperties.getRows();

                int startWidth = 0;
                int startHeight = 0;
                for (int i = 0; i < this.gridProperties.getColumns(); i++) {
                    for (int j = 0; j < this.gridProperties.getRows(); j++) {

                        //draw horizontal
                        LinePoint horizontalStart = new LinePoint(0, startHeight);
                        LinePoint horizontalEnd = new LinePoint(width, startHeight);
                        GridLine horizontal = new GridLine(horizontalStart, horizontalEnd, "" + gridPaint.getColor(), gridPaint.getStrokeWidth());

                        //draw vertical
                        LinePoint verticalStart = new LinePoint(startWidth, 0);
                        LinePoint verticalEnd = new LinePoint(startWidth, height);
                        GridLine vertical = new GridLine(verticalStart, verticalEnd, "" + gridPaint.getColor(), gridPaint.getStrokeWidth());

                        //calculate intersection point
                        LinePoint intersection = getIntersectionPoint(horizontalStart, horizontalEnd, verticalStart, verticalEnd);
                        if (intersection != null) {
                            final KDSearchTree.Node node = new KDSearchTree.Node(Arrays.asList(intersection.getXPos(), intersection.getYPos()));
                            //store line intersection in map
                            grid.put(intersection, new Pair<>(horizontal, vertical));
                            searchTree.add(node);
                        }

                        //add horizontal line
                        startWidth += spacingWidth;
                        startHeight += spacingHeight;
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
                Log.d("SRKI", "onDraw: " + searchTree.toString());

                LinePoint isSnap = new LinePoint(mPosX, mPosY);
                final KDSearchTree.Node nearestPoint = searchTree.findNearestNeighbor(new KDSearchTree.NodePoint(Arrays.asList(mPosX, mPosY)));
                if (nearestPoint != null && isInThreshold(nearestPoint.getNodePoint(),mPosX,mPosY)) {
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

        if (bitmapImage != null) {
            canvas.drawBitmap(bitmapImage, mPosX, mPosY, this.bitmapPaint);
        }

    }

    private boolean isInThreshold(@NonNull KDSearchTree.NodePoint nodePoint, float x, float y) {
        boolean result;
        Log.d("SRKI", "isInThreshold: " + x);
        final float xThreshold = x + (x / 100 * 10);
        Log.d("SRKI", "isInThreshold: " + xThreshold);

        Log.d("SRKI", "isInThreshold: " + y);
        final float yThreshold = y + (y / 100 * 10);
        Log.d("SRKI", "isInThreshold: " + yThreshold);

        return nodePoint.get(0) <= xThreshold && nodePoint.get(1) <= yThreshold;
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

    /**
     * P(li,l2)
     *
     * @param l1a x1,y1
     * @param l1b x2,y2
     * @param l2a x3,y3
     * @param l2b x4,y4
     * @return
     */
    private LinePoint getIntersectionPoint(LinePoint l1a, LinePoint l1b, LinePoint l2a, LinePoint l2b) {

        float x1 = l1a.getXPos();
        float y1 = l1a.getYPos();
        float x2 = l1b.getXPos();
        float y2 = l1b.getYPos();
        float x3 = l2a.getXPos();
        float y3 = l2a.getYPos();
        float x4 = l2b.getXPos();
        float y4 = l2b.getYPos();


        float a = (x1 * y2 - y1 * x2);
        float b = (x3 * y4 - y3 * x4);
        float determinant = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (determinant == 0) {
            return null;
        }
        float x = (a * (x3 - x4) - (x1 - x2) * b) / determinant;
        float y = (a * (y3 - y4) - (y1 - y2) * b) / determinant;
        return new LinePoint(x, y);
    }
}
