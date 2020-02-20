package com.robbies.scraddle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.robbies.scraddle.WordData.WordAndDefinition;

import java.util.List;

public class WordCrosswordListAdapter extends RecyclerView.Adapter<WordCrosswordListAdapter.WordViewHolder> {

    private final LayoutInflater mInflater;
    private List<WordAndDefinition> mWords; // Cached copy of words

    WordCrosswordListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = mInflater.inflate(R.layout.recycler_view_crossword_item, parent, false);

        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {

        if (mWords != null) {
            WordAndDefinition current = mWords.get(position);
            holder.wordTextView.setText(current.getWord());
            holder.definition.setText(current.getDefinition());

        } else {
            holder.wordTextView.setText(R.string.no_words);
            holder.definition.setVisibility(View.GONE);
        }
    }


    void setWords(List<WordAndDefinition> words) {
        mWords = words;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mWords != null ? mWords.size() : 0;
    }

    class WordViewHolder extends RecyclerView.ViewHolder {
        private TextView wordTextView;
        private TextView definition;

        private WordViewHolder(View itemView) {
            super(itemView);

            wordTextView = itemView.findViewById(R.id.crosswordTextViewSingleWord);
            definition = itemView.findViewById(R.id.crosswordTextViewWordDefinition);
        }
    }

}