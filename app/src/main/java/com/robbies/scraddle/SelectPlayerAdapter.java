package com.robbies.scraddle;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.robbies.scraddle.Data.Player;

import java.util.List;

public class SelectPlayerAdapter extends RecyclerView.Adapter<SelectPlayerAdapter.PlayerViewHolder> {

    private final OnStartDragListener mDragStartListener;
    private OnPlayerListener mOnPlayerListener;
    private List<Player> mAllPlayers;


    SelectPlayerAdapter(List<Player> allPlayers, OnPlayerListener onPlayerListener, OnStartDragListener dragStartListener) {

        mAllPlayers = allPlayers;
        this.mOnPlayerListener = onPlayerListener;
        mDragStartListener = dragStartListener;
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Create the new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_select_player_item, parent, false);
        return new PlayerViewHolder(view, mOnPlayerListener);
    }

    //Replace contents of view
    @Override
    public void onBindViewHolder(@NonNull final PlayerViewHolder holder, int position) {

        Player player = mAllPlayers.get(position);

        //Set each item
        holder.playerNameTextView.setText(player.getName());

        holder.handleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.performClick();
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    void setPlayers(List<Player> players) {
        mAllPlayers = players;
        notifyDataSetChanged();
    }


    // Size of the dataset
    @Override
    public int getItemCount() {
        return mAllPlayers != null ? mAllPlayers.size() : 0;
    }

    public interface OnPlayerListener {
        void onPlayerClick(int position);
    }

    public interface OnStartDragListener {
        void onStartDrag(RecyclerView.ViewHolder viewHolder);
    }

    public static class PlayerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView handleView;
        OnPlayerListener onPlayerListener;
        TextView playerNameTextView;


        PlayerViewHolder(@NonNull View itemView, OnPlayerListener onPlayerListener) {
            super(itemView);

            playerNameTextView = itemView.findViewById(R.id.textViewPlayerNameItem);

            this.onPlayerListener = onPlayerListener;

            itemView.setOnClickListener((this));
            handleView = itemView.findViewById(R.id.handle);
        }

        @Override
        public void onClick(View view) {
            onPlayerListener.onPlayerClick(getAdapterPosition());
        }
    }
}




