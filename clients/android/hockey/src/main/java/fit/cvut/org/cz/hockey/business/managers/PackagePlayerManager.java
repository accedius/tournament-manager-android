package fit.cvut.org.cz.hockey.business.managers;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.data.DAOFactory;
import fit.cvut.org.cz.hockey.data.StatsEnum;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IPackagePlayerManager;
import fit.cvut.org.cz.tmlibrary.data.entities.DStat;

/**
 * Created by atgot_000 on 8. 4. 2016.
 */
public class PackagePlayerManager {//implements IPackagePlayerManager {
/*
    @Override
    public void updatePlayersInParticipant(Context context, long participantId, long competitionId, long tournamentId, List<Player> players) {
        ArrayList<DStat> partStats = DAOFactory.getInstance().statDAO.getStatsByParticipantId(context, participantId);
        ArrayList<Long> playerIds = new ArrayList<>();
        for (Player p : players) playerIds.add(p.getId());

        ArrayList<Long> currentPlayers = DAOFactory.getInstance().packagePlayerDAO.getPlayerIdsByParticipant(context, participantId);

        ArrayList<Long> toDelete = new ArrayList<>(currentPlayers);
        toDelete.removeAll(playerIds);

        ArrayList<Long> toAdd = new ArrayList<>(playerIds);
        toAdd.removeAll(currentPlayers);

        for (Long id : toAdd) {
            createStatistics(context, id, participantId, tournamentId, competitionId);
        }
        for (Long id : toDelete) {
            removeStatistics(context, id, partStats);
        }
    }

    private void createStatistics(Context context, long playerId, long participantId, long tournamentId, long competitionId) {
        for (StatsEnum statEn : StatsEnum.values()) {
            if (!statEn.isForPlayer()) continue;
            DStat toInsert = new DStat(-1, playerId, participantId, statEn.toString(), tournamentId, competitionId, String.valueOf(0));
            DAOFactory.getInstance().statDAO.insert(context, toInsert);
        }
    }

    private void removeStatistics(Context context, long playerId, ArrayList<DStat> partStats) {
        for (DStat stat : partStats) {
            if (stat.getPlayerId() == playerId)
                DAOFactory.getInstance().statDAO.delete(context, stat.getId());
        }
    }*/
}

