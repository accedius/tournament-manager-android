package fit.cvut.org.cz.squash.business.managers;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IScoredMatchManager;

/**
 * Created by Vaclav on 10. 4. 2016.
 */
public class MatchManager implements IScoredMatchManager {
    @Override
    public ArrayList<ScoredMatch> getByTournamentId(long tournamentId) {
        ArrayList<ScoredMatch> matches = new ArrayList<>();

        ScoredMatch m = new ScoredMatch();
        m.setPlayed(false);
        m.setHomeName("Team1");
        m.setAwayName("Team2");
        m.setPeriod(1);
        m.setRound(1);

        matches.add(m);

        m = new ScoredMatch();
        m.setPlayed(false);
        m.setHomeName("Team2");
        m.setAwayName("Team1");
        m.setPeriod(1);
        m.setRound(1);

        matches.add(m);
        m = new ScoredMatch();
        m.setPlayed(false);
        m.setHomeName("Team1");
        m.setAwayName("Team2");
        m.setPeriod(2);
        m.setRound(1);

        matches.add(m);
        m = new ScoredMatch();
        m.setPlayed(false);
        m.setHomeName("Team1");
        m.setAwayName("Team2");
        m.setPeriod(2);
        m.setRound(1);

        matches.add(m);
        m = new ScoredMatch();
        m.setPlayed(false);
        m.setHomeName("Team1");
        m.setAwayName("Team2");
        m.setPeriod(1);
        m.setRound(2);

        matches.add(m);
        m = new ScoredMatch();
        m.setPlayed(false);
        m.setHomeName("Team1");
        m.setAwayName("Team2");
        m.setPeriod(1);
        m.setRound(2);

        matches.add(m);


        return matches;
    }

    @Override
    public ScoredMatch getById(long Id) {
        return null;
    }
}
