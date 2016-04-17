package fit.cvut.org.cz.hockey.business.managers;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.hockey.data.DAOFactory;
import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IScoredMatchManager;
import fit.cvut.org.cz.tmlibrary.data.entities.DMatch;

/**
 * Created by atgot_000 on 17. 4. 2016.
 */
public class MatchManager implements IScoredMatchManager {

    @Override
    public ArrayList<ScoredMatch> getByTournamentId(Context context, long tournamentId) {

        ArrayList<DMatch> dMatches = DAOFactory.getInstance().matchDAO.getByTournamentId( context, tournamentId );
        ArrayList<ScoredMatch> res = new ArrayList<>();

        for( DMatch dm : dMatches )
        {
            ScoredMatch match = new ScoredMatch();
            match.setRound( dm.getRound() );
            match.setPeriod( dm.getPeriod() );
            match.setId( dm.getId() );
            match.setTournamentId( dm.getTournamentId() );
            match.setDate( dm.getDate() );

            //TODO pridat nahrani hracu a skore podle participantu do scoredMatch


            res.add( match );
        }


        return res;
    }

    @Override
    public ScoredMatch getById(Context context, long Id) {
        return null;
    }
}
