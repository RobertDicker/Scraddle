package com.robbies.scraddle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.PlayerViewHolder> {

    private final List<Player> mAllPlayers;
    private OnPlayerListener mOnPlayerListener;


    public PlayerListAdapter(List<Player> allPlayers, OnPlayerListener onPlayerListener) {
        mAllPlayers = allPlayers;
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

        //Set each item
        holder.nameTextView.setText(player.getName());
        holder.scoreTextView.setText(Integer.toString(player.getTotal()));
        holder.lastScoreTextView.setText(player.getLastScore());
        holder.bestScoreTextView.setText(player.getHighScore());
        holder.personalBestTextView.setText(player.getPersonalBest());


        // Change the display to active player
        holder.nameTextView.setBackgroundResource(player.getPlayerStatus() ? R.color.activePlayer : R.color.inactivePlayer);


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
        private LinearLayout linearLayout;
        private TextView scoreTextView;
        private TextView bestScoreTextView;
        private TextView personalBestTextView;

        public PlayerViewHolder(@NonNull View itemView, OnPlayerListener onPlayerListener) {
            super(itemView);


            scoreTextView = itemView.findViewById(R.id.textViewPlayerTotal);
            nameTextView = itemView.findViewById(R.id.textViewPlayerName);
            lastScoreTextView = itemView.findViewById(R.id.textViewLastScore);
            bestScoreTextView = itemView.findViewById(R.id.textViewBestScore);
            personalBestTextView = itemView.findViewById(R.id.textViewPersonalBest);
            linearLayout = itemView.findViewById(R.id.linearLayoutPlayer);


            this.onPlayerListener = onPlayerListener;


            itemView.setOnClickListener((this));


        }

        @Override
        public void onClick(View view) {
            onPlayerListener.onPlayerClick(getAdapterPosition());

        }
    }


}





