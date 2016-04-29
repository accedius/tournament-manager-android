package fit.cvut.org.cz.hockey.business.managers;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.data.DAOFactory;
import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ITeamManager;
import fit.cvut.org.cz.tmlibrary.data.entities.DParticipant;
import fit.cvut.org.cz.tmlibrary.data.entities.DTeam;

/**
 * Created by atgot_000 on 17. 4. 2016.
 */
public class TeamManager implements ITeamManager {
    @Override
    public void insert(Context context, Team team) {
        DTeam dt = Team.convertToDTeam( team );
        DAOFactory.getInstance().teamDAO.insert( context, dt );
    }

    @Override
    public void update(Context context, Team team) {
        DTeam dt = Team.convertToDTeam( team );
        DAOFactory.getInstance().teamDAO.update(context, dt);
    }

    @Override
    public boolean delete(Context context, long id) {
        Team t = getById( context, id );
        ArrayList<ScoredMatch> matches = ManagerFactory.getInstance().matchManager.getByTournamentId( context, t.getTournamentId() );
        for( ScoredMatch match : matches ){
            if( match.getHomeParticipantId() == id || match.getAwayParticipantId() == id ) return false;
        }


        DAOFactory.getInstance().packagePlayerDAO.deleteAllPlayersFromTeam(context, id);
        DAOFactory.getInstance().teamDAO.delete( context, id );
        return true;
    }

    @Override
    public Team getById(Context context, long id) {

        DTeam dt = DAOFactory.getInstance().teamDAO.getById( context, id );
        Team t = new Team( dt );
        t.setPlayers(ManagerFactory.getInstance().packagePlayerManager.getPlayersByTeam(context, t.getId()));
        return t;
    }

    @Override
    public ArrayList<Team> getByTournamentId(Context context, long tournamentId) {

        ArrayList<DTeam> dts = DAOFactory.getInstance().teamDAO.getByTournamentId( context, tournamentId );
        ArrayList<Team> ts = new ArrayList<>();

        for( DTeam i : dts )
        {
            Team t = new Team(i);
            t.setPlayers(ManagerFactory.getInstance().packagePlayerManager.getPlayersByTeam(context, t.getId()));
            ts.add(t);
        }

        return ts;
    }
}
