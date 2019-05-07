package fit.cvut.org.cz.bowling.business.managers;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.bowling.business.managers.interfaces.IPlayerStatManager;
import fit.cvut.org.cz.bowling.data.entities.PlayerStat;
import fit.cvut.org.cz.tmlibrary.business.managers.BaseManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IPackagePlayerManager;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants;

public class PlayerStatManager extends BaseManager<PlayerStat> implements IPlayerStatManager {
    @Override
    public List<PlayerStat> getByPlayerId(long playerId) {
        List<PlayerStat> playerStats = managerFactory.getDaoFactory().getMyDao(PlayerStat.class).getListItemById(DBConstants.cPLAYER_ID, playerId);
        Map<Long, Player> playerMap = ((IPackagePlayerManager)managerFactory.getEntityManager(Player.class)).getMapAll();
        for (PlayerStat playerStat : playerStats) {
            playerStat.setName(playerMap.get(playerStat.getPlayerId()).getName());
        }
        return playerStats;
    }

    @Override
    public List<PlayerStat> getByParticipantId(long participantId) {
        List<PlayerStat> playerStats = managerFactory.getDaoFactory().getMyDao(PlayerStat.class).getListItemById(DBConstants.cPARTICIPANT_ID, participantId);
        Map<Long, Player> playerMap = ((IPackagePlayerManager)managerFactory.getEntityManager(Player.class)).getMapAll();
        for (PlayerStat playerStat : playerStats) {
            playerStat.setName(playerMap.get(playerStat.getPlayerId()).getName());
        }
        return playerStats;
    }

    @Override
    public void deleteByParticipantId(long participantId) {
        try {
            managerFactory.getDaoFactory().getMyDao(PlayerStat.class).deleteItemById(DBConstants.cPARTICIPANT_ID, participantId);
        } catch (SQLException e) {}
    }

    @Override
    protected Class<PlayerStat> getMyClass() {
        return PlayerStat.class;
    }
}
