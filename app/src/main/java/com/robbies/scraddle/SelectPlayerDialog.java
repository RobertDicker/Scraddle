package com.robbies.scraddle;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

class SelectPlayerDialog extends DialogFragment {

    private SelectPlayerDialogOnClickListener selectPlayerDialogOnClickListener;

    public SelectPlayerDialog() {

    }

    static SelectPlayerDialog getInstance(String title, String[] items, boolean[] checkedItems, SelectPlayerDialogOnClickListener listener) {

        SelectPlayerDialog dialog = new SelectPlayerDialog();
        dialog.setOnClickListener(listener);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putStringArray("items", items);
        bundle.putBooleanArray("checkedItems", checkedItems);
        dialog.setArguments(bundle);
        return dialog;
    }

    private void setOnClickListener(SelectPlayerDialogOnClickListener listener) {
        this.selectPlayerDialogOnClickListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String title = getArguments().getString("title", "");
        String[] items = getArguments().getStringArray("items");
        final boolean[] checkedItems = getArguments().getBooleanArray("checkedItems");

        return new AlertDialog.Builder(requireActivity()).
                setTitle(title).setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int index, boolean isChecked) {

                if (checkedItems != null) {
                    checkedItems[index] = isChecked;
                }

            }
        })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int buttonId) {
                        selectPlayerDialogOnClickListener.doPositiveClick(checkedItems);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();


    }

    public interface SelectPlayerDialogOnClickListener {
        void doPositiveClick(boolean[] checkedItems);

    }
}
