package com.robbies.scraddle;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.robbies.scraddle.Data.PlayersAndRecords;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.DetailViewHolder> {

    private List<PlayersAndRecords> mAllPlayers;

    LeaderBoardAdapter() {
        this.mAllPlayers = new ArrayList<>();
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Create the new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_leaderboard_item, parent, false);
        return new DetailViewHolder(view);
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

        holder.playerNameTextView.setBackgroundColor(Color.parseColor(playerNameBgColour));

        PlayersAndRecords player = mAllPlayers.get(position);

        //Set each item
        holder.playerNameTextView.setText(player.getName());

        holder.playerRankTextView.setText(String.format("%s", position + 1));
        holder.playerGamesPlayedTextView.setText(String.format("%s", player.getDraw() + player.getLoss() + player.getWins()));
        holder.playerWinsTextView.setText(String.format("%s", player.getWins()));
        holder.playerDrawsTextView.setText(String.format("%s", player.getDraw()));
        holder.playerLossTextView.setText(String.format("%s", player.getLoss()));
        holder.playerMatchHighScoreTextView.setText(String.format("%s", player.getPlayersHighestMatchScore()));
        holder.playerWordHighScoreTextView.setText(String.format("%s", player.getPersonalBest()));
    }

    void setPlayers(List<PlayersAndRecords> players) {
        mAllPlayers = players;
        Collections.sort(mAllPlayers, Collections.<PlayersAndRecords>reverseOrder());
        notifyDataSetChanged();
    }


    // Size of the dataset
    @Override
    public int getItemCount() {
        return mAllPlayers != null ? mAllPlayers.size() : 0;
    }


    static class DetailViewHolder extends RecyclerView.ViewHolder {

        //  OnPlayerListener onPlayerListener;
        TextView playerNameTextView;
        TextView playerRankTextView;
        TextView playerGamesPlayedTextView;
        TextView playerWinsTextView;
        TextView playerDrawsTextView;
        TextView playerLossTextView;
        TextView playerMatchHighScoreTextView;
        TextView playerWordHighScoreTextView;


        DetailViewHolder(@NonNull View itemView) {
            super(itemView);
            playerNameTextView = itemView.findViewById(R.id.tVLBPlayerName);
            playerRankTextView = itemView.findViewById(R.id.tVLBPosition);
            playerGamesPlayedTextView = itemView.findViewById(R.id.tVLBGamesPlayed);
            playerWinsTextView = itemView.findViewById(R.id.tVLBWins);
            playerDrawsTextView = itemView.findViewById(R.id.tVLBDraws);
            playerLossTextView = itemView.findViewById(R.id.tVLBLoss);
            playerMatchHighScoreTextView = itemView.findViewById(R.id.tVLBMatchHighScore);
            playerWordHighScoreTextView = itemView.findViewById(R.id.tvLBHighestWordScore);
        }
    }
}



