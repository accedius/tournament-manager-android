package fit.cvut.org.cz.bowling.business.loaders;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IParticipantManager;
import fit.cvut.org.cz.bowling.business.serialization.Constants;
import fit.cvut.org.cz.bowling.business.serialization.MatchSerializer;
import fit.cvut.org.cz.bowling.data.entities.Match;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.helpers.TournamentTypes;

/**
 * Support class for loading matches of a competition from import file
 */
public class MatchLoader {
    /**
     * Method to import class to local database
     * @param context context of execution
     * @param matches item to import
     * @param tournament related class
     * @param competition related class
     * @param importedTeams related class
     * @param importedPlayers related class
     */
    public static void importMatches(Context context, List<ServerCommunicationItem> matches, Tournament tournament, Competition competition, Map<String, Team> importedTeams, Map<String, Player> importedPlayers) {
        for (ServerCommunicationItem match : matches) {
            Log.d("IMPORT", "Match: " + match.syncData);
            Match importedMatch = MatchSerializer.getInstance(context).deserialize(match);
            importedMatch.setTournamentId(tournament.getId());
            ManagerFactory.getInstance(context).getEntityManager(Match.class).insert(importedMatch);

            HashMap<String, ServerCommunicationItem> matchTeamSCIs = new HashMap<>();
            HashMap<String, ServerCommunicationItem> matchPlayerSCIs = new HashMap<>();
            for (ServerCommunicationItem subItem : match.getSubItems()){
                if (subItem.getType().equals(Constants.TEAM)) {
                    matchTeamSCIs.put(subItem.getUid(), subItem);
                }
                if (subItem.getType().equals(Constants.PLAYER)) {
                    matchPlayerSCIs.put(subItem.getUid(), subItem);
                }
            }

            IParticipantManager participantManager = ManagerFactory.getInstance(context).getEntityManager(Participant.class);

            if (tournament.getTypeId() == TournamentTypes.individuals().id) {
                for (String uid : matchPlayerSCIs.keySet()) {
                    Player player = importedPlayers.get(uid);
                    Participant participant = new Participant(importedMatch.getId(), player.getId(), null);
                    participantManager.insert(participant);

                    List<ServerCommunicationItem> playerFrames = new ArrayList<>();

                    ServerCommunicationItem playerSCI = matchPlayerSCIs.get(uid);
                    for (ServerCommunicationItem subItem : playerSCI.getSubItems()) {
                        if (subItem.getType().equals(Constants.PLAYER_STAT)) {
                            PlayerStatLoader.importPlayerStat(context, subItem, player, participant);
                        }
                        if (subItem.getType().equals(Constants.PARTICIPANT_STAT)) {
                            ParticipantStatLoader.importParticipantStat(context, subItem, participant);
                        }
                        if (subItem.getType().equals(Constants.FRAME)) {
                            playerFrames.add(subItem);
                        }
                    }

                    if (importedMatch.isTrackRolls()) {
                        FrameLoader.importFrames(context, playerFrames, player, importedMatch, participant);
                    }
                }
            }
            else if (tournament.getTypeId() == TournamentTypes.teams().id) {
                for (String uid : matchTeamSCIs.keySet()) {
                    ServerCommunicationItem teamSCI = matchTeamSCIs.get(uid);
                    Team team = importedTeams.get(teamSCI.getUid());
                    Participant participant = new Participant(importedMatch.getId(), team.getId(), null);
                    participantManager.insert(participant);

                    for (ServerCommunicationItem subItem : teamSCI.getSubItems()) {
                        if (subItem.getType().equals(Constants.PARTICIPANT_STAT)) {
                            ParticipantStatLoader.importParticipantStat(context, subItem, participant);
                        }
                        else if (subItem.getType().equals(Constants.PLAYER)) {
                            List<ServerCommunicationItem> frameSCIs = new ArrayList<>();
                            for (ServerCommunicationItem playerSubItem : subItem.getSubItems()) {
                                if (playerSubItem.getType().equals(Constants.PLAYER_STAT)) {
                                    PlayerStatLoader.importPlayerStat(context, playerSubItem, importedPlayers.get(subItem.getUid()), participant);
                                }
                                else if (playerSubItem.getType().equals(Constants.FRAME)) {
                                    frameSCIs.add(playerSubItem);
                                }
                            }
                            if (importedMatch.isTrackRolls()) {
                                FrameLoader.importFrames(context, frameSCIs, importedPlayers.get(subItem.getUid()), importedMatch, participant);
                            }
                        }
                    }

                }
            }
        }
    }
}
