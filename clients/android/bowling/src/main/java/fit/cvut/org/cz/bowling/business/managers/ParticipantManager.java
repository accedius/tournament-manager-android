package fit.cvut.org.cz.bowling.business.managers;

import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.bowling.business.managers.interfaces.IParticipantManager;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IParticipantStatManager;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IPlayerStatManager;
import fit.cvut.org.cz.bowling.data.entities.Match;
import fit.cvut.org.cz.bowling.data.entities.ParticipantStat;
import fit.cvut.org.cz.bowling.data.entities.PlayerStat;
import fit.cvut.org.cz.tmlibrary.data.entities.EntityDAO;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.ShareBase;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.entities.TournamentType;
import fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.helpers.TournamentTypes;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntity;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntityDAO;

public class ParticipantManager extends fit.cvut.org.cz.tmlibrary.business.managers.ParticipantManager implements IParticipantManager {
    @Override
    public List<Participant> getByMatchId(long matchId) {
        Tournament tournament = getTournamentByMatchId(matchId);
        int tournamentTypeId = tournament.getTypeId();

        IEntityDAO<Participant, Long> tmpDAO = managerFactory.getDaoFactory().getMyDao(Participant.class);
        List<Participant> participants = tmpDAO.getListItemById(DBConstants.cMATCH_ID, matchId);
        for (Participant participant : participants) {
            long participantId = participant.getId();
            IParticipantStatManager participantStatManager = managerFactory.getEntityManager(ParticipantStat.class);
            List<ParticipantStat> participantStats = participantStatManager.getByParticipantId(participantId);
            participant.setParticipantStats(participantStats);

            /*IPlayerStatManager playerStatManager = managerFactory.getEntityManager(PlayerStat.class);
            List<PlayerStat> playerStats = playerStatManager.getByParticipantId(participantId);
            participant.setPlayerStats(playerStats);*/

            setParticipantName(participant, tournamentTypeId);
        }
        return new ArrayList<>(participants);
    }

    @Override
    public List<Participant> getByMatchIdWithPlayerStats(long matchId) {
        Tournament tournament = getTournamentByMatchId(matchId);
        int tournamentTypeId = tournament.getTypeId();

        IEntityDAO<Participant, Long> tmpDAO = managerFactory.getDaoFactory().getMyDao(Participant.class);
        List<Participant> participants = tmpDAO.getListItemById(DBConstants.cMATCH_ID, matchId);
        for (Participant participant : participants) {
            long participantId = participant.getId();
            IParticipantStatManager participantStatManager = managerFactory.getEntityManager(ParticipantStat.class);
            List<ParticipantStat> participantStats = participantStatManager.getByParticipantId(participantId);
            participant.setParticipantStats(participantStats);

            IPlayerStatManager playerStatManager = managerFactory.getEntityManager(PlayerStat.class);
            List<PlayerStat> playerStats = playerStatManager.getByParticipantId(participantId);
            participant.setPlayerStats(playerStats);

            setParticipantName(participant, tournamentTypeId);
        }
        return new ArrayList<>(participants);
    }

    private Tournament getTournamentByMatchId (long matchId) {
        TournamentManager tournamentManager = managerFactory.getEntityManager(Tournament.class);
        MatchManager matchManager = managerFactory.getEntityManager(Match.class);
        Match match = matchManager.getByIdFromDao(matchId);
        Tournament tournament = tournamentManager.getById(match.getTournamentId());
        if(tournament == null) {
            tournament = new Tournament();
            tournament.setTypeId(-1);
        }
        return tournament;
    }

    /**
     * Sets participant's name to the name of individual or team depending on tournament type
     * @param participant reference (by default in Java) to participant
     * @param tournamentTypeId id of type of tournament
     */
    private void setParticipantName (Participant participant, int tournamentTypeId) {
        switch (tournamentTypeId) {
            case TournamentTypes.type_individuals: {
                long individualId = participant.getParticipantId();
                Player individual = managerFactory.getEntityManager(Player.class).getById(individualId);
                participant.setName(individual.getName());
                break;
            }
            case TournamentTypes.type_teams: {
                TeamManager teamManager = managerFactory.getEntityManager(Team.class);
                long teamId = participant.getParticipantId();
                Team team = teamManager.getById(teamId);
                if(team != null)
                    participant.setName(team.getName());
                else
                    participant.setName("NONAME");
                break;
            }
        }
    }

    @Override
    public List<Participant> getByMatchIdWithAllContents(long matchId) {
        Tournament tournament = getTournamentByMatchId(matchId);
        int tournamentTypeId = tournament.getTypeId();

        IEntityDAO<Participant, Long> tmpDAO = managerFactory.getDaoFactory().getMyDao(Participant.class);
        List<Participant> participants = tmpDAO.getListItemById(DBConstants.cMATCH_ID, matchId);
        for (Participant participant : participants) {
            long participantId = participant.getId();
            IParticipantStatManager participantStatManager = managerFactory.getEntityManager(ParticipantStat.class);
            List<ParticipantStat> participantStats = participantStatManager.getByParticipantIdWithAllContents(participantId);
            participant.setParticipantStats(participantStats);

            IPlayerStatManager playerStatManager = managerFactory.getEntityManager(PlayerStat.class);
            List<PlayerStat> playerStats = playerStatManager.getByParticipantId(participantId);
            participant.setPlayerStats(playerStats);

            setParticipantName(participant, tournamentTypeId);
        }
        return new ArrayList<>(participants);
    }
}
