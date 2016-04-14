package fit.cvut.org.cz.tmlibrary.data.interfaces;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.DTeam;

/**
 * Created by Vaclav on 13. 4. 2016.
 */
public interface ITeamDAO {

    void insert(Context context, DTeam team);
    void update(Context context, DTeam team);
    void delete(Context context, long id);

    DTeam getById(Context context, long id);
    ArrayList<DTeam> getByTournamentId(Context context, long tournamentId);

}
