package fit.cvut.org.cz.squash.business.loaders;

import android.content.Context;

import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.squash.business.ManagerFactory;
import fit.cvut.org.cz.squash.business.entities.SetRowItem;
import fit.cvut.org.cz.squash.business.serialization.MatchSerializer;
import fit.cvut.org.cz.squash.data.entities.Match;
import fit.cvut.org.cz.squash.data.entities.ParticipantStat;
import fit.cvut.org.cz.tmlibrary.business.helpers.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.PlayerStat;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;

/**
 * Created by kevin on 13.12.2016.
 */

public class MatchLoader {
    public static void importMatches(Context context, List<ServerCommunicationItem> matches, Tournament tournament, Competition competition, Map<String, Team> importedTeams, Map<String, Player> importedPlayers) {
        for (ServerCommunicationItem match : matches) {
            Match importedMatch = MatchSerializer.getInstance(context).deserialize(match);
            importedMatch.setTournamentId(tournament.getId());
            ManagerFactory.getInstance((context)).getEntityManager(Match.class).insert(importedMatch);

            String role = "home";
            long participantId;
            Participant participant, homeParticipant = null, awayParticipant = null;
            for (ServerCommunicationItem matchParticipant : match.subItems) {
                if (CompetitionTypes.teams().equals(competition.getType())) {
                    participantId = importedTeams.get(matchParticipant.getUid()).getId();
                } else {
                    participantId = importedPlayers.get(matchParticipant.getUid()).getId();
                }
                participant = new Participant(importedMatch.getId(), participantId, role);
                ManagerFactory.getInstance((context)).getEntityManager(Participant.class).insert(participant);
                if (role.equals("home")) {
                    homeParticipant = participant;
                } else {
                    awayParticipant = participant;
                }
                role = "away";
            }

            // Add Participant stats (sets)
            int i = 1;
            for (SetRowItem set : importedMatch.getSets()) {
                ParticipantStat homeStat = new ParticipantStat(homeParticipant.getId(), i, set.getHomeScore());
                ParticipantStat awayStat = new ParticipantStat(awayParticipant.getId(), i, set.getAwayScore());
                ManagerFactory.getInstance((context)).getEntityManager(ParticipantStat.class).insert(homeStat);
                ManagerFactory.getInstance((context)).getEntityManager(ParticipantStat.class).insert(awayStat);
                i++;
            }

            // Add Player stats to match
            for (PlayerStat playerStat : importedMatch.getHomePlayers()) {
                // TODO, getPlayerId in case of FileStrategy, getUid otherwise
                PlayerStat homePlayerStat = new PlayerStat(homeParticipant.getId(), importedPlayers.get(String.valueOf(playerStat.getPlayerId())).getId());
                ManagerFactory.getInstance((context)).getEntityManager(PlayerStat.class).insert(homePlayerStat);
            }
            for (PlayerStat playerStat : importedMatch.getAwayPlayers()) {
                // TODO, getPlayerId in case of FileStrategy, getUid otherwise
                PlayerStat awayPlayerStat = new PlayerStat(awayParticipant.getId(), importedPlayers.get(String.valueOf(playerStat.getPlayerId())).getId());
                ManagerFactory.getInstance((context)).getEntityManager(PlayerStat.class).insert(awayPlayerStat);
            }
        }
    }
}
