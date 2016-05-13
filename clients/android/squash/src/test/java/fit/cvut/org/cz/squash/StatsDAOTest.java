package fit.cvut.org.cz.squash;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import fit.cvut.org.cz.squash.data.DAOFactory;
import fit.cvut.org.cz.squash.data.DatabaseFactory;
import fit.cvut.org.cz.squash.data.SDBConstants;
import fit.cvut.org.cz.squash.data.SquashDBHelper;
import fit.cvut.org.cz.squash.data.entities.DStat;
import fit.cvut.org.cz.squash.data.entities.StatsEnum;

/**
 * Created by Vaclav on 13. 5. 2016.
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class StatsDAOTest {

    @Before
    public void setUp() throws Exception {
        DatabaseFactory.getInstance().setTest(true);

    }

    @After
    public void tearDown() throws Exception {
        DatabaseFactory.getInstance().setTest(false);
        DatabaseFactory.getInstance().getDatabase(RuntimeEnvironment.application).delete(SDBConstants.tSTATS, null, null);
    }
    private boolean compareStat(DStat stat, long id, long competitionId, long tournamentId, long playerId, long participantId,
                                int status, int value, int lostValue, StatsEnum type){
        return !(stat.getCompetitionId() != competitionId || stat.getTournamentId() != tournamentId || stat.getPlayerId() != playerId
                || stat.getParticipantId() != participantId || stat.getStatus() != status || stat.getValue() != value
                || stat.getLostValue() != lostValue || stat.getType() != type);
    }

    @Test
    public void testInsertUpdateDelete() throws Exception {
        DAOFactory.getInstance().statDAO.insert(RuntimeEnvironment.application, new DStat(0, 0, 0, 0, 0, 1, -1, -1, StatsEnum.MATCH));
        ArrayList<DStat> stats = DAOFactory.getInstance().statDAO.getAll(RuntimeEnvironment.application, StatsEnum.MATCH);
        Assert.assertEquals(1, stats.size());
        Assert.assertTrue(compareStat(stats.get(0), 0, 0, 0, 0, 0, 1, -1, -1, StatsEnum.MATCH));

        DStat statToUpdate = stats.get(0);
        statToUpdate.setCompetitionId(1);
        statToUpdate.setTournamentId(1);
        statToUpdate.setPlayerId(-1);
        statToUpdate.setParticipantId(1);
        statToUpdate.setStatus(0);
        statToUpdate.setLostValue(3);
        statToUpdate.setValue(15);
        statToUpdate.setType(StatsEnum.SET);

        DAOFactory.getInstance().statDAO.update(RuntimeEnvironment.application, statToUpdate);
        stats = DAOFactory.getInstance().statDAO.getAll(RuntimeEnvironment.application, StatsEnum.SET);
        Assert.assertEquals(1, stats.size());
        Assert.assertTrue(compareStat(stats.get(0), 0, 1, 1, -1, 1, 0, 15, 3, StatsEnum.SET));

        DAOFactory.getInstance().statDAO.insert(RuntimeEnvironment.application, new DStat(0, 0, 0, 0, 1, 1, -1, -1, StatsEnum.MATCH));
        DAOFactory.getInstance().statDAO.insert(RuntimeEnvironment.application, new DStat(0, 0, 0, 0, 1, 1, -1, -1, StatsEnum.MATCH_PARTICIPATION));

        Assert.assertEquals(1, DAOFactory.getInstance().statDAO.getAll(RuntimeEnvironment.application, StatsEnum.SET).size());
        Assert.assertEquals(1, DAOFactory.getInstance().statDAO.getAll(RuntimeEnvironment.application, StatsEnum.MATCH).size());
        Assert.assertEquals(1, DAOFactory.getInstance().statDAO.getAll(RuntimeEnvironment.application, StatsEnum.MATCH_PARTICIPATION).size());

        DAOFactory.getInstance().statDAO.delete(RuntimeEnvironment.application, 1, StatsEnum.SET);
        Assert.assertEquals(0, DAOFactory.getInstance().statDAO.getAll(RuntimeEnvironment.application, StatsEnum.SET).size());
        Assert.assertEquals(1, DAOFactory.getInstance().statDAO.getAll(RuntimeEnvironment.application, StatsEnum.MATCH_PARTICIPATION).size());
        Assert.assertEquals(1, DAOFactory.getInstance().statDAO.getAll(RuntimeEnvironment.application, StatsEnum.MATCH).size());

        DAOFactory.getInstance().statDAO.deleteByParticipant(RuntimeEnvironment.application, 1);
        Assert.assertEquals(0, DAOFactory.getInstance().statDAO.getAll(RuntimeEnvironment.application, StatsEnum.MATCH_PARTICIPATION).size());
        Assert.assertEquals(0, DAOFactory.getInstance().statDAO.getAll(RuntimeEnvironment.application, StatsEnum.MATCH).size());

    }


    private void prepQueryData(){
        DAOFactory.getInstance().statDAO.insert(RuntimeEnvironment.application, new DStat(0, 1, 1, 1, 1, 1, 1, 1, StatsEnum.MATCH_PARTICIPATION));
        DAOFactory.getInstance().statDAO.insert(RuntimeEnvironment.application, new DStat(0, 2, 1, 2, 1, 1, 1, 1, StatsEnum.MATCH_PARTICIPATION));
        DAOFactory.getInstance().statDAO.insert(RuntimeEnvironment.application, new DStat(0, 1, 2, 3, 2, 1, 1, 1, StatsEnum.MATCH_PARTICIPATION));

        DAOFactory.getInstance().statDAO.insert(RuntimeEnvironment.application, new DStat(0, 1, 1, -1, 1, 1, 4, 15, StatsEnum.SET));
        DAOFactory.getInstance().statDAO.insert(RuntimeEnvironment.application, new DStat(0, 1, 2, -1, 5, 1, 4, 15, StatsEnum.SET));
        DAOFactory.getInstance().statDAO.insert(RuntimeEnvironment.application, new DStat(0, 1, 2, -1, 6, 1, 4, 15, StatsEnum.MATCH));
        DAOFactory.getInstance().statDAO.insert(RuntimeEnvironment.application, new DStat(0, 2, 3, -1, 7, 1, 4, 15, StatsEnum.SET));
        DAOFactory.getInstance().statDAO.insert(RuntimeEnvironment.application, new DStat(0, 2, 3, -1, 8, 1, 4, 15, StatsEnum.MATCH));

    }

    @Test
    public void testQueries() throws Exception {

        prepQueryData();
        Assert.assertEquals(2, DAOFactory.getInstance().statDAO.getPlayerIdsForParticipant(RuntimeEnvironment.application, 1).size());
        Assert.assertEquals(1, DAOFactory.getInstance().statDAO.getByParticipant(RuntimeEnvironment.application, 1, StatsEnum.SET).size());
        Assert.assertEquals(1, DAOFactory.getInstance().statDAO.getByTournament(RuntimeEnvironment.application, 2, StatsEnum.SET).size());
        Assert.assertEquals(1, DAOFactory.getInstance().statDAO.getByCompetition(RuntimeEnvironment.application, 2, StatsEnum.SET).size());
        Assert.assertEquals(3, DAOFactory.getInstance().statDAO.getAll(RuntimeEnvironment.application, StatsEnum.SET).size());
        Assert.assertEquals(1, DAOFactory.getInstance().statDAO.getByPlayerAndCompetition(RuntimeEnvironment.application, 1, 1, StatsEnum.MATCH_PARTICIPATION).size());
//        int count =  DAOFactory.getInstance().statDAO.getByPlayerAndTournament(RuntimeEnvironment.application, 3, 2, StatsEnum.MATCH_PARTICIPATION).size();
        Assert.assertEquals(1, DAOFactory.getInstance().statDAO.getByPlayerAndTournament(RuntimeEnvironment.application, 3, 2, StatsEnum.MATCH_PARTICIPATION).size());
        ;
    }
}
