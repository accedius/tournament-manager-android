package fit.cvut.org.cz.hockey.business.loaders;

import android.content.Context;

import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.business.entities.Match;
import fit.cvut.org.cz.hockey.business.entities.ParticipantStat;
import fit.cvut.org.cz.hockey.business.entities.PlayerStat;
import fit.cvut.org.cz.hockey.business.serialization.MatchSerializer;
import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.entities.Participant;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;

/**
 * Created by kevin on 13.12.2016.
 */

public class MatchLoader {
    public static void importMatches(Context context, List<ServerCommunicationItem> matches, Tournament tournament, Competition competition, Map<String, Team> importedTeams, Map<String, Player> importedPlayers) {
        for (ServerCommunicationItem match : matches) {
            Match importedMatch = MatchSerializer.getInstance(context).deserialize(match);
            importedMatch.setTournamentId(tournament.getId());
            ManagerFactory.getInstance(context).matchManager.insert(importedMatch);

            String role = "home";
            long participantId;
            Participant participant, homeParticipant = null, awayParticipant = null;
            for (ServerCommunicationItem matchTeam : match.subItems) {
                participantId = importedTeams.get(matchTeam.getUid()).getId();
                participant = new Participant(importedMatch.getId(), participantId, role);
                ManagerFactory.getInstance(context).participantManager.insert(participant);
                if (role.equals("home")) {
                    homeParticipant = participant;
                } else {
                    awayParticipant = participant;
                }
                role = "away";
            }

            // Add Participant stats (score)
            if (importedMatch.isPlayed()) {
                ParticipantStat homeStat = new ParticipantStat(homeParticipant.getId(), importedMatch.getHomeScore());
                ParticipantStat awayStat = new ParticipantStat(awayParticipant.getId(), importedMatch.getAwayScore());
                ManagerFactory.getInstance(context).participantStatManager.insert(homeStat);
                ManagerFactory.getInstance(context).participantStatManager.insert(awayStat);
            }

            // Add Player stats to match
            for (PlayerStat playerStat : importedMatch.getHomePlayers()) {
                // TODO, getPlayerId in case of FileStrategy, getUid otherwise
                PlayerStat homePlayerStat = new PlayerStat(playerStat);
                homePlayerStat.setParticipantId(homeParticipant.getId());
                homePlayerStat.setPlayerId(importedPlayers.get(String.valueOf(playerStat.getPlayerId())).getId());
                ManagerFactory.getInstance(context).playerStatManager.insert(homePlayerStat);
            }
            for (PlayerStat playerStat : importedMatch.getAwayPlayers()) {
                // TODO, getPlayerId in case of FileStrategy, getUid otherwise
                PlayerStat awayPlayerStat = new PlayerStat(playerStat);
                awayPlayerStat.setParticipantId(awayParticipant.getId());
                awayPlayerStat.setPlayerId(importedPlayers.get(String.valueOf(playerStat.getPlayerId())).getId());
                ManagerFactory.getInstance(context).playerStatManager.insert(awayPlayerStat);
            }
        }
    }
}
