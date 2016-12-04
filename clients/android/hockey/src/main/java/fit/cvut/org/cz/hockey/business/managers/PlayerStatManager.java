package fit.cvut.org.cz.hockey.business.managers;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.hockey.data.DatabaseFactory;
import fit.cvut.org.cz.hockey.business.entities.PlayerStat;
import fit.cvut.org.cz.hockey.business.interfaces.IPlayerStatManager;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ICorePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.managers.BaseManager;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.SportDBHelper;

/**
 * Created by kevin on 2.12.2016.
 */
public class PlayerStatManager extends BaseManager<PlayerStat> implements IPlayerStatManager {
    public PlayerStatManager(ICorePlayerManager corePlayerManager, SportDBHelper sportDBHelper) {
        super(corePlayerManager, sportDBHelper);
    }

    @Override
    protected Dao<PlayerStat, Long> getDao(Context context) {
        return DatabaseFactory.getDBeHelper(context).getHockeyPlayerStatDAO();
    }

    @Override
    public ArrayList<PlayerStat> getByParticipantId(Context context, long participantId) {
        ArrayList<PlayerStat> res = new ArrayList<>();
        try {
            List<PlayerStat> playerStats = getDao(context).queryBuilder()
                    .where()
                    .eq(DBConstants.cPARTICIPANT_ID, participantId)
                    .query();
            Map<Long, Player> playerMap = corePlayerManager.getAllPlayers(context);
            for (PlayerStat playerStat : playerStats) {
                playerStat.setName(playerMap.get(playerStat.getPlayerId()).getName());
            }
            res.addAll(playerStats);
            return res;
        } catch (SQLException e) {
            return res;
        }
    }

    @Override
    public void deleteByParticipantId(Context context, long participantId) {
        try {
            List<PlayerStat> playerStats = getDao(context).queryBuilder()
                    .where()
                    .eq(DBConstants.cPARTICIPANT_ID, participantId)
                    .query();
            for (PlayerStat playerStat : playerStats) {
                delete(context, playerStat.getId());
            }
        } catch (SQLException e) {}
    }
}
