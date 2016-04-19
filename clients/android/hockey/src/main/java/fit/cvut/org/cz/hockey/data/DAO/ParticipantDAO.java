package fit.cvut.org.cz.hockey.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

import fit.cvut.org.cz.hockey.data.DAOFactory;
import fit.cvut.org.cz.hockey.data.DatabaseFactory;
import fit.cvut.org.cz.tmlibrary.business.DateFormatFactory;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.entities.DParticipant;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IParticipantDAO;

/**
 * Created by atgot_000 on 18. 4. 2016.
 */
public class ParticipantDAO implements IParticipantDAO {

    private ContentValues toContVal(DParticipant participant)
    {
        ContentValues cv = new ContentValues();
        //cv.put(DBConstants.cID, participant.getId());
        cv.put(DBConstants.cUID, participant.getUid());
        cv.put(DBConstants.cETAG, participant.getEtag());
        cv.put(DBConstants.cTEAM_ID, participant.getTeamId());
        cv.put(DBConstants.cMATCH_ID, participant.getMatchId());
        cv.put(DBConstants.cROLE, participant.getRole());
        if ( participant.getLastSynchronized() != null )
            cv.put(DBConstants.cLASTSYNCHRONIZED, DateFormatFactory.getInstance().getDateFormat().format(participant.getLastSynchronized()));
        cv.put(DBConstants.cLASTMODIFIED, DateFormatFactory.getInstance().getDateFormat().format(new Date()));

        return cv;
    }

    private void createStatistics( long playerId, long participantId, long tournamentId, long competitionId, SQLiteDatabase db )
    {
        //TODO dodelat vkladani vsech statistik
        ContentValues cv = new ContentValues();
        cv.put( DBConstants.cPARTICIPANT_ID, participantId );
        cv.put(DBConstants.cPLAYER_ID, playerId);
        cv.put( DBConstants.cTOURNAMENT_ID, tournamentId);
        cv.put( DBConstants.cCOMPETITIONID, competitionId);
        cv.put( DBConstants.cSTATS_ENUM_ID, 1 );
        cv.put( DBConstants.cVALUE, Long.toString( 0 ) );
        db.insert( DBConstants.tSTATS, null, cv );

    }

    @Override
    public long insert(Context context, DParticipant participant) {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase( context );

        ContentValues values = toContVal( participant );

        long newRowId;
        newRowId = db.insert(DBConstants.tPARTICIPANTS, null, values);

        long tournamentId = DAOFactory.getInstance().matchDAO.getById( context, participant.getMatchId() ).getTournamentId();
        long competitionId = DAOFactory.getInstance().tournamentDAO.getById( context, tournamentId ).getCompetitionId();

        for( Long pId : participant.getPlayerIds() )
        {
            createStatistics( pId, newRowId, tournamentId, competitionId, db );
        }

        return newRowId;
    }

    @Override
    public void update(Context context, DParticipant participant) {

    }

    @Override
    public void delete(Context context, long id) {

    }
}
