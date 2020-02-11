package com.robbies.scraddle;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class WordEnterWordFragment extends Fragment {

    private  FragmentListener mListener;
    private StringBuilder yourWordBuilder;
    private Keyboard mKeyboard;
    private KeyboardView mKeyboardView;
    EditText yourLettersTV;


    static WordEnterWordFragment newInstance() {
        return new WordEnterWordFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            final Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_enter_word, container, false);


        yourWordBuilder = new StringBuilder();
        yourLettersTV = root.findViewById(R.id.editTextYourWord);

        yourLettersTV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mListener.updateWord(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        //===================KEYBOARD ====================
        // Create the Keyboard
        mKeyboard= new Keyboard(requireContext(),R.xml.keyboard_letters_portrait);

        // Lookup the KeyboardView
        mKeyboardView = root.findViewById(R.id.keyboardview);
        // Attach the keyboard to the view
        mKeyboardView.setKeyboard( mKeyboard );

        // Do not show the preview balloons
        mKeyboardView.setPreviewEnabled(false);

        // Install the key handler
        mKeyboardView.setOnKeyboardActionListener(mOnKeyboardActionListener);



        //===================KEYBOARD ====================

        mKeyboardView.setVisibility(View.VISIBLE);
        mKeyboardView.setEnabled(true);
        if( root!=null){((InputMethodManager)requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(root.getWindowToken(), 0);}

        return root;
    }


    public void updateDisplayedWord(int letterValue){

        switch(letterValue){

            //Backspace
            case(-5):
                if(yourWordBuilder.length() > 0){
                    yourWordBuilder.delete(yourWordBuilder.length()-1, yourWordBuilder.length());
                }
                break;
                //Done
            case(-3):
                    mListener.changePage(1);
                    break;
            default:
                if(yourWordBuilder.length() < R.string.maxAnagramLetters){
                yourWordBuilder.append((char)letterValue);}
        }
        yourLettersTV.setText(yourWordBuilder.toString());
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentListener) {
            mListener = (FragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private KeyboardView.OnKeyboardActionListener mOnKeyboardActionListener = new KeyboardView.OnKeyboardActionListener() {
        @Override public void onKey(int primaryCode, int[] keyCodes)
        {
        }

        @Override public void onPress(int arg0) {

            updateDisplayedWord(arg0);
        }

        @Override public void onRelease(int primaryCode) {
        }

        @Override public void onText(CharSequence text) {

        }

        @Override public void swipeDown() {

        }

        @Override public void swipeLeft() {
        }

        @Override public void swipeRight() {
        }

        @Override public void swipeUp() {

        }
    };




}