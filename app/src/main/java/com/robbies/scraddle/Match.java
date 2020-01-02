package com.robbies.scraddle;

import java.util.Map;

class Match {


    private Map<Player, Score> mPlayersAndScores;


    //Precondition: must have at least one player
    //Postcondition a Player and a Score set is created
    public Match(Player player1, Player... players) {
        mPlayersAndScores.put(player1, new Score());


        if (players.length > 0) {
            for (Player player : players) {
                mPlayersAndScores.put(player, new Score());

            }
        }

    }
}




