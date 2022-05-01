package com.sdremthix.com.gridview.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.sdremthix.com.gridview.databinding.DialogFragmentGridPropertiesBinding;
import com.sdremthix.com.gridview.domain.GridProperties;

/**
 * Grid properties fragment dialog. Handlers the interaction callbacks for the Grid view settings and the dialog lifecycle.
 */
public final class GridPropertiesDialog extends DialogFragment {

    @Nullable
    private GridPropertiesViewModel gridPropertiesViewModel;

    private DialogFragmentGridPropertiesBinding binding;

    public GridPropertiesDialog() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Listen to ViewModel events
        gridPropertiesViewModel = new ViewModelProvider(requireActivity()).get(GridPropertiesViewModel.class);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogFragmentGridPropertiesBinding.inflate(inflater, container, false);
        binding.setViewModel(gridPropertiesViewModel);
        if (getDialog() != null) {
            getDialog().requestWindowFeature(STYLE_NO_TITLE);
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnDialogApply.setOnClickListener(v -> {
            if (gridPropertiesViewModel != null) {
                gridPropertiesViewModel.updateGrid(new GridProperties(Integer.parseInt(binding.editColumns.getText().toString()),
                        Integer.parseInt(binding.editRows.getText().toString()),
                        binding.chkShowGrid.isChecked(), binding.chkSnapToGrid.isChecked(),
                        binding.chkRenderToImage.isChecked(), false));
            }
            dismiss();
        });

        binding.btnDialogClose.setOnClickListener(v -> dismiss());

        binding.chkShowGrid.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (gridPropertiesViewModel != null) {
                gridPropertiesViewModel.updateGrid(new GridProperties(Integer.parseInt(binding.editColumns.getText().toString()),
                        Integer.parseInt(binding.editRows.getText().toString()),
                        checked, binding.chkSnapToGrid.isChecked(),
                        binding.chkRenderToImage.isChecked(), false));
            }
        });
    }
}
