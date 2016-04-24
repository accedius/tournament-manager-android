package fit.cvut.org.cz.hockey.business.managers;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.business.entities.AgregatedStatistics;
import fit.cvut.org.cz.hockey.business.entities.MatchPlayerStatistic;
import fit.cvut.org.cz.hockey.business.entities.MatchScore;
import fit.cvut.org.cz.hockey.business.entities.Standing;
import fit.cvut.org.cz.hockey.data.DAOFactory;
import fit.cvut.org.cz.hockey.data.StatsEnum;
import fit.cvut.org.cz.hockey.data.entities.DMatchStat;
import fit.cvut.org.cz.hockey.data.entities.DPointConfiguration;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.ParticipantType;
import fit.cvut.org.cz.tmlibrary.data.entities.DParticipant;
import fit.cvut.org.cz.tmlibrary.data.entities.DPlayer;
import fit.cvut.org.cz.tmlibrary.data.entities.DStat;

/**
 * Created by atgot_000 on 8. 4. 2016.
 */
public class StatisticsManager {

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

    private AgregatedStatistics agregateStats( Context context, long plId, String pName,  long tourId, ArrayList<DStat> allStats )
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

//        long tournamentId = DAOFactory.getInstance().matchDAO.getById( context, score.getMatchId() ).getTournamentId();
//        long competitionId = DAOFactory.getInstance().tournamentDAO.getById( context, tournamentId ).getCompetitionId();
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

    public MatchPlayerStatistic getPlayerStatsInMatch( Context context, long playerId, long matchId )
    {
        MatchPlayerStatistic res = new MatchPlayerStatistic();
        ArrayList<DParticipant> participants = DAOFactory.getInstance().participantDAO.getParticipantsByMatchId( context, matchId );
        long partId = -1;
        for(DParticipant dParticipant : participants){
            if( DAOFactory.getInstance().packagePlayerDAO.getPlayerIdsByParticipant( context, dParticipant.getId()).contains( playerId ) ){
                partId = dParticipant.getId();
                break;
            }
        }
        if( partId == -1 ) return null;

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


}
