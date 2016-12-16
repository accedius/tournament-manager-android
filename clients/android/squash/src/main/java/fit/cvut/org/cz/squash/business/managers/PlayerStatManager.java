package fit.cvut.org.cz.squash.business.managers;

import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICorePlayerManager;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.PlayerStat;

/**
 * Created by kevin on 9.12.2016.
 */

public class PlayerStatManager extends fit.cvut.org.cz.tmlibrary.business.managers.PlayerStatManager {
    @Override
    public List<PlayerStat> getByPlayerId(long playerId) {
        try {
            List<PlayerStat> playerStats = managerFactory.getDaoFactory().getMyDao(PlayerStat.class).queryForEq(DBConstants.cPLAYER_ID, playerId);
            Map<Long, Player> playerMap = ((ICorePlayerManager)managerFactory.getEntityManager(Player.class)).getMapAll();
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
            List<PlayerStat> playerStats = managerFactory.getDaoFactory().getMyDao(PlayerStat.class).queryForEq(DBConstants.cPARTICIPANT_ID, participantId);
            Map<Long, Player> playerMap = ((ICorePlayerManager)managerFactory.getEntityManager(Player.class)).getMapAll();
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
            DeleteBuilder<PlayerStat, Long> deleteBuilder = managerFactory.getDaoFactory().getMyDao(PlayerStat.class).deleteBuilder();
            deleteBuilder.where().eq(DBConstants.cPARTICIPANT_ID, participantId);
            deleteBuilder.delete();
        } catch (SQLException e) {}
    }
}
