package fit.cvut.org.cz.hockey.business.managers;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.business.entities.AgregatedStatistics;
import fit.cvut.org.cz.hockey.business.entities.MatchScore;
import fit.cvut.org.cz.hockey.business.entities.Standing;
import fit.cvut.org.cz.hockey.data.DAOFactory;
import fit.cvut.org.cz.hockey.data.StatsEnum;
import fit.cvut.org.cz.hockey.data.entities.DMatchStat;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.DStat;

/**
 * Created by atgot_000 on 8. 4. 2016.
 */
public class StatisticsManager {

    private AgregatedStatistics agregateStats( Context context, long plId, String pName,  long compId, ArrayList<DStat> allStats )
    {
        ArrayList<DStat> playerStats = DAOFactory.getInstance().statDAO.getStatsByPlayerId( context, plId );
        playerStats.retainAll( allStats ); //common elements -> players stats in competition
        long matches = 0, wins = 0, draws = 0, losses = 0, goals = 0, assists = 0, plusMinusPoints = 0, teamPoints = 0;
        for(DStat stat : playerStats)
        {
            long value = Long.parseLong(stat.getValue());
            switch (StatsEnum.valueOf(stat.getStatsEnumId()))
            {
                case goals:
                {
                    goals += value;
                    break;
                }
                case participates:
                {
                    matches++;
                    break;
                }
                case assists:
                {
                    assists += value;
                    break;
                }
                case plus_minus_points:
                {
                    plusMinusPoints += value;
                    break;
                }
                case team_points:
                {
                    teamPoints += value;
                    break;
                }
                case outcome:
                {
                    switch ((int)value)
                    {
                        case 1: {
                            wins++;
                            break;
                        }
                        case 2: {
                            draws++;
                            break;
                        }
                        case 3: {
                            losses++;
                            break;
                        }
                        default: break;
                    }
                }
            }
        }

        return new AgregatedStatistics(plId, pName, matches, wins, draws, losses, goals, assists, plusMinusPoints, teamPoints);
    }


    public ArrayList<AgregatedStatistics> getByCompetitionID( Context context, long compId )
    {
        ArrayList<Player> compPlayers = ManagerFactory.getInstance().packagePlayerManager.getPlayersByCompetition( context, compId );

        ArrayList<AgregatedStatistics> res = new ArrayList<>();

        ArrayList<DStat> competitionStats = DAOFactory.getInstance().statDAO.getStatsByCompetitionId( context, compId );

        for( Player p : compPlayers )
        {
            res.add( agregateStats(context, p.getId(), p.getName(), compId, competitionStats) );
        }

        return res;
    }

    public ArrayList<AgregatedStatistics> getByTournamentID( Context context, long tourId )
    {
        ArrayList<Player> tourPlayers = ManagerFactory.getInstance().packagePlayerManager.getPlayersByTournament(context, tourId);

        ArrayList<AgregatedStatistics> res = new ArrayList<>();

        ArrayList<DStat> tournamentStats = DAOFactory.getInstance().statDAO.getStatsByTournamentId(context, tourId);

        for( Player p : tourPlayers )
        {
            res.add( agregateStats(context, p.getId(), p.getName(), tourId, tournamentStats) );
        }

        return res;
    }

    public ArrayList<Standing> getStandingsByTournamentId( Context context, long tourId)
    {
        //TODO remove mock
        ArrayList<Team> teams = ManagerFactory.getInstance().teamManager.getByTournamentId( context, tourId );

        ArrayList<Standing> standings = new ArrayList<>();

        for( Team t : teams )
        {
            standings.add( new Standing(t.getName(), 1L, 1L, 1L, 1L, 1L, 1L));
        }

        Collections.sort(standings, new Comparator<Standing>() {
            @Override
            public int compare(Standing lhs, Standing rhs) {
                return (int)(lhs.getPoints() - rhs.getPoints());
            }
        });
        return standings;
    }
    public MatchScore getMatchScoreByMatchId( Context context, long id )
    {
        DMatchStat stat = DAOFactory.getInstance().matchStatisticsDAO.getByMatchId( context, id);
        MatchScore score = new MatchScore();
        score.setMatchId( stat.getMatchId() );
        score.setShootouts( stat.isShootouts() );
        score.setOvertime( stat.isOvertime() );
        return score;
    }


}
