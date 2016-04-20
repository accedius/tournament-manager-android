package fit.cvut.org.cz.tmlibrary.data.interfaces;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.data.entities.DStat;

/**
 * Created by atgot_000 on 20. 4. 2016.
 */
public interface IStatDAO {

    long insert( Context context, DStat stat);
    void update( Context context, DStat stat);
    void delete( Context context, long id);

    ArrayList<DStat> getStatsByPlayerId( Context context, long playerId );
    ArrayList<DStat> getStatsByParticipantId( Context context, long participantId );
    ArrayList<DStat> getStatsByTournamentId( Context context, long tournamentId );
    ArrayList<DStat> getStatsByCompetitionId( Context context, long competitionId );
}
