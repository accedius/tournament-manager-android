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

/**
 * Created by kevin on 9.12.2016.
 */

public class PlayerStatManager extends fit.cvut.org.cz.tmlibrary.business.managers.PlayerStatManager {
    protected SquashDBHelper sportDBHelper;

    public PlayerStatManager(Context context, ICorePlayerManager corePlayerManager, SquashDBHelper sportDBHelper) {
        super(context, corePlayerManager, sportDBHelper);
        this.sportDBHelper = sportDBHelper;
    }

    @Override
    protected Dao<PlayerStat, Long> getDao() {
        return sportDBHelper.getPlayerStatDAO();
    }

    @Override
    public List<PlayerStat> getByPlayerId(long playerId) {
        try {
            List<PlayerStat> playerStats = getDao().queryForEq(DBConstants.cPLAYER_ID, playerId);
            Map<Long, Player> playerMap = corePlayerManager.getAllPlayers();
            for (PlayerStat playerStat : playerStats) {
                playerStat.setName(playerMap.get(playerStat.getPlayerId()).getName());
            }
            return playerStats;
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<PlayerStat> getByParticipantId(long participantId) {
        try {
            List<PlayerStat> playerStats = getDao().queryForEq(DBConstants.cPARTICIPANT_ID, participantId);
            Map<Long, Player> playerMap = corePlayerManager.getAllPlayers();
            for (PlayerStat playerStat : playerStats) {
                playerStat.setName(playerMap.get(playerStat.getPlayerId()).getName());
            }
            return playerStats;
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public void deleteByParticipantId(long participantId) {
        try {
            DeleteBuilder<PlayerStat, Long> deleteBuilder = getDao().deleteBuilder();
            deleteBuilder.where().eq(DBConstants.cPARTICIPANT_ID, participantId);
            deleteBuilder.delete();
        } catch (SQLException e) {}
    }
}
