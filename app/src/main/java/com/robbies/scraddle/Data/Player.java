package com.robbies.scraddle.Data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;

@Entity(tableName = "player_table")
public class Player implements Parcelable {

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };
    @NonNull
    @ColumnInfo(name = "name", defaultValue = "Unknown Player")
    private String name;
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "playerId")
    private long playerId;
    @NonNull
    @ColumnInfo(name = "lastPlayed")
    private String lastPlayed;

    public Player(@NonNull String name) {
        lastPlayed = Calendar.getInstance().getTime().toString();
        this.name = name;
    }

    protected Player(Parcel in) {
        this.name = in.readString();
        this.playerId = in.readInt();
        this.lastPlayed = in.readString();
    }

    public Player(GameDetail gameDetail) {
        this.name = gameDetail.getName();
        this.playerId = gameDetail.getPlayerId();
        lastPlayed = Calendar.getInstance().getTime().toString();
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public long getPlayerId() {
        return playerId;
    }

    void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    @NonNull
    String getLastPlayed() {
        return lastPlayed;
    }

    void setLastPlayed(@NonNull String lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    @NonNull
    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", playerId=" + playerId +

                '}';
    }

    @Override
    public boolean equals(@Nullable Object obj) {

        boolean result = false;
        if (obj instanceof Player) {
            if (this.getPlayerId() == ((Player) obj).getPlayerId()) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeLong(this.playerId);
        parcel.writeString(this.lastPlayed);
    }
}