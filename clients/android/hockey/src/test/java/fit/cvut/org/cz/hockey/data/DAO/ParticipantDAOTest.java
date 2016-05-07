package fit.cvut.org.cz.hockey.data.DAO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import fit.cvut.org.cz.hockey.BuildConfig;
import fit.cvut.org.cz.hockey.data.DAOFactory;
import fit.cvut.org.cz.tmlibrary.data.ParticipantType;
import fit.cvut.org.cz.tmlibrary.data.entities.DParticipant;

import static org.junit.Assert.*;

/**
 * Created by atgot_000 on 7. 5. 2016.
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ParticipantDAOTest {

    @Test
    public void testInsertUpdateDelete() throws Exception {
        DParticipant part = new DParticipant(1, 1, 1, ParticipantType.home.toString());
        DAOFactory.getInstance().participantDAO.insert(RuntimeEnvironment.application, part);
        DParticipant retPart = DAOFactory.getInstance().participantDAO.getById(RuntimeEnvironment.application, 1);

        assertTrue(part.getId() == retPart.getId());
        assertTrue(part.getRole().equals(retPart.getRole()));
        assertTrue(part.getMatchId() == retPart.getMatchId());
        assertTrue(part.getTeamId() == retPart.getTeamId());

        part.setMatchId(2);
        part.setTeamId(2);

        DAOFactory.getInstance().participantDAO.update(RuntimeEnvironment.application, part);
        retPart = DAOFactory.getInstance().participantDAO.getById(RuntimeEnvironment.application, 1);

        assertTrue(part.getId() == retPart.getId());
        assertTrue(part.getRole().equals(retPart.getRole()));
        assertTrue(part.getMatchId() == retPart.getMatchId());
        assertTrue(part.getTeamId() == retPart.getTeamId());

        DAOFactory.getInstance().participantDAO.delete(RuntimeEnvironment.application, part.getId());
        retPart = DAOFactory.getInstance().participantDAO.getById(RuntimeEnvironment.application, 1);

        assertNull(retPart);
    }

    @Test
    public void testGetByMatchId() throws Exception {
        DAOFactory.getInstance().participantDAO.insert(RuntimeEnvironment.application, new DParticipant(3, 1, 1, ParticipantType.home.toString()));
        DAOFactory.getInstance().participantDAO.insert(RuntimeEnvironment.application, new DParticipant(2, 1, 1, ParticipantType.away.toString()));

        ArrayList<DParticipant> retParts = DAOFactory.getInstance().participantDAO.getParticipantsByMatchId(RuntimeEnvironment.application, 1);

        assertTrue(retParts.size() == 2);
    }
}