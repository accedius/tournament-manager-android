package fit.cvut.org.cz.hockey.business.managers;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.business.entities.AggregatedStatistics;
import fit.cvut.org.cz.hockey.business.entities.MatchPlayerStatistic;
import fit.cvut.org.cz.hockey.business.entities.MatchScore;
import fit.cvut.org.cz.hockey.business.entities.Standing;
import fit.cvut.org.cz.hockey.business.interfaces.IHockeyStatisticsManager;
import fit.cvut.org.cz.hockey.data.DAOFactory;
import fit.cvut.org.cz.hockey.data.StatsEnum;
import fit.cvut.org.cz.hockey.data.entities.DMatchStat;
import fit.cvut.org.cz.hockey.data.entities.DPointConfiguration;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.ParticipantType;
import fit.cvut.org.cz.tmlibrary.data.entities.DMatch;
import fit.cvut.org.cz.tmlibrary.data.entities.DParticipant;
import fit.cvut.org.cz.tmlibrary.data.entities.DPlayer;
import fit.cvut.org.cz.tmlibrary.data.entities.DStat;

/**
 * Created by atgot_000 on 8. 4. 2016.
 */
public class StatisticsManager implements IHockeyStatisticsManager {

    private long calculatePoints( int outcome, DPointConfiguration pointConfig, DMatchStat matchStat )
    {
        switch (outcome)
        {
            case 1:{
                if(matchStat.isShootouts())
                    return pointConfig.soW;
                if(matchStat.isOvertime())
                    return pointConfig.otW;
                return pointConfig.ntW;
            }
            case 2:{
                if(matchStat.isOvertime())
                    return pointConfig.otD;
                return pointConfig.ntD;
            }
            case 3:{
                if(matchStat.isShootouts())
                    return pointConfig.soL;
                if(matchStat.isOvertime())
                    return pointConfig.otL;
                return pointConfig.ntL;
            }
        }
        return 0;
    }

