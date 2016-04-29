package fit.cvut.org.cz.tmlibrary.business.interfaces;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.business.entities.Team;

/**
 * Created by Vaclav on 13. 4. 2016.
 */
public interface ITeamManager {

    void insert(Context context, Team team);
    void update(Context context, Team team);
    boolean delete(Context context, long id);

    Team getById(Context context, long id);
    ArrayList<Team> getByTournamentId(Context context, long tournamentId);

}
