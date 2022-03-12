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
import com.sdremthix.com.gridview.domain.LinePoint;
import com.sdremthix.com.gridview.domain.Pair;

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
                        //store lineintersection in map
                        grid.put(intersection, new Pair<>(horizontal, vertical));

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
                Log.d("SRKI", "onDraw: MRSKBJKDHkjGHDKJGFEJ");

                LinePoint isSnap = new LinePoint(mPosX, mPosY);
                if (grid.containsKey(isSnap)) {
                    Log.d("SRKI", "onDraw: MRSKBJKDHkjGHDKJGFEJ");
                    Pair<GridLine, GridLine> lines = grid.get(isSnap);
                    mPosX = lines.getFirst().getStartPoint().getXPos();
                    mPosY = lines.getSecond().getStartPoint().getYPos();
                }
            }
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
     * @param l1a
     * @param l1b
     * @param l2a
     * @param l2b
     * @return
     */
    private LinePoint getIntersectionPoint(LinePoint l1a, LinePoint l1b, LinePoint l2a, LinePoint l2b) {
        int thresholdPercentage = 10;
        float a = (l1a.getXPos() * l1b.getYPos() - l1a.getYPos() * l1b.getXPos());
        float b = (l2a.getXPos() * l2b.getYPos() - l2a.getYPos() * l2b.getXPos());
        float determinant = (l1a.getXPos() - l1b.getXPos()) * (l2a.getYPos() - l2b.getYPos()) - (l1a.getYPos() - l1b.getYPos()) * (l2a.getXPos() - l2b.getXPos());
        float x = (a * (l2a.getXPos() - l2b.getXPos()) - (l1a.getXPos() - l1b.getXPos()) * b) / determinant;
        float y = (a * (l2a.getYPos()) - l2b.getYPos() - (l1a.getYPos() - l1b.getYPos()) * b);
        return new LinePoint(x + (x / 100 * thresholdPercentage), y + (y / 100 * thresholdPercentage));
    }
}
