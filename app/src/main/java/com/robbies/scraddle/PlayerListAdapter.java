package com.robbies.scraddle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.PlayerViewHolder> {

    private final ArrayList<Player> mAllPlayers;
    private final Match mCurrentMatch;
    private OnPlayerListener mOnPlayerListener;


    public PlayerListAdapter(ArrayList<Player> allPlayers, Match currentMatch, OnPlayerListener onPlayerListener) {
        mAllPlayers = allPlayers;
        this.mCurrentMatch = currentMatch;
        this.mOnPlayerListener = onPlayerListener;
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Create the new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scorecard, parent, false);
        PlayerViewHolder vh = new PlayerViewHolder(view, mOnPlayerListener);
        return vh;
    }

    //Replace contents of view
    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {

        Player player = mAllPlayers.get(position);
        Score playerScore = mCurrentMatch.getPlayerScore(player.getPlayerID());
        int visibility = player.getPlayerStatus() ? View.VISIBLE : View.GONE;

        //Always show
        holder.nameTextView.setText(player.getName());
        holder.nameTextView.setBackgroundResource(player.getPlayerStatus() ? R.color.activePlayer : R.color.inactivePlayer);
        holder.scoreTextView.setText(playerScore.getTotalScore());

        //Set Text
        holder.lastScoreTextView.setText(playerScore.getLastScore());
        holder.bestScoreTextView.setText(playerScore.getBestScoreSoFar());
        holder.personalBestTextView.setText(Integer.toString(player.getPersonalBest()));
        holder.miniScore.setText(playerScore.getTotalScore());

        //Set Visibility

        holder.lastScoreTextView.setVisibility(visibility);
        holder.bestScoreTextView.setVisibility(visibility);
        holder.personalBestTextView.setVisibility(visibility);
        holder.labelPB.setVisibility(visibility);
        holder.labelBest.setVisibility(visibility);
        holder.labelLast.setVisibility(visibility);
        holder.miniScore.setVisibility(player.getPlayerStatus() ? View.GONE : View.VISIBLE);



    }

    // Size of the dataset
    @Override
    public int getItemCount() {
        return mAllPlayers.size();
    }


    public interface OnPlayerListener {
        void onPlayerClick(int position);

    }

    public static class PlayerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnPlayerListener onPlayerListener;
        private TextView nameTextView;
        private TextView lastScoreTextView;
        private TextView scoreTextView, miniScore;
        private TextView bestScoreTextView;
        private TextView personalBestTextView;
        private TextView labelLast, labelBest, labelPB;


        public PlayerViewHolder(@NonNull View itemView, OnPlayerListener onPlayerListener) {
            super(itemView);


            scoreTextView = itemView.findViewById(R.id.textViewPlayerTotal);
            miniScore = itemView.findViewById(R.id.textViewminiScore);
            nameTextView = itemView.findViewById(R.id.textViewPlayerName);
            lastScoreTextView = itemView.findViewById(R.id.textViewLastScore);
            bestScoreTextView = itemView.findViewById(R.id.textViewBestScore);
            personalBestTextView = itemView.findViewById(R.id.textViewPersonalBest);
            labelLast = itemView.findViewById(R.id.labelLastScore);
            labelBest = itemView.findViewById(R.id.labelBestScore);
            labelPB = itemView.findViewById(R.id.labelHighestScore);

            this.onPlayerListener = onPlayerListener;


            itemView.setOnClickListener((this));


        }

        @Override
        public void onClick(View view) {
            onPlayerListener.onPlayerClick(getAdapterPosition());

        }
    }


}





