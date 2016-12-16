package fit.cvut.org.cz.squash.business.managers;

import android.content.Context;
import android.test.AndroidTestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import java.util.Date;
import java.util.List;

import fit.cvut.org.cz.squash.BuildConfig;
import fit.cvut.org.cz.squash.business.ManagerFactory;
import fit.cvut.org.cz.squash.business.managers.interfaces.IMatchManager;
import fit.cvut.org.cz.squash.business.managers.interfaces.IPointConfigurationManager;
import fit.cvut.org.cz.squash.data.entities.Match;
import fit.cvut.org.cz.squash.presentation.SquashPackage;
import fit.cvut.org.cz.tmlibrary.business.helpers.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICompetitionManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITournamentManager;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.CompetitionType;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;

/**
 * Created by kevin on 16.12.2016.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MatchManagerTest extends AndroidTestCase {
    private Context context;
    private static final String sportContext = "Tennis";

    private static final String note = "Center Court";
    private static long tournamentId = 1;
    private static final long matchId = 1;
    private static final int round = 1;
    private static final int period = 1;
    private static final boolean played = false;

    public static ITournamentManager tournamentManager = null;
    public static ICompetitionManager competitionManager = null;
    public static IMatchManager matchManager = null;

    @Before
    public void setUp() {
        ShadowApplication testContext = Shadows.shadowOf(RuntimeEnvironment.application);
        context = testContext.getApplicationContext();
        ((SquashPackage)context.getApplicationContext()).setSportContext(sportContext);

        tournamentManager = ManagerFactory.getInstance(context).tournamentManager;
        competitionManager = ManagerFactory.getInstance(context).competitionManager;
        matchManager = ManagerFactory.getInstance(context).matchManager;

        /* Preconditions */
        assertNotNull(ManagerFactory.getInstance(context));
        assertEquals(sportContext, ManagerFactory.getInstance(context).sportDBHelper.getDBName());
        assertNotNull(competitionManager);
        assertNotNull(tournamentManager);
        assertNotNull(matchManager);
    }

    @Test
    public void procedure() {
        insert();
        getByCompetitionId();
        delete();
    }

    /**
     * Verify that when match is created, default point configuration is created.
     */
    private void insert() {
        Competition c = new Competition();
        c.setType(CompetitionTypes.teams());
        competitionManager.insert(c);
        Tournament t = new Tournament();
        t.setCompetitionId(c.getId());
        tournamentManager.insert(t);
        tournamentId = t.getId();

        matchManager.insert(new Match(new fit.cvut.org.cz.tmlibrary.data.entities.Match(matchId, tournamentId, new Date(), played, note, period, round)));
        Match match = matchManager.getById(matchId);
        assertEquals(tournamentId, match.getTournamentId());
        assertEquals(played, match.isPlayed());
        assertEquals(note, match.getNote());
        assertEquals(period, match.getPeriod());
        assertEquals(round, match.getRound());
    }

    /**
     * Verify getByCompetitionId method returns correct tournament list.
     */
    private void getByCompetitionId() {
        List<Match> matches = matchManager.getByTournamentId(tournamentId);
        assertEquals(1, matches.size());
        Match match = matches.get(0);
        assertEquals(tournamentId, match.getTournamentId());
        assertEquals(played, match.isPlayed());
        assertEquals(note, match.getNote());
        assertEquals(period, match.getPeriod());
        assertEquals(round, match.getRound());
    }

    /**
     * Verify that when tournament is deleted, related point configuration is also deleted.
     */
    private void delete() {
        matchManager.delete(matchId);
        Match match = matchManager.getById(matchId);
        assertNull(match);

        List<Match> matches = matchManager.getByTournamentId(tournamentId);
        assertNotNull(matches);
        assertEquals(0, matches.size());
    }
}