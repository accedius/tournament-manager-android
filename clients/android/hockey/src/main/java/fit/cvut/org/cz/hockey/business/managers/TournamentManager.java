package fit.cvut.org.cz.hockey.business.managers;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.hockey.data.DAOFactory;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ITournamentManager;
import fit.cvut.org.cz.tmlibrary.data.entities.DTournament;

/**
 * Created by atgot_000 on 5. 4. 2016.
 */
public class TournamentManager implements ITournamentManager {
    @Override
    public void insert(Context context, Tournament tournament) {
        DTournament dt = Tournament.convertToDTournament( tournament );
        DAOFactory.getInstance().tournamentDAO.insert( context, dt );
    }

    @Override
    public void update(Context context, Tournament tournament) {
        DTournament dt = Tournament.convertToDTournament( tournament );
        DAOFactory.getInstance().tournamentDAO.update( context, dt );
    }

    @Override
    public void delete(Context context, long id) {
        DAOFactory.getInstance().tournamentDAO.delete( context, id );
    }

    @Override
    public Tournament getById(Context context, long id) {
        DTournament dt = DAOFactory.getInstance().tournamentDAO.getById( context, id );
        return new Tournament( dt );
    }

    @Override
    public ArrayList<Tournament> getByCompetitionId(Context context, long competitionId) {

        ArrayList<Tournament> res = new ArrayList<>();
        ArrayList<DTournament> dTList = DAOFactory.getInstance().tournamentDAO.getByCompetitionId( context, competitionId );

        for ( DTournament dt: dTList )
        {
            res.add( new Tournament(dt) );
        }
        return res;
    }
}
