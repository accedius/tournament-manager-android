package fit.cvut.org.cz.bowling.business.loaders;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.business.serialization.TeamSerializer;
import fit.cvut.org.cz.bowling.business.serialization.TournamentSerializer;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.TournamentImportInfo;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITeamManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITournamentManager;
import fit.cvut.org.cz.tmlibrary.business.serialization.Constants;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;

/**
 * Support class for loading tournaments of a competition from import file
 */
public class TournamentLoader {
    /**
     * Method to get import info about tournaments to import
     * @param context
     * @param tournaments
     * @return
     */
    public static List<TournamentImportInfo> getTournamentsImportInfo(Context context, List<ServerCommunicationItem> tournaments) {
        List<TournamentImportInfo> tournamentsInfo = new ArrayList<>();
        for (ServerCommunicationItem tournament : tournaments) {
            List<ServerCommunicationItem> tournamentPlayers = new ArrayList<>();
            List<ServerCommunicationItem> tournamentTeams = new ArrayList<>();
            List<ServerCommunicationItem> tournamentMatches = new ArrayList<>();

            Tournament importedTournament = TournamentSerializer.getInstance(context).deserialize(tournament);

            for (ServerCommunicationItem subItem : tournament.subItems) {
                if (subItem.getType().equals(Constants.PLAYER)) {
                    tournamentPlayers.add(subItem);
                } else if (subItem.getType().equals(Constants.TEAM)) {
                    tournamentTeams.add(subItem);
                } else if (subItem.getType().equals(Constants.MATCH)) {
                    tournamentMatches.add(subItem);
                }
            }

            tournamentsInfo.add(new TournamentImportInfo(importedTournament.getName(), tournamentPlayers.size(), tournamentTeams.size(), tournamentMatches.size()));
        }
        return tournamentsInfo;
    }

    /**
     * Method to import tournaments to local database
     * @param context
     * @param tournaments
     * @param importedCompetition related class
     * @param importedPlayers related class
     */
    public static void importTournaments(Context context, List<ServerCommunicationItem> tournaments, Competition importedCompetition, Map<String, Player> importedPlayers) {
        for (ServerCommunicationItem tournament : tournaments) {
            List<ServerCommunicationItem> tournamentPlayers = new ArrayList<>();
            List<ServerCommunicationItem> tournamentTeams = new ArrayList<>();
            List<ServerCommunicationItem> tournamentMatches = new ArrayList<>();

            Log.d("IMPORT", "Tournament: " + tournament.syncData);
            Tournament importedTournament = TournamentSerializer.getInstance(context).deserialize(tournament);
            importedTournament.setCompetitionId(importedCompetition.getId());
            ManagerFactory.getInstance(context).getEntityManager(Tournament.class).insert(importedTournament);

            for (ServerCommunicationItem subItem : tournament.subItems) {
                if (subItem.getType().equals(Constants.PLAYER)) {
                    tournamentPlayers.add(subItem);
                } else if (subItem.getType().equals(Constants.TEAM)) {
                    tournamentTeams.add(subItem);
                } else if (subItem.getType().equals(Constants.MATCH)) {
                    tournamentMatches.add(subItem);
                }
            }

            /* Players loading */
            for (ServerCommunicationItem player : tournamentPlayers) {
                // Add player to tournament.
                long playerId = importedPlayers.get(player.getUid()).getId();
                ((ITournamentManager)ManagerFactory.getInstance(context).getEntityManager(Tournament.class)).addPlayer(playerId, importedTournament.getId());
            }

            /* Teams loading */
            Map<String, Team> importedTeams = new HashMap<>();
            for (ServerCommunicationItem team : tournamentTeams) {
                // Add team to tournament.
                Team importedTeam = TeamSerializer.getInstance(context).deserialize(team);
                importedTeam.setTournamentId(importedTournament.getId());
                ManagerFactory.getInstance(context).getEntityManager(Team.class).insert(importedTeam);
                importedTeams.put(team.getUid(), importedTeam);

                // Add players to team.
                ArrayList<Player> teamPlayers = new ArrayList<>();
                for (ServerCommunicationItem teamPlayer : team.subItems) {
                    if (teamPlayer.getType().equals(Constants.PLAYER)) {
                        teamPlayers.add(importedPlayers.get(teamPlayer.getUid()));
                    }
                }
                ((ITeamManager)ManagerFactory.getInstance(context).getEntityManager(Team.class)).updatePlayersInTeam(importedTeam.getId(), teamPlayers);
            }

            /* Matches loading */
            MatchLoader.importMatches(context, tournamentMatches, importedTournament, importedCompetition, importedTeams, importedPlayers);
        }
    }
}
