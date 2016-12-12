package fit.cvut.org.cz.squash.business.managers;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.squash.data.SquashDBHelper;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.PlayerStat;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ICorePlayerManager;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.SportDBHelper;

/**
 * Created by kevin on 9.12.2016.
 */

public class PlayerStatManager extends fit.cvut.org.cz.tmlibrary.business.managers.PlayerStatManager {
    protected SquashDBHelper sportDBHelper;

    public PlayerStatManager(ICorePlayerManager corePlayerManager, SquashDBHelper sportDBHelper) {
        super(corePlayerManager, sportDBHelper);
        this.sportDBHelper = sportDBHelper;
    }

    @Override
    protected Dao<PlayerStat, Long> getDao(Context context) {
        return sportDBHelper.getPlayerStatDAO();
    }

    @Override
    public List<PlayerStat> getByPlayerId(Context context, long playerId) {
        try {
            List<PlayerStat> playerStats = getDao(context).queryForEq(DBConstants.cPLAYER_ID, playerId);
            Map<Long, Player> playerMap = corePlayerManager.getAllPlayers(context);
            for (PlayerStat playerStat : playerStats) {
                playerStat.setName(playerMap.get(playerStat.getPlayerId()).getName());
            }
            return playerStats;
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<PlayerStat> getByParticipantId(Context context, long participantId) {
        try {
            List<PlayerStat> playerStats = getDao(context).queryForEq(DBConstants.cPARTICIPANT_ID, participantId);
            Map<Long, Player> playerMap = corePlayerManager.getAllPlayers(context);
            for (PlayerStat playerStat : playerStats) {
                playerStat.setName(playerMap.get(playerStat.getPlayerId()).getName());
            }
            return playerStats;
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public void deleteByParticipantId(Context context, long participantId) {
        try {
            DeleteBuilder<PlayerStat, Long> deleteBuilder = getDao(context).deleteBuilder();
            deleteBuilder.where().eq(DBConstants.cPARTICIPANT_ID, participantId);
            deleteBuilder.delete();
        } catch (SQLException e) {}
    }
}
