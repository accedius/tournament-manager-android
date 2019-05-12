package fit.cvut.org.cz.bowling.business.managers;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.bowling.business.managers.interfaces.IPlayerStatManager;
import fit.cvut.org.cz.bowling.data.entities.PlayerStat;
import fit.cvut.org.cz.tmlibrary.business.managers.BaseManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IPackagePlayerManager;
import fit.cvut.org.cz.tmlibrary.data.entities.EntityDAO;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntityDAO;

public class PlayerStatManager extends BaseManager<PlayerStat> implements IPlayerStatManager {
    @Override
    public List<PlayerStat> getByPlayerId(long playerId) {
        IEntityDAO<PlayerStat, Long> playerStatDAO = managerFactory.getDaoFactory().getMyDao(PlayerStat.class);
        List<PlayerStat> playerStats = playerStatDAO.getListItemById(DBConstants.cPLAYER_ID, playerId);
        final IPackagePlayerManager packagePlayerManager = managerFactory.getEntityManager(Player.class);
        Map<Long, Player> playerMap = packagePlayerManager.getMapAll();
        for (PlayerStat playerStat : playerStats) {
            playerStat.setName(playerMap.get(playerStat.getPlayerId()).getName());
        }
        return playerStats;
    }

    @Override
    public List<PlayerStat> getByParticipantId(long participantId) {
        IEntityDAO<PlayerStat, Long> playerStatDAO = managerFactory.getDaoFactory().getMyDao(PlayerStat.class);
        List<PlayerStat> playerStats = playerStatDAO.getListItemById(DBConstants.cPARTICIPANT_ID, participantId);
        final IPackagePlayerManager packagePlayerManager = managerFactory.getEntityManager(Player.class);
        Map<Long, Player> playerMap = packagePlayerManager.getMapAll();
        for (PlayerStat playerStat : playerStats) {
            playerStat.setName(playerMap.get(playerStat.getPlayerId()).getName());
        }
        return playerStats;
    }

    @Override
    public void deleteByParticipantId(long participantId) {
        try {
            IEntityDAO<PlayerStat, Long> playerStatDAO = managerFactory.getDaoFactory().getMyDao(PlayerStat.class);
            playerStatDAO.deleteItemById(DBConstants.cPARTICIPANT_ID, participantId);
        } catch (SQLException e) {}
    }

    @Override
    protected Class<PlayerStat> getMyClass() {
        return PlayerStat.class;
    }
}
