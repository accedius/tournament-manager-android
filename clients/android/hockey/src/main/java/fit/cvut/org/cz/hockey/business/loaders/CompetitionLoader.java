package fit.cvut.org.cz.hockey.business.loaders;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.business.serialization.CompetitionSerializer;
import fit.cvut.org.cz.tmlibrary.business.helpers.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.business.helpers.DateFormatter;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.CompetitionImportInfo;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.Conflict;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.ImportInfo;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.PlayerImportInfo;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.TournamentImportInfo;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICompetitionManager;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;

/**
 * Created by kevin on 13.12.2016.
 */

public class CompetitionLoader {
    public static ImportInfo getImportInfo(Context context, ServerCommunicationItem competition, List<TournamentImportInfo> tournamentsInfo,
                                           List<PlayerImportInfo> playersInfo, List<Conflict> playersModified) {
        Competition importedCompetition = CompetitionSerializer.getInstance(context).deserialize(competition);
        ImportInfo competitionInfo = new CompetitionImportInfo(importedCompetition.getName(), CompetitionTypes.teams());

        List<ServerCommunicationItem> allSubItems = competition.subItems;
        List<ServerCommunicationItem> players = new ArrayList<>();
        List<ServerCommunicationItem> tournaments = new ArrayList<>();

        for (ServerCommunicationItem subItem : allSubItems) {
            if (subItem.getType().equals("Player")) {
                players.add(subItem);
            } else if (subItem.getType().equals("Tournament")) {
                tournaments.add(subItem);
            }
        }

        /* TOURNAMENTS HANDLING */
        tournamentsInfo.addAll(TournamentLoader.getTournamentsImportInfo(context, tournaments));

        /* PLAYERS HANDLING */
        playersInfo.addAll(PlayerLoader.getPlayersImportInfo(context, players, playersModified));
        return competitionInfo;
    }

    public static Competition importCompetition(Context context, ServerCommunicationItem competition, Map<String, String> conflictSolutions) {
        Competition importedCompetition = CompetitionSerializer.getInstance(context).deserialize(competition);
        importedCompetition.setName(importedCompetition.getName()+" "+ DateFormatter.getInstance().getDBDateTimeFormat().format(new Date()));
        ((ICompetitionManager)ManagerFactory.getInstance((context)).getEntityManager(Competition.class)).insert(importedCompetition);

        List<ServerCommunicationItem> players = new ArrayList<>();
        List<ServerCommunicationItem> tournaments = new ArrayList<>();
        for (ServerCommunicationItem subItem : competition.subItems) {
            if (subItem.getType().equals("Player")) {
                players.add(subItem);
            } else if (subItem.getType().equals("Tournament")) {
                tournaments.add(subItem);
            }
        }

        /* Players loading
            - add if not exists
            - add to competition
            - create HashMap<uid, player> */
        Map<String, Player> importedPlayers = new HashMap<>();
        PlayerLoader.importPlayers(context, players, importedCompetition, importedPlayers, conflictSolutions);

        /* Tournaments loading */
        TournamentLoader.importTournaments(context, tournaments, importedCompetition, importedPlayers);
        return importedCompetition;
    }
}
