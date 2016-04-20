package fit.cvut.org.cz.hockey.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import fit.cvut.org.cz.hockey.data.DAOFactory;
import fit.cvut.org.cz.hockey.data.DatabaseFactory;
import fit.cvut.org.cz.hockey.data.HockeyDBConstants;
import fit.cvut.org.cz.hockey.data.StatsEnum;
import fit.cvut.org.cz.tmlibrary.business.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.entities.DParticipant;

/**
 * Created by atgot_000 on 20. 4. 2016.
 */
public class StatisticsDAO {

    private ContentValues createStatisticsCV( long participantId, long tournamentId, long competitionId )
    {
        ContentValues cv = new ContentValues();
        cv.put( DBConstants.cPARTICIPANT_ID, participantId );
        cv.put( DBConstants.cTOURNAMENT_ID, tournamentId);
        cv.put( DBConstants.cCOMPETITIONID, competitionId);
        cv.put( DBConstants.cSTATS_ENUM_ID, StatsEnum.team_goals.getId());
        cv.put( DBConstants.cVALUE, Long.toString(0) );
        return cv;

    }

    public void createStatsForMatch( Context context, long matchId )
    {
        SQLiteDatabase db = DatabaseFactory.getInstance().getDatabase( context );

        ContentValues values = new ContentValues();
        values.put(HockeyDBConstants.cOVERTIME, 0);
        values.put(HockeyDBConstants.cSHOOTOUTS, 0);
        values.put(DBConstants.cMATCH_ID, matchId);

        long newRowId;
        newRowId = db.insert(HockeyDBConstants.tMATCH_SCORE, null, values);

        long tournamentId = DAOFactory.getInstance().matchDAO.getById( context, matchId ).getTournamentId();
        long competitionId = DAOFactory.getInstance().tournamentDAO.getById( context, tournamentId ).getCompetitionId();

        ArrayList<DParticipant> participants = DAOFactory.getInstance().participantDAO.getParticipantsByMatchId(context, matchId);

        for(DParticipant dp : participants) {
            values = createStatisticsCV( dp.getId(), tournamentId, competitionId );
            db.insert(DBConstants.tSTATS, null, values);
        }

        return;
    }

}
