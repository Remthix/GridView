package com.sdremthix.com.gridview;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.sdremthix.com.gridview.domain.GridProperties;
import com.sdremthix.com.gridview.domain.KDSearchTree;
import com.sdremthix.com.gridview.view.GridPropertiesDialog;
import com.sdremthix.com.gridview.view.ObjectDraw;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ObjectDraw objectDraw = findViewById(R.id.draw_main);

        findViewById(R.id.btn_grid_properties).setOnClickListener(view -> {
            //Toggle dialog display
            toggleGridPropertiesDisplay(objectDraw);
        });


        Bitmap image = getBitmapFromVectorDrawable(this, R.drawable.ic_launcher_background);
        objectDraw.setBitmapImage(image);
        objectDraw.drawGrid(new GridProperties(
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

    private void toggleGridPropertiesDisplay(@NonNull final ObjectDraw objectDraw){

        final GridPropertiesDialog gridPropertiesDialog = new GridPropertiesDialog(new GridPropertiesDialog.GridPropertiesListener() {
            @Override
            public void onGridPropertiesUpdated(GridProperties updatedGridProperties) {
                objectDraw.drawGrid(updatedGridProperties);
            }
        });

        final FragmentManager fragmentManager = getSupportFragmentManager();
        gridPropertiesDialog.show(getSupportFragmentManager(),"SRKI_FRAG");


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