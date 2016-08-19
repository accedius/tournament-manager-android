package fit.cvut.org.cz.hockey.business.managers;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Date;

import fit.cvut.org.cz.hockey.BuildConfig;
import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.data.DAO.MatchDAO;
import fit.cvut.org.cz.hockey.data.DAO.PackagePlayerDAO;
import fit.cvut.org.cz.hockey.data.DAO.ParticipantDAO;
import fit.cvut.org.cz.hockey.data.DAO.TeamDAO;
import fit.cvut.org.cz.hockey.data.DAO.TournamentDAO;
import fit.cvut.org.cz.hockey.data.DAOFactory;
import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;
import fit.cvut.org.cz.tmlibrary.data.ParticipantType;
import fit.cvut.org.cz.tmlibrary.data.entities.DMatch;
import fit.cvut.org.cz.tmlibrary.data.entities.DParticipant;
import fit.cvut.org.cz.tmlibrary.data.entities.DTeam;
import fit.cvut.org.cz.tmlibrary.data.entities.DTournament;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IMatchDAO;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IPackagePlayerDAO;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IParticipantDAO;
import fit.cvut.org.cz.tmlibrary.data.interfaces.ITeamDAO;
import fit.cvut.org.cz.tmlibrary.data.interfaces.ITournamentDAO;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by atgot_000 on 5. 5. 2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class MatchManagerTest {

    @Mock
    IMatchDAO mockMatchDAO;
    @Mock
    IParticipantDAO mockParticipantDAO;
    @Mock
    ITeamDAO mockTeamDAO;
    @Mock
    IPackagePlayerDAO mockPlayerDAO;
    @Mock
    ITournamentDAO mockTournamentDAO;

    long id = 3;
    ArrayList<DParticipant> parts = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        mockMatchDAO = Mockito.mock(IMatchDAO.class);
        mockParticipantDAO = Mockito.mock(IParticipantDAO.class);
        mockTeamDAO = Mockito.mock(ITeamDAO.class);
        mockPlayerDAO = Mockito.mock(IPackagePlayerDAO.class);
        mockTournamentDAO = Mockito.mock(ITournamentDAO.class);
        parts.add( new DParticipant(1, 2, id, ParticipantType.home.toString()) );
        parts.add(new DParticipant(1, 2, id, ParticipantType.away.toString()));
        when(mockMatchDAO.getById(RuntimeEnvironment.application, id)).thenReturn(new DMatch(id, 1, 1, 1, new Date(), "note", true));
        when(mockParticipantDAO.getParticipantsByMatchId(RuntimeEnvironment.application, id)).thenReturn( parts );
        when(mockTeamDAO.getById(RuntimeEnvironment.application, 2)).thenReturn( new DTeam(2, 1, "TestName"));
        when(mockPlayerDAO.getPlayerIdsByParticipant(RuntimeEnvironment.application, 1)).thenReturn( new ArrayList<Long>());

        DAOFactory.getInstance().tournamentDAO = mockTournamentDAO;
        DAOFactory.getInstance().matchDAO = mockMatchDAO;
        DAOFactory.getInstance().participantDAO = mockParticipantDAO;
        DAOFactory.getInstance().teamDAO = mockTeamDAO;
        DAOFactory.getInstance().packagePlayerDAO = mockPlayerDAO;
    }

    @Test
    public void testMatchFindById() throws Exception {
        ScoredMatch match = ManagerFactory.getInstance().matchManager.getById(RuntimeEnvironment.application, id);
        assertTrue(match.getId() == id);
        assertTrue(match.getHomeName() == "TestName");
        assertTrue(match.getHomeIds().isEmpty());
        assertTrue(match.getNote() == "note");
    }

    @Test
    public void testBeginMatchUpdateCalled() throws Exception {
        long thisId = 5;
        when(mockParticipantDAO.getParticipantsByMatchId(RuntimeEnvironment.application, thisId)).thenReturn( new ArrayList<DParticipant>() );
        when(mockTournamentDAO.getById(RuntimeEnvironment.application, 1)).thenReturn(new DTournament(1, "tourn", new Date(), new Date(), "tourNote"));
        ManagerFactory.getInstance().matchManager.beginMatch(RuntimeEnvironment.application, id);
        verify(mockMatchDAO, times(0)).update(any(Context.class), any(DMatch.class));
        when(mockMatchDAO.getById(RuntimeEnvironment.application, id)).thenReturn(new DMatch(id, 1, 1, 1, new Date(), "note", false));
        ManagerFactory.getInstance().matchManager.beginMatch(RuntimeEnvironment.application, id);
        verify(mockMatchDAO).update(any(Context.class), any(DMatch.class));
    }

    @After
    public void tearDown() throws Exception {
        DAOFactory.getInstance().matchDAO = new MatchDAO();
        DAOFactory.getInstance().participantDAO = new ParticipantDAO();
        DAOFactory.getInstance().teamDAO = new TeamDAO();
        DAOFactory.getInstance().packagePlayerDAO = new PackagePlayerDAO();
        DAOFactory.getInstance().tournamentDAO = new TournamentDAO();
    }
}