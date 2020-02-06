package com.robbies.scraddle;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.robbies.scraddle.Data.Player;
import com.robbies.scraddle.Data.PlayerRecord;
import com.robbies.scraddle.Data.ScoringViewModel;

public class CreatePlayerDialog extends DialogFragment {

    public CreatePlayerDialog() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final EditText editText = new EditText(getActivity());
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        editText.setSingleLine(true);
        final ScoringViewModel scoringViewModel = new ViewModelProvider(requireActivity()).get(ScoringViewModel.class);

        editText.setHint("Enter Player Name");
        return new AlertDialog.Builder(requireContext())
                .setTitle("Create New Player")
                .setMessage("Enter new players name below")
                .setView(editText)

                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!editText.getText().toString().isEmpty()) {
                            String editTextInput = editText.getText().toString().substring(0, 1).toUpperCase() + editText.getText().toString().trim().substring(1);

                            int playerId =(int) scoringViewModel.insertPlayer(new Player(editTextInput));
                            scoringViewModel.newPlayerRecord(new PlayerRecord(playerId));
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
    }
}
