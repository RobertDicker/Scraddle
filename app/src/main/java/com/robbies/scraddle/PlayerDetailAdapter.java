package com.robbies.scraddle;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerDetailAdapter extends RecyclerView.Adapter<PlayerDetailAdapter.DetailViewHolder> {

    private List<Player> mAllPlayers;
    private OnPlayerListener mOnPlayerListener;

    public PlayerDetailAdapter(OnPlayerListener playerListener) {
        this.mAllPlayers = new ArrayList<>();
        this.mOnPlayerListener = playerListener;
    }


    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Create the new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_leaderboard_item, parent, false);
        DetailViewHolder vh = new DetailViewHolder(view, mOnPlayerListener);
        return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {


        //Determine the colour for positions 1,2,3 and striation for odds even thereafter
        String playerNameBgColour = "#00BCD4";
        //TODO remove this to a seperate class later
        switch (position) {
            case (0):
                playerNameBgColour = "#9C27B0";
                break;
            case (1):
                playerNameBgColour = "#673AB7";
                break;
            case (2):
                playerNameBgColour = "#3F51B5";
                break;
            default:
                if (position % 2 != 0) {
                    playerNameBgColour = "#009688";
                }
        }
        Log.d("====Color==>", playerNameBgColour + "");

        holder.playerNameTextView.setBackgroundColor(Color.parseColor(playerNameBgColour));

        Player player = mAllPlayers.get(position);

        //Set each item
        holder.playerNameTextView.setText(player.getName());

        int gamesPlayed = player.getDraw() + player.getLoss() + player.getWins();

        holder.playerRankTextView.setText(Integer.toString(position + 1));
        holder.playerGamesPlayedTextView.setText(Integer.toString(gamesPlayed));
        holder.playerWinsTextView.setText(Integer.toString(player.getWins()));
        holder.playerDrawsTextView.setText(Integer.toString(player.getDraw()));
        holder.playerLossTextView.setText(Integer.toString(player.getLoss()));
        holder.playerMatchHighScoreTextView.setText(Integer.toString(player.getPlayersHighestMatchScore()));
        holder.playerWordHighScoreTextView.setText(Integer.toString(player.getPersonalBest()));
    }

    void setPlayers(List<Player> players) {
        mAllPlayers = players;
        Collections.sort(mAllPlayers, Collections.<Player>reverseOrder());
        notifyDataSetChanged();
    }


    // Size of the dataset
    @Override
    public int getItemCount() {
        int size = mAllPlayers != null ? mAllPlayers.size() : 0;

        return size;
    }


    public interface OnPlayerListener {
        void onPlayerClick(int position);

    }

    public static class DetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnPlayerListener onPlayerListener;
        TextView playerNameTextView;
        TextView playerRankTextView;
        TextView playerGamesPlayedTextView;
        TextView playerWinsTextView;
        TextView playerDrawsTextView;
        TextView playerLossTextView;
        TextView playerMatchHighScoreTextView;
        TextView playerWordHighScoreTextView;


        public DetailViewHolder(@NonNull View itemView, OnPlayerListener onPlayerListener) {
            super(itemView);

            playerNameTextView = itemView.findViewById(R.id.tVLBPlayerName);
            playerRankTextView = itemView.findViewById(R.id.tVLBPosition);
            playerGamesPlayedTextView = itemView.findViewById(R.id.tVLBGamesPlayed);
            playerWinsTextView = itemView.findViewById(R.id.tVLBWins);
            playerDrawsTextView = itemView.findViewById(R.id.tVLBDraws);
            playerLossTextView = itemView.findViewById(R.id.tVLBLoss);
            playerMatchHighScoreTextView = itemView.findViewById(R.id.tVLBMatchHighScore);
            playerWordHighScoreTextView = itemView.findViewById(R.id.tvLBHighestWordScore);

            this.onPlayerListener = onPlayerListener;

            itemView.setOnClickListener((this));

        }

        @Override
        public void onClick(View view) {
            onPlayerListener.onPlayerClick(getAdapterPosition());

        }
    }


}



