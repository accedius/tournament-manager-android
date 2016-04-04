package fit.cvut.org.cz.squash.buisness.managers;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.squash.data.DAOFactory;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ITournamentManager;
import fit.cvut.org.cz.tmlibrary.data.entities.DTournament;

/**
 * Created by Vaclav on 5. 4. 2016.
 */
public class TournamentManager implements ITournamentManager {
    @Override
    public void insert(Context context, Tournament tournament) {
        DAOFactory.getInstance().tournamentDAO.insert(context, Tournament.convertToDTournament(tournament));
    }

    @Override
    public void update(Context context, Tournament tournament) {
        DAOFactory.getInstance().tournamentDAO.update(context, Tournament.convertToDTournament(tournament));
    }

    @Override
    public void delete(Context context, long id) {

        //TODO Delete Matches and stats first
        DAOFactory.getInstance().tournamentDAO.delete(context, id);
    }

    @Override
    public Tournament getById(Context context, long id) {
        return new Tournament(DAOFactory.getInstance().tournamentDAO.getById(context, id));
    }

    @Override
    public ArrayList<Tournament> getByCompetitionId(Context context, long competitionId) {

        ArrayList<Tournament> tournaments = new ArrayList<>();
        ArrayList<DTournament> dTournaments = DAOFactory.getInstance().tournamentDAO.getByCompetitionId(context, competitionId);

        for (DTournament t: dTournaments){
            tournaments.add(new Tournament(t));
        }

        return tournaments;
    }
}
