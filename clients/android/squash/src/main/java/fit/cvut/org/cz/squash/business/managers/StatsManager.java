package fit.cvut.org.cz.squash.business.managers;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import fit.cvut.org.cz.squash.business.ManagersFactory;
import fit.cvut.org.cz.squash.business.entities.AgregatedStats;
import fit.cvut.org.cz.squash.business.entities.SetRowItem;
import fit.cvut.org.cz.squash.business.entities.StandingItem;
import fit.cvut.org.cz.squash.business.interfaces.IStatsManager;
import fit.cvut.org.cz.squash.data.DAOFactory;
import fit.cvut.org.cz.squash.data.entities.DStat;
import fit.cvut.org.cz.squash.data.entities.StatsEnum;
import fit.cvut.org.cz.tmlibrary.business.CompetitionType;
import fit.cvut.org.cz.tmlibrary.business.entities.Match;
import fit.cvut.org.cz.tmlibrary.business.entities.Participant;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.entities.DMatch;
import fit.cvut.org.cz.tmlibrary.data.entities.DParticipant;

/**
 * Created by Vaclav on 7. 4. 2016.
 */
public class StatsManager implements IStatsManager {
    @Override
    public ArrayList<AgregatedStats> getAgregatedStatsByCompetitionId(Context context, long competitionId) {

        //TODO remove mock data

        ArrayList<Player> players = ManagersFactory.getInstance().playerManager.getPlayersByCompetition(context, competitionId);
        ArrayList<AgregatedStats> stats = new ArrayList<>();
        for (Player player : players) stats.add(new AgregatedStats(player.getId(), player.getName(), 63, 42, 1, 150, 90, 999999, 999999, 2.1d, 1.3d, 54.3f, 33.12d, 61.23f, 72.5d));

        return stats;
    }

    @Override
    public ArrayList<AgregatedStats> getAgregatedStatsByTournamentId(Context context, long tournamentId) {
        ArrayList<Player> players = ManagersFactory.getInstance().playerManager.getPlayersByTournament(context, tournamentId);
        ArrayList<AgregatedStats> stats = new ArrayList<>();
        for (Player player : players) stats.add(new AgregatedStats(player.getId(), player.getName(), 63, 42, 1, 150, 90, 999999, 999999, 2.1d, 1.3d, 54.3f, 33.12d, 61.23f, 72.5d));

        return stats;
    }

    @Override
    public ArrayList<StandingItem> getStandingsByTournament(Context context, long tournamentId) {

        Tournament t = ManagersFactory.getInstance().tournamentManager.getById(context, tournamentId);
        CompetitionType type = ManagersFactory.getInstance().competitionManager.getById(context, t.getCompetitionId()).getType();

        ArrayList<StandingItem> standings = new ArrayList<>();

        if (type == CompetitionType.Individuals){

            ArrayList<Player> players = ManagersFactory.getInstance().playerManager.getPlayersByTournament(context, tournamentId);

            for (Player p : players) standings.add(new StandingItem(p.getName(), 3, 1, 0, 12, 6, 9));
        } else {
            //team competition
            ArrayList<Team> teams = ManagersFactory.getInstance().teamsManager.getByTournamentId(context, tournamentId);
            for (Team team : teams) standings.add(new StandingItem(team.getName(), 2, 1, 2, 6, 5, 7));
        }
        Collections.sort(standings, new Comparator<StandingItem>() {
            @Override
            public int compare(StandingItem lhs, StandingItem rhs) {
                return lhs.getPoints() - rhs.getPoints();
            }
        });
        return standings;
    }

    @Override
    public ArrayList<SetRowItem> getSetsForMatch(Context context, long matchId) {
        ArrayList<SetRowItem> sets = new ArrayList<>();

        ArrayList<DParticipant> dparticipants = DAOFactory.getInstance().participantDAO.getParticipantsByMatchId(context, matchId);
        DParticipant home = null, away = null;

        for (DParticipant participant : dparticipants){
            if (participant.getRole().equals("home")) home = participant;
            else away = participant;
        }

        ArrayList<DStat> homeSets = DAOFactory.getInstance().statDAO.getByParticipant(context, home.getId(), StatsEnum.SET);
        ArrayList<DStat> awaySets = DAOFactory.getInstance().statDAO.getByParticipant(context, away.getId(), StatsEnum.SET);

        for (int i = 0; i< homeSets.size(); i++){
            SetRowItem item = new SetRowItem();
            item.setHomeScore(homeSets.get(i).getValue());
            item.setAwayScore(awaySets.get(i).getValue());
            item.setWinner(homeSets.get(i).getStatus());

            sets.add(item);
        }

        return sets;
    }

    @Override
    public void updateStatsForMatch(Context context, long matchId, ArrayList<SetRowItem> sets) {

        DMatch m = DAOFactory.getInstance().matchDAO.getById(context, matchId);
        m.setPlayed(true);
        DAOFactory.getInstance().matchDAO.update(context, m);

        ArrayList<DParticipant> dparticipants = DAOFactory.getInstance().participantDAO.getParticipantsByMatchId(context, matchId);
        DParticipant home = null, away = null;

        for (DParticipant participant : dparticipants){
            DAOFactory.getInstance().statDAO.delete(context, participant.getId(), StatsEnum.SET);
            DAOFactory.getInstance().statDAO.delete(context, participant.getId(), StatsEnum.MATCH);
            if (participant.getRole().equals("home")) home = participant;
            else away = participant;
        }

        Tournament t = ManagersFactory.getInstance().tournamentManager.getById(context, m.getTournamentId());

        int setswon = 0;

        for (int i = 0; i< sets.size(); i++) {
            SetRowItem item = sets.get(i);
            DAOFactory.getInstance().statDAO.insert(context, new DStat(-1, t.getCompetitionId(), t.getId(), -1, home.getId(),
                    item.getWinner(), i + 1, item.getHomeScore(), StatsEnum.SET));
            DAOFactory.getInstance().statDAO.insert(context, new DStat(-1, t.getCompetitionId(), t.getId(), -1, away.getId(),
                    item.getWinner() * -1, i + 1, item.getAwayScore(), StatsEnum.SET));
            setswon += item.getWinner();
        }
        int result = 0;
        if (setswon > 0) result = 1;
        if (setswon < 0) result = -1;
        DAOFactory.getInstance().statDAO.insert(context, new DStat(-1, t.getCompetitionId(), t.getId(), -1, home.getId(),
                result, -1, -1, StatsEnum.MATCH));
        DAOFactory.getInstance().statDAO.insert(context, new DStat(-1, t.getCompetitionId(), t.getId(), -1, away.getId(),
                result * -1, -1, -1, StatsEnum.MATCH));
    }
}
