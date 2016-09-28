package fit.cvut.org.cz.hockey.business.managers;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.hockey.data.DAOFactory;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ITournamentManager;
import fit.cvut.org.cz.tmlibrary.data.entities.DMatch;
import fit.cvut.org.cz.tmlibrary.data.entities.DTeam;
import fit.cvut.org.cz.tmlibrary.data.entities.DTournament;

/**
 * Created by atgot_000 on 5. 4. 2016.
 */
public class TournamentManager implements ITournamentManager {
    @Override
    public void insert(Context context, Tournament tournament) {
        DTournament dt = Tournament.convertToDTournament(tournament);
        long tourId = DAOFactory.getInstance().tournamentDAO.insert(context, dt);
        DAOFactory.getInstance().pointConfigDAO.insertDefault(context, tourId);
    }

    @Override
    public void update(Context context, Tournament tournament) {
        DTournament dt = Tournament.convertToDTournament(tournament);
        DAOFactory.getInstance().tournamentDAO.update(context, dt);
    }

    @Override
    public boolean delete(Context context, long id) {
        ArrayList<DTeam> teams = DAOFactory.getInstance().teamDAO.getByTournamentId(context, id);
        ArrayList<Long> players = DAOFactory.getInstance().packagePlayerDAO.getPlayerIdsByTournament(context, id);
        ArrayList<DMatch> matches = DAOFactory.getInstance().matchDAO.getByTournamentId(context, id);
        if (teams.size() == 0 && players.size() == 0 && matches.size() == 0) {
            DAOFactory.getInstance().tournamentDAO.delete(context, id);
            DAOFactory.getInstance().pointConfigDAO.delete(context, id);
            return true;
        }
        return false;
    }

    @Override
    public Tournament getById(Context context, long id) {
        DTournament dt = DAOFactory.getInstance().tournamentDAO.getById(context, id);
        return new Tournament(dt);
    }

    @Override
    public ArrayList<Tournament> getByCompetitionId(Context context, long competitionId) {
        ArrayList<Tournament> res = new ArrayList<>();
        ArrayList<DTournament> dTList = DAOFactory.getInstance().tournamentDAO.getByCompetitionId(context, competitionId);

        for (DTournament dt: dTList) {
            res.add(new Tournament(dt));
        }
        return res;
    }
}
