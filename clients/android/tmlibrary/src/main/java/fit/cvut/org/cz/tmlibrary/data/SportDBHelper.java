package fit.cvut.org.cz.tmlibrary.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.entities.CompetitionPlayer;
import fit.cvut.org.cz.tmlibrary.business.entities.Match;
import fit.cvut.org.cz.tmlibrary.business.entities.Participant;
import fit.cvut.org.cz.tmlibrary.business.entities.ParticipantStat;
import fit.cvut.org.cz.tmlibrary.business.entities.PlayerStat;
import fit.cvut.org.cz.tmlibrary.business.entities.PointConfiguration;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.entities.TeamPlayer;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.business.entities.TournamentPlayer;

/**
 * Created by kevin on 5.11.2016.
 */
abstract public class SportDBHelper extends OrmLiteSqliteOpenHelper {
    private Dao<Competition, Long> competitionDAO;
    private Dao<CompetitionPlayer, Long> competitionPlayerDAO;
    private Dao<Tournament, Long> tournamentDAO;
    private Dao<PointConfiguration, Long> pointConfigurationDAO;
    private Dao<TournamentPlayer, Long> tournamentPlayerDAO;
    private Dao<Team, Long> teamDAO;
    private Dao<TeamPlayer, Long> teamPlayerDAO;
    private Dao<Match, Long> matchDAO;
    private Dao<Participant, Long> participantDAO;
    private Dao<ParticipantStat, Long> participantStatDAO;
    private Dao<PlayerStat, Long> playerStatDAO;

    public SportDBHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
    }

    public Dao<Competition, Long> getCompetitionDAO() {
        if (competitionDAO == null) {
            try {
                competitionDAO = getDao(Competition.class);
            } catch (SQLException e) {
                return null;
            }
        }
        return competitionDAO;
    }

    public Dao<CompetitionPlayer, Long> getCompetitionPlayerDAO() {
        if (competitionPlayerDAO == null) {
            try {
                competitionPlayerDAO = getDao(CompetitionPlayer.class);
            } catch (SQLException e) {
                return null;
            }
        }
        return competitionPlayerDAO;
    }

    public Dao<Tournament, Long> getTournamentDAO() {
        if (tournamentDAO == null) {
            try {
                tournamentDAO = getDao(Tournament.class);
            } catch (SQLException e) {
                return null;
            }
        }
        return tournamentDAO;
    }

    public Dao<TournamentPlayer, Long> getTournamentPlayerDAO() {
        if (tournamentPlayerDAO == null) {
            try {
                tournamentPlayerDAO = getDao(TournamentPlayer.class);
            } catch (SQLException e) {
                return null;
            }
        }
        return tournamentPlayerDAO;
    }

    public Dao<Team, Long> getTeamDAO() {
        if (teamDAO == null) {
            try {
                teamDAO = getDao(Team.class);
            } catch (SQLException e) {
                return null;
            }
        }
        return teamDAO;
    }

    public Dao<TeamPlayer, Long> getTeamPlayerDAO() {
        if (teamPlayerDAO == null) {
            try {
                teamPlayerDAO = getDao(TeamPlayer.class);
            } catch (SQLException e) {
                return null;
            }
        }
        return teamPlayerDAO;
    }

    public Dao<Match, Long> getMatchDAO() {
        if (matchDAO == null) {
            try {
                matchDAO = getDao(Match.class);
            } catch (SQLException e) {
                return null;
            }
        }
        return matchDAO;
    }

    public Dao<Participant, Long> getParticipantDAO() {
        if (participantDAO == null) {
            try {
                participantDAO = getDao(Participant.class);
            } catch (SQLException e) {
                return null;
            }
        }
        return participantDAO;
    }

    public Dao<ParticipantStat, Long> getParticipantStatDAO() {
        if (participantStatDAO == null) {
            try {
                participantStatDAO = getDao(ParticipantStat.class);
            } catch (SQLException e) {
                return null;
            }
        }
        return participantStatDAO;
    }

    public Dao<PlayerStat, Long> getPlayerStatDAO() {
        if (playerStatDAO == null) {
            try {
                playerStatDAO = getDao(PlayerStat.class);
            } catch (SQLException e) {
                return null;
            }
        }
        return playerStatDAO;
    }

    public Dao<PointConfiguration, Long> getPointConfigurationDAO() {
        if (pointConfigurationDAO == null) {
            try {
                pointConfigurationDAO = getDao(PointConfiguration.class);
            } catch (SQLException e) {
                return null;
            }
        }
        return pointConfigurationDAO;
    }
}
