package fit.cvut.org.cz.tmlibrary.business.interfaces;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;

/**
 * Created by Vaclav on 10. 4. 2016.
 */
public interface IScoredMatchManager {

    ArrayList<ScoredMatch> getByTournamentId(Context context, long tournamentId);
    ScoredMatch getById(Context context, long Id);

    void beginMatch( Context context, long matchId ); //register players to match and other things needed when match is begun

    void generateRound(Context context, long tournamentId);
    void resetMatch( Context context, long matchId );

    void insert( Context context, ScoredMatch match);
    void update( Context context, ScoredMatch match );
    void delete( Context context, long id);
}
