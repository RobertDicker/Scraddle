package com.robbies.scraddle;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.robbies.scraddle.WordData.WordViewModel;


public class CheckWordDialogFragment extends DialogFragment {

    private WordViewModel wordViewModel;
    private EditText editText;
    private TextView definition;
    private TextView definitionLabel;
    private GradientDrawable drawable;
    private boolean searched;
    private Drawable buttonDrawable;
    private GradientDrawable divider;
    private Observer<String> wordObserver;

    public CheckWordDialogFragment() {
        // Required empty public constructor
    }

    static CheckWordDialogFragment newInstance() {
        return new CheckWordDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View views = inflater.inflate(R.layout.word_check_alert_dialog, null);
        wordViewModel = new ViewModelProvider(requireActivity()).get(WordViewModel.class);

        final Button checkWordButton = views.findViewById(R.id.button2);
        searched = false;
        editText = views.findViewById(R.id.wordCheckEditText);
        definition = views.findViewById(R.id.checkWordDefinitionTextView);
        definitionLabel = views.findViewById(R.id.checkWordTextViewDefinitionLabel);
        drawable = (GradientDrawable) views.getBackground();
        divider = (GradientDrawable) views.findViewById(R.id.wordCheckerImageViewDividerHorizontal).getBackground();
        buttonDrawable = checkWordButton.getBackground();

        wordObserver = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    drawable.setStroke(3, Color.GREEN);
                    divider.setStroke(2, Color.GREEN);
                    definitionLabel.setVisibility(View.VISIBLE);
                    definition.setVisibility(View.VISIBLE);
                    definition.setText(s);
                    buttonDrawable.setTint(Color.GREEN);

                } else {

                    drawable.setStroke(3, Color.RED);
                    divider.setStroke(2, Color.RED);
                    definitionLabel.setVisibility(View.GONE);
                    definition.setVisibility(View.GONE);
                    definition.setText("");
                    buttonDrawable.setTint(Color.RED);
                }
                wordViewModel.getDefinition(editText.getText().toString()).removeObservers(getViewLifecycleOwner());
            }
        };


        checkWordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                searched = !searched;

                if (searched) {

                    checkWordButton.setText(R.string.try_again);
                    wordViewModel.getDefinition(editText.getText().toString()).observe(requireActivity(), wordObserver);

                } else {

                    TypedValue typedValue = new TypedValue();
                    Resources.Theme theme = requireContext().getTheme();
                    theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
                    @ColorInt int color = typedValue.data;


                    checkWordButton.setText(R.string.check_word);
                    buttonDrawable.setTint(color);
                    editText.setText("");
                    drawable.setStroke(2, color);
                    divider.setStroke(2, color);

                }
            }


        });

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }


        return views;
    }


}
