package com.sdremthix.com.gridview.view;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.DialogFragment;

import com.sdremthix.com.gridview.R;
import com.sdremthix.com.gridview.domain.GridProperties;

/**
 * Grid properties fragment dialog. Handlers the interaction callbacks for the Grid view settings and the dialog lifecycle.
 */
public final class GridPropertiesDialog extends DialogFragment {

    @Nullable
    private final GridPropertiesListener gridPropertiesListener;

    public GridPropertiesDialog() {
        gridPropertiesListener = null;
    }

    public GridPropertiesDialog(GridPropertiesListener gridPropertiesListener) {
        this.gridPropertiesListener = gridPropertiesListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Bundle arguments = getArguments();
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(STYLE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_fragment_grid_properties);
        final AppCompatCheckBox chkShowGrid = dialog.findViewById(R.id.chk_show_grid);
        final AppCompatCheckBox chkSnapToGrid = dialog.findViewById(R.id.chk_snap_to_grid);
        final AppCompatCheckBox chkRenderToImage = dialog.findViewById(R.id.chk_render_to_image);
        final AppCompatEditText editColumns = dialog.findViewById(R.id.edit_columns);
        final AppCompatEditText editRows = dialog.findViewById(R.id.edit_rows);

        dialog.findViewById(R.id.btn_dialog_apply).setOnClickListener(view -> {
            if (gridPropertiesListener != null) {
                gridPropertiesListener.onGridPropertiesUpdated(new GridProperties(Integer.parseInt(editColumns.getText().toString()),
                        Integer.parseInt(editRows.getText().toString()),
                        chkShowGrid.isChecked(), chkSnapToGrid.isChecked(),
                        chkRenderToImage.isChecked(), false));
            }
            dismiss();
        });

        dialog.findViewById(R.id.btn_dialog_close).setOnClickListener(view -> dismiss());

        chkShowGrid.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (gridPropertiesListener != null) {
                gridPropertiesListener.onGridPropertiesUpdated(new GridProperties(Integer.parseInt(editColumns.getText().toString()),
                        Integer.parseInt(editRows.getText().toString()),
                        checked, chkSnapToGrid.isChecked(),
                        chkRenderToImage.isChecked(), false));
            }
        });


        return dialog;
    }

    public interface GridPropertiesListener {
        void onGridPropertiesUpdated(final GridProperties updatedGridProperties);
    }
}
