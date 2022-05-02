package com.sdremthix.com.gridview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sdremthix.com.gridview.domain.GridDrawer;
import com.sdremthix.com.gridview.domain.GridLine;
import com.sdremthix.com.gridview.domain.GridPosition;
import com.sdremthix.com.gridview.domain.LinePoint;
import com.sdremthix.com.gridview.domain.Pair;

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
    private GridDrawer gridDrawer;

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
        if (this.gridDrawer != null && this.gridDrawer.isDrawGrid()) {
            drawGridOnCanvas(this.gridDrawer, canvas);
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

    public void drawGrid(@NonNull GridDrawer gridDrawer) {
        this.gridDrawer = gridDrawer;
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

    private void drawGridOnCanvas(@NonNull final GridDrawer gridDrawer, final Canvas canvas) {
        gridPaint.setStrokeWidth(3);
        gridPaint.setColor(Color.RED);

        final Map<LinePoint, Pair<GridLine, GridLine>> grid = gridDrawer.generateGrid(getWidth(), getHeight(), gridPaint);
        //draw grid
        for (Map.Entry<LinePoint, Pair<GridLine, GridLine>> mapEntry : grid.entrySet()) {
            //Horizontal
            canvas.drawLine(mapEntry.getValue().getFirst().getStartPoint().getXPos(), mapEntry.getValue().getFirst().getStartPoint().getYPos(), mapEntry.getValue().getFirst().getEndPoint().getXPos(), mapEntry.getValue().getFirst().getEndPoint().getYPos(), gridPaint);
            //Vertical
            canvas.drawLine(mapEntry.getValue().getSecond().getStartPoint().getXPos(), mapEntry.getValue().getSecond().getStartPoint().getYPos(), mapEntry.getValue().getSecond().getEndPoint().getXPos(), mapEntry.getValue().getSecond().getEndPoint().getYPos(), gridPaint);
        }


        if (gridDrawer.isSnapToGrid() && !isMoving) {
            final GridPosition updatedValues = gridDrawer.snapToGrid(mPosX, mPosY);
            mPosX = updatedValues.getXPos();
            mPosY = updatedValues.getYPos();
        }
    }
}
