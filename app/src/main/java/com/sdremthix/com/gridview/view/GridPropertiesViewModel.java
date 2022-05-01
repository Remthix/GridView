package com.sdremthix.com.gridview.view;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sdremthix.com.gridview.domain.GridProperties;

/**
 * GridProperties Android architecture ViewModel component.
 */
public final class GridPropertiesViewModel extends ViewModel {

    private final MutableLiveData<GridProperties> gridPropertiesData = new MutableLiveData<>();
    public final MutableLiveData<Boolean> showGridEnabled = new MutableLiveData<>();
    public final MutableLiveData<Boolean> snapToGridEnabled = new MutableLiveData<>();

    public void updateGrid(final GridProperties gridProperties) {
        gridPropertiesData.setValue(gridProperties);
        showGridEnabled.setValue(gridProperties.isDrawGrid());
        snapToGridEnabled.setValue(gridProperties.isSnapToGrid());
    }

    public LiveData<GridProperties> observeGridProperties() {
        return this.gridPropertiesData;
    }

}
