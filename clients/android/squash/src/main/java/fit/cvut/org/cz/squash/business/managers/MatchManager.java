package fit.cvut.org.cz.squash.business.managers;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IScoredMatchManager;

/**
 * Created by Vaclav on 10. 4. 2016.
 */
public class MatchManager implements IScoredMatchManager {
    @Override
    public ArrayList<ScoredMatch> getByTournamentId(Context context, long tournamentId) {
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
    public ScoredMatch getById(Context context, long Id) {
        return null;
    }

    @Override
    public void beginMatch(Context context, long matchId) {

    }

    @Override
    public void insert(Context context, ScoredMatch match) {

    }

    @Override
    public void update(Context context, ScoredMatch match) {

    }

    @Override
    public void delete(Context context, long id) {

    }
}
