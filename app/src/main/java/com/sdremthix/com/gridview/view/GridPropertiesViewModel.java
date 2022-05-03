package com.sdremthix.com.gridview.view;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sdremthix.com.gridview.domain.GridDrawer;
import com.sdremthix.com.gridview.domain.GridProperties;
import com.sdremthix.com.gridview.domain.contracts.IGridDrawer;

/**
 * GridProperties Android architecture ViewModel component.
 */
public final class GridPropertiesViewModel extends ViewModel {

    private final MutableLiveData<IGridDrawer> gridDrawerMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<Boolean> showGridEnabled = new MutableLiveData<>();
    public final MutableLiveData<Boolean> snapToGridEnabled = new MutableLiveData<>();
    public final MutableLiveData<Boolean> squareCellsEnabled = new MutableLiveData<>();
    public final MutableLiveData<Integer> columns = new MutableLiveData<>(10);
    public final MutableLiveData<Integer> rows = new MutableLiveData<>(10);


    public void updateGrid(final GridProperties gridProperties) {
        gridDrawerMutableLiveData.setValue(new GridDrawer(gridProperties));
        showGridEnabled.setValue(gridProperties.isDrawGrid());
        snapToGridEnabled.setValue(gridProperties.isSnapToGrid());
        squareCellsEnabled.setValue(gridProperties.isSquareCells());
        columns.setValue(gridProperties.getColumns());
        rows.setValue(gridProperties.getRows());
    }

    public LiveData<IGridDrawer> observeGridDrawer() {
        return this.gridDrawerMutableLiveData;
    }

}