    private AggregatedStatistics agregateStats( Context context, long plId, String pName, ArrayList<DStat> allStats )
    {
        ArrayList<DStat> playerStats = DAOFactory.getInstance().statDAO.getStatsByPlayerId( context, plId );
        if( allStats != null ) {
            playerStats.retainAll(allStats); //common elements -> players stats in competition
        }
        long matches = 0, wins = 0, draws = 0, losses = 0, goals = 0, assists = 0, plusMinusPoints = 0, teamPoints = 0, interventions = 0;
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
                case interventions:
                {
                    interventions += value;
                    break;
                }
                case outcome:
                {
                    DPointConfiguration pointConfiguration = DAOFactory.getInstance().pointConfigDAO.getByTournamentId( context, stat.getTournamentId() );
                    DParticipant participant = DAOFactory.getInstance().participantDAO.getById( context, stat.getParticipantId() );
                    DMatchStat matchStat = DAOFactory.getInstance().matchStatisticsDAO.getByMatchId( context, participant.getMatchId() );

                    teamPoints += calculatePoints( (int)value, pointConfiguration, matchStat );
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

        return new AggregatedStatistics(plId, pName, matches, wins, draws, losses, goals, assists, plusMinusPoints, teamPoints, interventions);
    }

    public ArrayList<AggregatedStatistics> getAllAgregated( Context context )
    {
        ArrayList<DStat> allStats = null;
        ArrayList<Player> players = ManagerFactory.getInstance().packagePlayerManager.getAllPlayers( context );

        ArrayList<AggregatedStatistics> res = new ArrayList<>();

        for( Player p : players ){
            res.add( agregateStats(context, p.getId(), p.getName(), allStats) );
        }
        return res;
    }


    public ArrayList<AggregatedStatistics> getByCompetitionID( Context context, long compId )
    {
        ArrayList<Player> compPlayers = ManagerFactory.getInstance().packagePlayerManager.getPlayersByCompetition( context, compId );

        ArrayList<AggregatedStatistics> res = new ArrayList<>();

        ArrayList<DStat> competitionStats = DAOFactory.getInstance().statDAO.getStatsByCompetitionId( context, compId );

        for( Player p : compPlayers )
        {
            res.add( agregateStats(context, p.getId(), p.getName(), competitionStats) );
        }

        return res;
    }

    public ArrayList<AggregatedStatistics> getByTournamentID( Context context, long tourId )
    {
        ArrayList<Player> tourPlayers = ManagerFactory.getInstance().packagePlayerManager.getPlayersByTournament(context, tourId);

        ArrayList<AggregatedStatistics> res = new ArrayList<>();

        ArrayList<DStat> tournamentStats = DAOFactory.getInstance().statDAO.getStatsByTournamentId(context, tourId);

        for( Player p : tourPlayers )
        {
            res.add( agregateStats(context, p.getId(), p.getName(),  tournamentStats) );
        }

        return res;
    }

    public ArrayList<Standing> getStandingsByTournamentId( Context context, long tourId)
    {
        ArrayList<Team> teams = ManagerFactory.getInstance().teamManager.getByTournamentId( context, tourId );

        ArrayList<Standing> standings = new ArrayList<>();

        DPointConfiguration pointConfiguration = DAOFactory.getInstance().pointConfigDAO.getByTournamentId( context, tourId );

        for( Team t : teams )
        {
            standings.add( new Standing(t.getName(), 0L, 0L, 0L, 0L, 0L, 0L, t.getId()));
        }
        ArrayList<DMatch> matches = DAOFactory.getInstance().matchDAO.getByTournamentId( context, tourId );
        for( DMatch dMatch : matches )
        {
            if(!dMatch.isPlayed()) continue;
            ScoredMatch match = ManagerFactory.getInstance().matchManager.getById( context, dMatch.getId() );
            Standing standingH = null;
            Standing standingA = null;
            for( Standing s : standings ){
                if( s.getTeamId() == match.getHomeParticipantId() ) standingH = s;
                else if ( s.getTeamId() == match.getAwayParticipantId() ) standingA = s;
            }
            if(standingA == null || standingH == null)continue;
            DMatchStat matchStat = DAOFactory.getInstance().matchStatisticsDAO.getByMatchId( context, match.getId() );
            int homeOutcome, awayOutcome;
            if( match.getHomeScore() > match.getAwayScore() ) {
                homeOutcome = 1;
                standingH.addWin();
                awayOutcome = 3;
                standingA.addLoss();
            }
            else if (match.getHomeScore() == match.getAwayScore()){
                homeOutcome = 2;
                standingH.addDraw();
                awayOutcome = 2;
                standingA.addDraw();
            }
            else{
                homeOutcome = 3;
                standingH.addLoss();
                awayOutcome = 1;
                standingA.addWin();
            }
            standingH.addPoints( calculatePoints( homeOutcome, pointConfiguration, matchStat ) );
            standingA.addPoints( calculatePoints( awayOutcome, pointConfiguration, matchStat ) );

            standingH.addGoalsGiven( match.getHomeScore() );
            standingA.addGoalsGiven( match.getAwayScore() );

            standingH.addGoalsReceived( match.getAwayScore() );
            standingA.addGoalsReceived( match.getHomeScore() );

        }

        Collections.sort(standings, new Comparator<Standing>() {
            @Override
            public int compare(Standing lhs, Standing rhs) {
                return (int)(rhs.getPoints() - lhs.getPoints());
            }
        });
        return standings;
    }
    public MatchScore getMatchScoreByMatchId( Context context, long id )
    {
        DMatchStat stat = DAOFactory.getInstance().matchStatisticsDAO.getByMatchId(context, id);
        if( stat == null )return null;
        MatchScore score = new MatchScore();
        score.setMatchId( stat.getMatchId() );
        score.setShootouts( stat.isShootouts() );
        score.setOvertime(stat.isOvertime());
        return score;
    }

    public void setMatchScoreByMatchId( Context context, long id, MatchScore score )
    {
        DMatchStat matchStat = new DMatchStat( id, score.isOvertime(), score.isShootouts() );
        DAOFactory.getInstance().matchStatisticsDAO.update( context, matchStat );

        int homeOutcome = 0, awayOutcome = 0;
        if( score.getHomeScore() > score.getAwayScore() ) {
            homeOutcome = 1;
            awayOutcome = 3;
        }
        else if (score.getHomeScore() == score.getAwayScore()){
            homeOutcome = 2;
            awayOutcome = 2;
        }
        else{
            homeOutcome = 3;
            awayOutcome = 1;
        }
        ArrayList<DParticipant> participants = DAOFactory.getInstance().participantDAO.getParticipantsByMatchId(context, id);
        DStat homeScoreStat = new DStat();
        DStat awayScoreStat = new DStat();
        for( DParticipant dp : participants ) {
            ArrayList<DStat> stats = DAOFactory.getInstance().statDAO.getStatsByParticipantId( context, dp.getId() );
            if( dp.getRole().equals(ParticipantType.home.toString())){
                for( DStat ds : stats ) if( StatsEnum.team_goals.toString().equals(ds.getStatsEnumId()) ) {homeScoreStat = ds; break;}
                homeScoreStat.setValue( Integer.toString( score.getHomeScore() ) );
                giveOutcome( context, homeOutcome, stats );
            }
            else if ( dp.getRole().equals(ParticipantType.away.toString())){
                for( DStat ds : stats ) if( StatsEnum.team_goals.toString().equals(ds.getStatsEnumId()) ) {awayScoreStat = ds; break;}
                awayScoreStat.setValue( Integer.toString( score.getAwayScore() ) );
                giveOutcome( context, awayOutcome, stats );
            }
        }
        DAOFactory.getInstance().statDAO.update(context, homeScoreStat);
        DAOFactory.getInstance().statDAO.update(context, awayScoreStat);
    }

    private void giveOutcome( Context context, int outcome, ArrayList<DStat> participantStats )
    {
        for( DStat stat : participantStats )
        {
            if(StatsEnum.outcome.toString().equals(stat.getStatsEnumId()))
            {
                stat.setValue( String.valueOf(outcome));
                DAOFactory.getInstance().statDAO.update( context, stat );
            }
        }
    }

    private long getParticipantIdByPlayerAndMatch( Context context, long playerId, long matchId )
    {
        ArrayList<DParticipant> participants = DAOFactory.getInstance().participantDAO.getParticipantsByMatchId(context, matchId);
        long partId = -1;
        for(DParticipant dParticipant : participants){
            if( DAOFactory.getInstance().packagePlayerDAO.getPlayerIdsByParticipant( context, dParticipant.getId()).contains( playerId ) ){
                partId = dParticipant.getId();
                break;
            }
        }
        return partId;
    }

    public MatchPlayerStatistic getPlayerStatsInMatch( Context context, long playerId, long matchId )
    {
        MatchPlayerStatistic res = new MatchPlayerStatistic();

        long partId = getParticipantIdByPlayerAndMatch( context, playerId, matchId );

        ArrayList<Player> players = ManagerFactory.getInstance().packagePlayerManager.getPlayersByParticipant( context, partId );
        Player curPlayer = null;
        for( Player p : players ) if( p.getId() == playerId ) curPlayer = p;
        if( curPlayer == null ) return null;

        ArrayList<DStat> partStats = DAOFactory.getInstance().statDAO.getStatsByParticipantId( context, partId );
        res.setPlayerId(playerId);
        res.setName( curPlayer.getName() );

        for( DStat stat : partStats )
        {
            if( stat.getPlayerId() != playerId ) continue;
            int value = Integer.parseInt(stat.getValue());
            switch (StatsEnum.valueOf(stat.getStatsEnumId()))
            {
                case goals:
                    res.setGoals( value );
                    break;
                case assists:
                    res.setAssists( value );
                    break;
                case plus_minus_points:
                    res.setPlusMinusPoints( value );
                    break;
                case interventions:
                    res.setInterventions( value );
                    break;
                default: break;
            }
        }
        return res;
    }

    public void updatePlayersInMatch( Context context, long matchId, ParticipantType parType, ArrayList<Long> playerIds )
    {
        ArrayList<DParticipant> participants = DAOFactory.getInstance().participantDAO.getParticipantsByMatchId( context, matchId);
        DParticipant currentPart = null;
        for( DParticipant part : participants ) {
            if(part.getRole().equals( parType.toString() )){
                currentPart = part;
                break;
            }
        }
        if( currentPart == null ) return;

        Map<Long, DPlayer> allPlayers = DAOFactory.getInstance().packagePlayerDAO.getAllPlayers(context);
        ArrayList<Player> playerListToUpdate = new ArrayList<>();
        for (Long pId : playerIds) {
            playerListToUpdate.add( new Player(allPlayers.get( pId )) );
        }

        long tourId = DAOFactory.getInstance().matchDAO.getById( context, matchId ).getTournamentId();
        long compId = DAOFactory.getInstance().tournamentDAO.getById(context, tourId).getCompetitionId();

        ManagerFactory.getInstance().packagePlayerManager.updatePlayersInParticipant( context, currentPart.getId(), compId, tourId, playerListToUpdate );
    }

    public void updatePlayerStatsInMatch( Context context, MatchPlayerStatistic statistic, long matchId )
    {
        long partId = getParticipantIdByPlayerAndMatch( context, statistic.getPlayerId(), matchId );
        ArrayList<DStat> participantStats = DAOFactory.getInstance().statDAO.getStatsByParticipantId(context, partId);

        for( DStat stat : participantStats )
        {
            if( stat.getPlayerId() != statistic.getPlayerId() ) continue;
            //int value = Integer.parseInt(stat.getValue());
            switch (StatsEnum.valueOf(stat.getStatsEnumId()))
            {
                case goals:
                    stat.setValue(String.valueOf(statistic.getGoals()));
                    break;
                case assists:
                    stat.setValue(String.valueOf(statistic.getAssists()));
                    break;
                case plus_minus_points:
                    stat.setValue(String.valueOf(statistic.getPlusMinusPoints()));
                    break;
                case interventions:
                    stat.setValue(String.valueOf(statistic.getInterventions()));
                    break;
                default: break;
            }
            DAOFactory.getInstance().statDAO.update( context, stat );
        }
    }


}
