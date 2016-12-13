package fit.cvut.org.cz.squash.business.loaders;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.squash.business.ManagerFactory;
import fit.cvut.org.cz.squash.business.serialization.TeamSerializer;
import fit.cvut.org.cz.squash.business.serialization.TournamentSerializer;
import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.business.entities.TournamentImportInfo;
import fit.cvut.org.cz.tmlibrary.business.serialization.ServerCommunicationItem;

/**
 * Created by kevin on 13.12.2016.
 */

public class TournamentLoader {
    public static List<TournamentImportInfo> getTournamentsImportInfo(Context context, List<ServerCommunicationItem> tournaments) {
        List<TournamentImportInfo> tournamentsInfo = new ArrayList<>();
        for (ServerCommunicationItem tournament : tournaments) {
            List<ServerCommunicationItem> tournamentPlayers = new ArrayList<>();
            List<ServerCommunicationItem> tournamentTeams = new ArrayList<>();
            List<ServerCommunicationItem> tournamentMatches = new ArrayList<>();

            Tournament importedTournament = TournamentSerializer.getInstance(context).deserialize(tournament);

            for (ServerCommunicationItem subItem : tournament.subItems) {
                if (subItem.getType().equals("Player")) {
                    tournamentPlayers.add(subItem);
                } else if (subItem.getType().equals("Team")) {
                    tournamentTeams.add(subItem);
                } else if (subItem.getType().equals("Match")) {
                    tournamentMatches.add(subItem);
                }
            }

            tournamentsInfo.add(new TournamentImportInfo(importedTournament.getName(), tournamentPlayers.size(), tournamentTeams.size(), tournamentMatches.size()));
        }
        return tournamentsInfo;
    }

    public static void importTournaments(Context context, List<ServerCommunicationItem> tournaments, Competition importedCompetition, Map<String, Player> importedPlayers) {
        for (ServerCommunicationItem tournament : tournaments) {
            List<ServerCommunicationItem> tournamentPlayers = new ArrayList<>();
            List<ServerCommunicationItem> tournamentTeams = new ArrayList<>();
            List<ServerCommunicationItem> tournamentMatches = new ArrayList<>();

            Log.d("IMPORT", "Tournament: " + tournament.syncData);
            Tournament importedTournament = TournamentSerializer.getInstance(context).deserialize(tournament);
            importedTournament.setCompetitionId(importedCompetition.getId());
            ManagerFactory.getInstance(context).tournamentManager.insert(importedTournament);

            for (ServerCommunicationItem subItem : tournament.subItems) {
                if (subItem.getType().equals("Player")) {
                    tournamentPlayers.add(subItem);
                } else if (subItem.getType().equals("Team")) {
                    tournamentTeams.add(subItem);
                } else if (subItem.getType().equals("Match")) {
                    tournamentMatches.add(subItem);
                }
            }

            /* Players loading */
            for (ServerCommunicationItem player : tournamentPlayers) {
                // Add player to tournament.
                long playerId = importedPlayers.get(player.getUid()).getId();
                ManagerFactory.getInstance(context).tournamentManager.addPlayer(playerId, importedTournament.getId());
            }

            /* Teams loading */
            Map<String, Team> importedTeams = new HashMap<>();
            for (ServerCommunicationItem team : tournamentTeams) {
                // Add team to tournament.
                Team importedTeam = TeamSerializer.getInstance(context).deserialize(team);
                importedTeam.setTournamentId(importedTournament.getId());
                ManagerFactory.getInstance(context).teamManager.insert(importedTeam);
                importedTeams.put(team.getUid(), importedTeam);

                // Add players to team.
                ArrayList<Player> teamPlayers = new ArrayList<>();
                for (ServerCommunicationItem teamPlayer : team.subItems) {
                    if (teamPlayer.getType().equals("Player")) {
                        teamPlayers.add(importedPlayers.get(teamPlayer.getUid()));
                    }
                }
                ManagerFactory.getInstance(context).teamManager.updatePlayersInTeam(importedTeam.getId(), teamPlayers);
            }

            /* Matches loading */
            MatchLoader.importMatches(context, tournamentMatches, importedTournament, importedCompetition, importedTeams, importedPlayers);
        }
    }
}
