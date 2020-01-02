package com.robbies.scraddle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SelectPlayerAdapter extends RecyclerView.Adapter<SelectPlayerAdapter.PlayerViewHolder> {

    private final List<Player> mAllPlayers;
    private OnPlayerListener mOnPlayerListener;


    public SelectPlayerAdapter(List<Player> allPlayers, OnPlayerListener onPlayerListener) {
        mAllPlayers = allPlayers;
        this.mOnPlayerListener = onPlayerListener;
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Create the new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_select_player_item, parent, false);
        PlayerViewHolder vh = new PlayerViewHolder(view, mOnPlayerListener);
        return vh;
    }

    //Replace contents of view
    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {

        Player player = mAllPlayers.get(position);

        //Set each item
        holder.playerNameTextView.setText(player.getName());

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
        TextView playerNameTextView;


        public PlayerViewHolder(@NonNull View itemView, OnPlayerListener onPlayerListener) {
            super(itemView);

            playerNameTextView = itemView.findViewById(R.id.textViewPlayerNameItem);

            this.onPlayerListener = onPlayerListener;

            itemView.setOnClickListener((this));

        }

        @Override
        public void onClick(View view) {
            onPlayerListener.onPlayerClick(getAdapterPosition());

        }
    }


}




