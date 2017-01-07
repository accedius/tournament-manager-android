package fit.cvut.org.cz.tournamentmanager.business.generators;

import android.test.AndroidTestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.generators.AllPlayAllMatchGenerator;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IMatchGenerator;
import fit.cvut.org.cz.tmlibrary.data.entities.Match;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.ParticipantType;
import fit.cvut.org.cz.tournamentmanager.BuildConfig;

/**
 * Created by kevin on 5.1.2017.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class AllPlayAllMatchGeneratorTest extends AndroidTestCase {
    IMatchGenerator matchGenerator = null;

    @Before
    public void setUp() {
        matchGenerator = new AllPlayAllMatchGenerator();
        assertNotNull(matchGenerator);
    }

    /**
     * Verify no match is generated for one or no participant.
     */
    @Test
    public void verifyNoMatchGenerated() {
        List<Participant> participants = new ArrayList<>();
        List<Match> round = matchGenerator.generateRound(participants, 1);
        assertNotNull(round);
        assertTrue(round.isEmpty());

        participants.add(new Participant(-1, 1, null));
        round = matchGenerator.generateRound(participants, 1);
        assertNotNull(round);
        assertTrue(round.isEmpty());
    }

    /**
     * Verify round generation.
     */
    @Test
    public void verifyMatchesCountInRound() {
        List<Participant> participants = new ArrayList<>();
        participants.add(new Participant(-1, 1, null));
        participants.add(new Participant(-1, 2, null));
        participants.add(new Participant(-1, 3, null));

        List<Match> round = matchGenerator.generateRound(participants, 1);
        assertNotNull(round);
        assertEquals(3, round.size());

        /* Verify that each participant plays two matches */
        assertEquals(2, countMatchesForParticipantId(round, 1));
        assertEquals(2, countMatchesForParticipantId(round, 2));
        assertEquals(2, countMatchesForParticipantId(round, 3));
    }

    /**
     * Verify that all participants play with all
     */
    @Test
    public void verifyAllPlayAll() {
        List<Participant> participants = new ArrayList<>();
        participants.add(new Participant(-1, 1, null));
        participants.add(new Participant(-1, 2, null));
        participants.add(new Participant(-1, 3, null));
        participants.add(new Participant(-1, 4, null));

        List<Match> round = matchGenerator.generateRound(participants, 1);
        List<Match> round2 = matchGenerator.generateRound(participants, 2);
        round.addAll(round2);

        assertNotNull(round);
        assertEquals(12, round.size());

        /* Verify that each participant play with each other participant twice,
        once as home and once as away */
        assertEquals(1, countMatchesForHomeAndAwayId(round, 1, 2));
        assertEquals(1, countMatchesForHomeAndAwayId(round, 2, 1));
        assertEquals(1, countMatchesForHomeAndAwayId(round, 3, 4));
        assertEquals(1, countMatchesForHomeAndAwayId(round, 4, 3));

        assertEquals(1, countMatchesForHomeAndAwayId(round, 1, 3));
        assertEquals(1, countMatchesForHomeAndAwayId(round, 3, 1));
        assertEquals(1, countMatchesForHomeAndAwayId(round, 2, 4));
        assertEquals(1, countMatchesForHomeAndAwayId(round, 4, 2));

        assertEquals(1, countMatchesForHomeAndAwayId(round, 1, 4));
        assertEquals(1, countMatchesForHomeAndAwayId(round, 4, 1));
        assertEquals(1, countMatchesForHomeAndAwayId(round, 2, 3));
        assertEquals(1, countMatchesForHomeAndAwayId(round, 3, 2));
    }

    private int countMatchesForParticipantId(List<Match> matches, long participantId) {
        int count = 0;
        for (Match match : matches)
            for (Participant participant : match.getParticipants())
                if (participant.getParticipantId() == participantId)
                    count++;

        return count;
    }

    private int countMatchesForHomeAndAwayId(List<Match> matches, long homeId, long awayId) {
        int count = 0;
        for (Match match : matches) {
            int matched = 0;
            for (Participant participant : match.getParticipants()) {
                if (participant.getParticipantId() == homeId && participant.getRole().equals(ParticipantType.home.toString()))
                    matched++;
                else if (participant.getParticipantId() == awayId && participant.getRole().equals(ParticipantType.away.toString()))
                    matched++;
            }
            if (matched == 2)
                count++;
        }

        return count;
    }
}
