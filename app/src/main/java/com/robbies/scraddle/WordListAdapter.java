package com.robbies.scraddle;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {

    private final LayoutInflater mInflater;
    private List<Word> mWords; // Cached copy of words
    private final String[] COLOUR_CHOICES = {"#F44336", "#F44336", "#4CAF50", "#8BC34A", "#CDDC39", "#FFEB3B", "#FFC107", "#FF9800", "#FF5722"};

    WordListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recycler_view_word_item, parent, false);
        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {

        if (mWords != null) {
            Word current = mWords.get(position);
            holder.wordItemView.setText(current.getWord());


            String backgroundColorOfWord = COLOUR_CHOICES[0];
            backgroundColorOfWord = current.getWord().length() <= 8 ? COLOUR_CHOICES[current.getWord().length()] : backgroundColorOfWord;

            holder.wordItemView.setBackgroundColor(Color.parseColor(backgroundColorOfWord));




        } else {
            // Covers the case of data not being ready yet.
            holder.wordItemView.setText("No Word");
        }


    }


    void setWords(List<Word> words) {
        mWords = words;
        notifyDataSetChanged();
    }


    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mWords != null)
            return mWords.size();
        else return 0;
    }

    class WordViewHolder extends RecyclerView.ViewHolder {
        private final TextView wordItemView;

        private WordViewHolder(View itemView) {
            super(itemView);
            wordItemView = itemView.findViewById(R.id.textViewSingleWordAnagramItem);
        }
    }
}