package com.robbies.scraddle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.ActivePlayerViewHolder> {

    private ArrayList<GameDetail> playerDetails;
    private OnPlayerListener mOnPlayerListener;

    PlayerListAdapter(ArrayList<GameDetail> playerDetails, OnPlayerListener onPlayerListener) {
        this.playerDetails = playerDetails;
        this.mOnPlayerListener = onPlayerListener;
    }

    @NonNull
    @Override
    public ActivePlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View 0 is the first active player, every other view is default to miniscorecard
        View view;

        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scorecard, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scorecardmini, parent, false);
        }

        return new ActivePlayerViewHolder(view, mOnPlayerListener);
    }

    //Replace contents of view
    @Override
    public void onBindViewHolder(@NonNull ActivePlayerViewHolder holder, int position) {

        GameDetail player = playerDetails.get(position);


        //Always show
        holder.nameTextView.setText(player.getName());

        //Show when tapped
        String scoreString = player.getScore().isEmpty() ? "" : player.getScore().substring(2);
        holder.allScores.setText(scoreString);


        if (position == 0) {
            holder.scoreTextView.setText(String.format("%s", player.getTotalScore()));
            holder.lastScoreTextView.setText(player.getLastScore());
            holder.bestScoreTextView.setText(String.format("%s", player.getMaxScore()));
            holder.personalBestTextView.setText(String.format("%s", player.getPersonalBest()));
        } else {
            //Inactive Player
            holder.miniScore.setText(String.format("%s", player.getTotalScore()));
        }
    }

    void setAllPlayers(ArrayList<GameDetail> allPlayerDetails) {
        this.playerDetails = allPlayerDetails;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    // Size of the dataset
    @Override
    public int getItemCount() {
        return playerDetails != null ? playerDetails.size() : 0;
    }


    public interface OnPlayerListener {
        void onPlayerClick(View view);
    }

    public static class ActivePlayerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnPlayerListener onPlayerListener;
        private TextView nameTextView;
        private TextView allScores;
        private TextView scoreTextView, miniScore;
        private TextView lastScoreTextView, bestScoreTextView, personalBestTextView;

        ActivePlayerViewHolder(@NonNull View itemView, OnPlayerListener onPlayerListener) {
            super(itemView);

            scoreTextView = itemView.findViewById(R.id.textViewPlayerTotal);
            miniScore = itemView.findViewById(R.id.textViewminiScore);
            nameTextView = itemView.findViewById(R.id.textViewPlayerName);
            lastScoreTextView = itemView.findViewById(R.id.textViewLastScore);
            bestScoreTextView = itemView.findViewById(R.id.textViewBestScore);
            personalBestTextView = itemView.findViewById(R.id.textViewPersonalBest);
            allScores = itemView.findViewById(R.id.tVScoreTally);

            this.onPlayerListener = onPlayerListener;

            itemView.setOnClickListener((this));
        }

        @Override
        public void onClick(View view) {
            onPlayerListener.onPlayerClick(view);
        }
    }

}





