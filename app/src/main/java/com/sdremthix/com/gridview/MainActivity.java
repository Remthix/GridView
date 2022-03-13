package com.sdremthix.com.gridview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.sdremthix.com.gridview.domain.GridProperties;
import com.sdremthix.com.gridview.domain.KDSearchTree;
import com.sdremthix.com.gridview.view.ObjectDraw;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ObjectDraw objectDraw = findViewById(R.id.draw_main);

        Bitmap image = getBitmapFromVectorDrawable(this, R.drawable.ic_launcher_background);
        objectDraw.setBitmapImage(image);
        objectDraw.drawGrid(new GridProperties(
                null,
                10, 3,
                true,
                true,
                false,
                false
        ));

        //Test Nearest Neighbor
        float[][] gridPoints = new float[][]{
                {50, 50, 1},
                {80, 40, 2},
                {10, 60, 3},
                {51, 38, 4},
                {48, 38, 5}
        };

        KDSearchTree kdSearchTree = new KDSearchTree(2);
        for (float[] point : gridPoints) {
            KDSearchTree.Node node = new KDSearchTree.Node(Arrays.asList(point[0], point[1]));
            kdSearchTree.add(node);
        }

        Log.d("SRKI", "onCreate: TREE: " + kdSearchTree);
        Log.d("SRKI", "Najbliza tacka: " + kdSearchTree.findNearestNeighbor(new KDSearchTree.NodePoint(Arrays.asList(40f, 40f))));
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }


}