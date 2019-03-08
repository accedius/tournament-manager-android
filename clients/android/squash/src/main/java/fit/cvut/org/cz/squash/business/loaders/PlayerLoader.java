package fit.cvut.org.cz.squash.business.loaders;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.squash.business.ManagerFactory;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.Conflict;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.PlayerImportInfo;
import fit.cvut.org.cz.tmlibrary.business.loaders.helpers.ConflictCreator;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICompetitionManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IPackagePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.serialization.PlayerSerializer;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;

/**
 * Created by kevin on 13.12.2016.
 */

public class PlayerLoader {
    public static List<PlayerImportInfo> getPlayersImportInfo(Context context, Resources res, List<ServerCommunicationItem> players, List<Conflict> playersModified) {
        ArrayList<PlayerImportInfo> playersInfo = new ArrayList<>();
        Map<Long, Player> allPlayers = ((IPackagePlayerManager)ManagerFactory.getInstance(context).getEntityManager(Player.class)).getMapAll();
        HashMap<String, Player> allPlayersMap = new HashMap<>();
        for (Player player : allPlayers.values()) {
            allPlayersMap.put(player.getEmail(), player);
        }

                /* Import players */
        for (ServerCommunicationItem player : players) {
            Player importedPlayer = PlayerSerializer.getInstance(context).deserialize(player);
            if (allPlayersMap.containsKey(importedPlayer.getEmail())) {
                Player matchedPlayer = allPlayersMap.get(importedPlayer.getEmail());
                if (!matchedPlayer.samePlayer(importedPlayer)) {
                    playersModified.add(ConflictCreator.createConflict(matchedPlayer, importedPlayer, res));
                }
            } else {
                playersInfo.add(new PlayerImportInfo(importedPlayer.getName(), importedPlayer.getEmail()));
            }
        }
        return playersInfo;
    }

    public static void importPlayers(Context context, List<ServerCommunicationItem> players, Competition competition, Map<String, Player> importedPlayers, Map<String, String> conflictSolutions) {
        Map<Long, Player> allPlayers = ((IPackagePlayerManager)ManagerFactory.getInstance(context).getEntityManager(Player.class)).getMapAll();
        Map<String, Player> allPlayersMap = new HashMap<>();
        for (Player player : allPlayers.values()) {
            allPlayersMap.put(player.getEmail(), player);
        }

        /* Import players
            - add if not exists
            - add to competition
            - fill HashMap<uid, player> */
        for (ServerCommunicationItem player : players) {
            Log.d("IMPORT", "Player: " + player.syncData);
            Player importedPlayer = PlayerSerializer.getInstance(context).deserialize(player);
            long playerId;
            if (allPlayersMap.containsKey(importedPlayer.getEmail())) {
                Player matchedPlayer = allPlayersMap.get(importedPlayer.getEmail());
                playerId = matchedPlayer.getId();
                if (!matchedPlayer.samePlayer(importedPlayer)) {
                    if (conflictSolutions.containsKey(matchedPlayer.getEmail())) {
                        if (conflictSolutions.get(matchedPlayer.getEmail()).equals(Conflict.TAKE_FILE)) {
                            ManagerFactory.getInstance(context).getEntityManager(Player.class).update(importedPlayer);
                            Log.d("IMPORT", "\tCONFLICT!");
                            Log.d("IMPORT", "Player " + matchedPlayer.getEmail() + " will be replaced by file!");
                        }
                    }
                }
            } else {
                ManagerFactory.getInstance(context).getEntityManager(Player.class).insert(importedPlayer);
                playerId = importedPlayer.getId();
                Log.d("IMPORT", "\tADDED " + playerId);
            }
            importedPlayer.setId(playerId);
            importedPlayers.put(importedPlayer.getUid(), importedPlayer);

            // Add player to competition.
            ((ICompetitionManager)ManagerFactory.getInstance(context).getEntityManager(Competition.class)).addPlayer(competition, importedPlayer);
        }

        Log.d("IMPORTED PLAYERS", importedPlayers.toString());
    }
}
