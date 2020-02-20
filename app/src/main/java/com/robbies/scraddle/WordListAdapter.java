package com.robbies.scraddle;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.robbies.scraddle.WordData.Word;

import java.util.List;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {

    private final LayoutInflater mInflater;
    private final String[] COLOUR_CHOICES = {"#F44336", "#F44336", "#4CAF50", "#8BC34A", "#CDDC39", "#FFEB3B", "#FFC107", "#FF9800", "#FF5722"};
    private List<Word> mWords; // Cached copy of words
    private OnWordClickListener listener;


    WordListAdapter(Context context, OnWordClickListener listener) {
        mInflater = LayoutInflater.from(context);
        this.listener = listener;

    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = mInflater.inflate(R.layout.recycler_view_word_item, parent, false);

        return new WordViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {

        if (mWords != null) {

            Word current = mWords.get(position);
            if (current != null) {
                holder.wordItemView.setText(current.getWord());
                holder.scrabbleScore.setText(String.format("%spts", current.getScrabbleValue()));

                String backgroundColorOfWord = COLOUR_CHOICES[0];
                backgroundColorOfWord = current.getWord().length() <= 8 ? COLOUR_CHOICES[current.getWord().length()] : backgroundColorOfWord;

                holder.scrabbleScore.setBackgroundColor(Color.parseColor(backgroundColorOfWord));
                holder.wordItemView.setBackgroundColor(Color.parseColor(backgroundColorOfWord));
            }

        } else {
            holder.wordItemView.setText(R.string.no_words);
        }

    }


    void setWords(List<Word> words) {
        mWords = words;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mWords != null ? mWords.size() : 0;
    }

    public interface OnWordClickListener {
        void onWordClick(String word);
    }

    class WordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView wordItemView;
        private final TextView scrabbleScore;
        private OnWordClickListener mListener;


        private WordViewHolder(View itemView, OnWordClickListener listener) {
            super(itemView);
            this.mListener = listener;
            itemView.setOnClickListener(this);

            wordItemView = itemView.findViewById(R.id.textViewSingleWordAnagramItem);
            scrabbleScore = itemView.findViewById(R.id.anagramTextViewScrabbleScore);
        }

        @Override
        public void onClick(View view) {
            mListener.onWordClick(wordItemView.getText().toString());
        }
    }


}