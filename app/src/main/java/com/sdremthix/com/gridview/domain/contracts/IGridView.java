package com.sdremthix.com.gridview.domain.contracts;

import androidx.annotation.NonNull;

/**
 * Domain contract for the Android @link View component that will implement the canvas drawing
 * of the generated grid based on the properties passed to it.
 */
public interface IGridView {
    void drawGrid(@NonNull IGridDrawer gridDrawer);
}
