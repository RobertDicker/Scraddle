package com.robbies.scraddle;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.TreeMap;

public class GameData {

    private static GameData gameDataHolder = null;
    private TreeMap<String, Player> mAllPlayers;
    private ArrayList<Match> mAllMatches;
    private final String LOG = getClass().getSimpleName();


    private GameData(Context context) {


        mAllPlayers = new TreeMap<>();
        mAllMatches = new ArrayList<>();

        readObjectsFromFile(context);

    }

    public static GameData getInstance(Context context) {
        if (gameDataHolder == null) {
            gameDataHolder = new GameData(context);
        }
        return gameDataHolder;
    }


    public TreeMap<String, Player> getAllPlayers() {
        return mAllPlayers;
    }


    public ArrayList<Match> getAllMatches() {
        return mAllMatches;
    }

    public void addMatchtoHistory(Match match) {
        mAllMatches.add(match);
    }

    public void updatePlayer(Player player) {
        mAllPlayers.put(player.getPlayerID(), player);
    }

    public void persistPlayerData(Context context) {

        writeObjectToFile("allPlayers", context, mAllPlayers);

    }

    public void persistMatchData(Context context) {

        writeObjectToFile("allMatches", context, mAllMatches);
    }


    private void readObjectsFromFile(Context context) {

        FileInputStream fis;

        try {
            fis = context.openFileInput("allPlayers");
            ObjectInputStream ois = new ObjectInputStream(fis);

            this.mAllPlayers = (TreeMap) ois.readObject();
            Log.d(LOG, "Attempted to read mAllPlayers " + mAllPlayers.toString());
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            fis = context.openFileInput("allMatches");
            ObjectInputStream ois = new ObjectInputStream(fis);
            mAllMatches = (ArrayList<Match>) ois.readObject();
            Log.d(LOG, "Attempted to read mAllMatches " + mAllMatches.toString());
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void writeObjectToFile(String fileName, Context context, Object object) {

        FileOutputStream fos;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);

            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            Log.d(LOG, "Attempted to write object " + object.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}





