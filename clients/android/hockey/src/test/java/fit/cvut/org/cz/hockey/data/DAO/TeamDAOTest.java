package fit.cvut.org.cz.hockey.data.DAO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import fit.cvut.org.cz.hockey.BuildConfig;
import fit.cvut.org.cz.hockey.data.DAOFactory;
import fit.cvut.org.cz.tmlibrary.data.entities.DTeam;
import fit.cvut.org.cz.tmlibrary.data.entities.DTournament;

import static org.junit.Assert.*;

/**
 * Created by atgot_000 on 7. 5. 2016.
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class TeamDAOTest {



    @Test
    public void testInsertUpdateDelete() throws Exception {
        DTeam team1 = new DTeam(1, 1, "TEAMA");
        DTeam team2 = new DTeam(2, 1, "TEAMB");

        DAOFactory.getInstance().teamDAO.insert(RuntimeEnvironment.application, team1);
        DAOFactory.getInstance().teamDAO.insert(RuntimeEnvironment.application, team2);

        ArrayList<DTeam> ret = DAOFactory.getInstance().teamDAO.getByTournamentId(RuntimeEnvironment.application, 1);

        assertTrue(ret.size() == 2);

        DTeam changeTeam = ret.get(0);
        changeTeam.setName("ABCD");

        DAOFactory.getInstance().teamDAO.update(RuntimeEnvironment.application, changeTeam);

        ret = DAOFactory.getInstance().teamDAO.getByTournamentId(RuntimeEnvironment.application, 1);
        int flag = 0;
        for(DTeam dt : ret){
            if(dt.getId() == changeTeam.getId()){
                assertTrue(dt.getName().equals("ABCD"));
                flag = 1;
            }
        }
        assertTrue(flag == 1);

        for(DTeam dt : ret){
            DAOFactory.getInstance().teamDAO.delete(RuntimeEnvironment.application, dt.getId());
        }
        ret = DAOFactory.getInstance().teamDAO.getByTournamentId(RuntimeEnvironment.application, 1);
        assertTrue(ret.isEmpty());

    }
}