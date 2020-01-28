package fit.cvut.org.cz.bowling.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.business.managers.FrameManager;
import fit.cvut.org.cz.bowling.business.managers.MatchManager;
import fit.cvut.org.cz.bowling.business.managers.ParticipantManager;
import fit.cvut.org.cz.bowling.business.managers.ParticipantStatManager;
import fit.cvut.org.cz.bowling.business.managers.PlayerStatManager;
import fit.cvut.org.cz.bowling.business.managers.RollManager;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IFrameManager;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IMatchManager;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IParticipantStatManager;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IPlayerStatManager;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IRollManager;
import fit.cvut.org.cz.bowling.data.entities.Frame;
import fit.cvut.org.cz.bowling.data.entities.Match;
import fit.cvut.org.cz.bowling.data.entities.ParticipantStat;
import fit.cvut.org.cz.bowling.data.entities.PlayerStat;
import fit.cvut.org.cz.bowling.data.entities.Roll;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManagerFactory;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IParticipantManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITeamManager;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.ParticipantType;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

/**
 * Bowling match service to handle intent/service/activity work in matches' scope
 */
public class MatchService extends AbstractIntentServiceWProgress {
    public static final String ACTION_FIND_BY_ID = "action_find_match_by_id";
    public static final String ACTION_FIND_BY_TOURNAMENT_ID = "action_find_match_by_tournament_id";
    public static final String ACTION_CREATE = "action_create_match";
    public static final String ACTION_DELETE = "action_delete_match";
    public static final String ACTION_RESET = "action_reset_match";
    public static final String ACTION_UPDATE = "action_update_match";
    public static final String ACTION_GENERATE_ROUND = "action_generate_round";
    public static final String ACTION_FIND_BY_ID_FOR_OVERVIEW = "action_find_match_for_match_overview";
    public static final String ACTION_UPDATE_FOR_OVERVIEW = "action_update_match_for_overview";
    public static final String ACTION_UPDATE_CASCADE = "action_update_cascade";
    public static final String ACTION_GENERATE_BY_LANES = "action_generate_by_lanes";

    public MatchService() {
        super("Bowling Match Service");
    }

    public static Intent newStartIntent(String action, Context context) {
        Intent res = new Intent(context, MatchService.class);
        res.putExtra(ExtraConstants.EXTRA_ACTION, action);
        return res;
    }

    @Override
    protected String getActionKey() {
        return ExtraConstants.EXTRA_ACTION;
    }

    @Override
    protected void doWork(Intent intent) {
        String action = intent.getStringExtra(ExtraConstants.EXTRA_ACTION);
        if (action == null)
            action = intent.getAction();

        switch (action) {
            case ACTION_CREATE: {
                fit.cvut.org.cz.tmlibrary.data.entities.Match m = intent.getParcelableExtra(ExtraConstants.EXTRA_MATCH);
                Match BowlingMatch = new Match(m);
                ManagerFactory.getInstance(this).getEntityManager(Match.class).insert(BowlingMatch);
                /*for (Participant participant : BowlingMatch.getParticipants()) {
                    Team team =ManagerFactory.getInstance(this).getEntityManager(Team.class).getById(participant.getParticipantId());
                    List<Player> teamPlayers = ((ITeamManager)ManagerFactory.getInstance(this).getEntityManager(Team.class)).getTeamPlayers(team);
                    for (Player player : teamPlayers) {
                        ManagerFactory.getInstance(this).getEntityManager(PlayerStat.class).insert(new PlayerStat(participant.getId(), player.getId()));
                    }
                }*/
                break;
            }
            case ACTION_UPDATE_CASCADE: {
                final Intent res = new Intent(action);
                final Match match = intent.getParcelableExtra(ExtraConstants.EXTRA_MATCH_WITH_RESULTS);

                // 1. Delete everything in the old match
                final IManagerFactory managerFactory = ManagerFactory.getInstance();
                final MatchManager matchManager = managerFactory.getEntityManager(Match.class);
                matchManager.resetMatch(match.getId()); //TODO be 100% sure this deletes all the participants, playerstats, participantstats, frames, rolls and etc.
                matchManager.deleteContents(match.getId());

                // 2. Save everything
                matchManager.update(match);

                // Participants
                final ParticipantManager participantManager = managerFactory.getEntityManager(Participant.class);
                final PlayerStatManager playerStatManager = managerFactory.getEntityManager(PlayerStat.class);
                final ParticipantStatManager participantStatManager = managerFactory.getEntityManager(ParticipantStat.class);
                final FrameManager frameManager = managerFactory.getEntityManager(Frame.class);
                final RollManager rollManager = managerFactory.getEntityManager(Roll.class);

                int i = 0;
                for(Participant participant : match.getParticipants()) {
                    participantManager.insert(participant);

                    ArrayList<ParticipantStat> participantStats = intent.getParcelableArrayListExtra(ExtraConstants.PARTICIPANT_STATS_TO_CREATE + i);
                    ArrayList<PlayerStat> playerStats = intent.getParcelableArrayListExtra(ExtraConstants.PLAYER_STATS_TO_CREATE + i);

                    if(participantStats == null) {
                        //Ouch
                        Log.e("MatchService", "Stats null");
                    } else {
                        for(ParticipantStat participantStat : participantStats) {
                            participantStat.setParticipantId(participant.getId());
                            participantStatManager.insert(participantStat);

                            for(Frame frame : participantStat.getFrames()) {
                                frame.setMatchId(match.getId());
                                frame.setParticipantId(participant.getId());
                                frameManager.insert(frame);

                                for(Roll roll : frame.getRolls()) {
                                    roll.setFrameId(frame.getId());
                                    rollManager.insert(roll);
                                }
                            }
                        }
                    }

                    if(playerStats == null) {
                        Log.e("MatchService", "PlayerStats null");
                    } else {
                        for(PlayerStat playerStat : playerStats) {
                            playerStat.setParticipantId(participant.getId());
                            playerStatManager.insert(playerStat);
                        }
                    }

                    i++;
                }

                break;
            }
            case ACTION_UPDATE_FOR_OVERVIEW: {
                Intent res = new Intent(action);
                Match match = intent.getParcelableExtra(ExtraConstants.EXTRA_MATCH_WITH_RESULTS);
                IManagerFactory managerFactory = ManagerFactory.getInstance(this);
                Match original = managerFactory.getEntityManager(Match.class).getById(match.getId());

                //need to update these variables, because they may be edited during ShowMatchActivity
                match.setDate(original.getDate());
                match.setNote(original.getNote());
                match.setPeriod(original.getPeriod());
                match.setRound(original.getRound());

                managerFactory.getEntityManager(Match.class).update(match);

                List<Participant> participants = ((IParticipantManager)ManagerFactory.getInstance(this).getEntityManager(Participant.class)).getByMatchId(match.getId());
                for (Participant participant : participants) {
                    // Remove original player stats and add new ones afterwards (way to handle removed items)
                    ((IPlayerStatManager)managerFactory.getEntityManager(PlayerStat.class)).deleteByParticipantId(participant.getId());
                }
                ArrayList<PlayerStat> playerStats = intent.getParcelableArrayListExtra(ExtraConstants.EXTRA_PLAYER_STATS);
                for (PlayerStat playerStat : playerStats)
                    managerFactory.getEntityManager(PlayerStat.class).insert(playerStat);

                Bundle matchResults = intent.getBundleExtra(ExtraConstants.EXTRA_MATCH_BUNDLE);

                List<ParticipantStat> participantStatsToCreate = matchResults.getParcelableArrayList(ExtraConstants.PARTICIPANT_STATS_TO_CREATE);
                List<ParticipantStat> participantStatsToUpdate = matchResults.getParcelableArrayList(ExtraConstants.PARTICIPANT_STATS_TO_UPDATE);
                IParticipantStatManager participantStatManager = managerFactory.getEntityManager(ParticipantStat.class);
                for(ParticipantStat participantStat : participantStatsToCreate){
                    participantStatManager.insert(participantStat);
                }
                for(ParticipantStat participantStat : participantStatsToUpdate){
                    participantStatManager.update(participantStat);
                }

                boolean isInputTypeChanged = intent.getBooleanExtra(ExtraConstants.EXTRA_BOOLEAN_IS_INPUT_TYPE_CHANGED, false);

                if(match.isTrackRolls()){
                    List<Frame> framesToCreate = matchResults.getParcelableArrayList(ExtraConstants.FRAMES_TO_CREATE);
                    List<Frame> framesToUpdate = matchResults.getParcelableArrayList(ExtraConstants.FRAMES_TO_UPDATE);
                    List<Frame> framesToDelete = matchResults.getParcelableArrayList(ExtraConstants.FRAMES_TO_DELETE);
                    IFrameManager frameManager = managerFactory.getEntityManager(Frame.class);
                    IRollManager rollManager = managerFactory.getEntityManager(Roll.class);
                    for(Frame frame : framesToCreate){
                        frameManager.insert(frame);
                    }
                    for(Frame frame : framesToUpdate){
                        frameManager.update(frame);
                    }
                    for(Frame frame : framesToDelete){
                        rollManager.deleteByFrameId(frame.getId());
                        frameManager.delete(frame.getId());
                    }

                    List<Roll> rollsToCreate = matchResults.getParcelableArrayList(ExtraConstants.ROLLS_TO_CREATE);
                    List<Roll> rollsToUpdate = matchResults.getParcelableArrayList(ExtraConstants.ROLLS_TO_UPDATE);
                    List<Roll> rollsToDelete = matchResults.getParcelableArrayList(ExtraConstants.ROLLS_TO_DELETE);
                    List<Frame> notChangedFramesButToAddRollsTo = matchResults.getParcelableArrayList(ExtraConstants.NOT_CHANGED_FRAMES_BUT_TO_ADD_ROLLS_TO);
                    List<Frame> framesToExist = new ArrayList<>();
                    framesToExist.addAll(framesToCreate);
                    framesToExist.addAll(framesToUpdate);
                    framesToExist.addAll(notChangedFramesButToAddRollsTo);
                    for(Roll roll : rollsToCreate){
                        for(Frame frame : framesToExist) {
                            if(roll.getId() == frame.getParticipantId() && roll.getFrameId() == frame.getFrameNumber()) {
                                roll.setId(0);
                                roll.setFrameId(frame.getId());
                                break;
                            }
                        }
                        if(roll.getId() == 0){
                            rollManager.insert(roll);
                        }
                    }
                    for(Roll roll : rollsToUpdate){
                        rollManager.update(roll);
                    }
                    for(Roll roll : rollsToDelete){
                        rollManager.delete(roll.getId());
                    }
                    
                } else if (isInputTypeChanged) {
                    IFrameManager frameManager = managerFactory.getEntityManager(Frame.class);
                    frameManager.deleteAllByMatchId(match.getId());
                }

                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
            case ACTION_FIND_BY_ID: {
                Intent res = new Intent(action);
                long matchId = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);
                long tourId = intent.getLongExtra(ExtraConstants.EXTRA_TOUR_ID, -1);
                //List<Team> tourTeams = ((ITeamManager)ManagerFactory.getInstance(this).getEntityManager(Team.class)).getByTournamentId(tourId);
                if (matchId != -1) {
                    Match m = ManagerFactory.getInstance(this).getEntityManager(Match.class).getById(matchId);
                    res.putExtra(ExtraConstants.EXTRA_MATCH, m);
                }

                ArrayList<Participant> participants = new ArrayList<>();
                /*for (Team team : tourTeams) {
                    Participant participant = new Participant(matchId, team.getId(), null);
                    participant.setName(team.getName());
                    participants.add(participant);
                }*/

                res.putParcelableArrayListExtra(ExtraConstants.EXTRA_PARTICIPANTS, participants);
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
            case ACTION_FIND_BY_TOURNAMENT_ID: {
                Intent res = new Intent(action);

                long tourId = intent.getLongExtra(ExtraConstants.EXTRA_TOUR_ID, -1);
                ArrayList<Match> matches = new ArrayList<>(((IMatchManager)ManagerFactory.getInstance(this).getEntityManager(Match.class)).getByTournamentId(tourId));
                for (Match m : matches) {
                    /* TODO create method for this */
                    // TODO consider previous TODO
                    List<Participant> participants = ((IParticipantManager)ManagerFactory.getInstance(this).getEntityManager(Participant.class)).getByMatchId(m.getId());
                    for (Participant p : participants) {
                        m.addParticipant(p);
                        int score = ((IParticipantStatManager)ManagerFactory.getInstance(this).getEntityManager(ParticipantStat.class)).getScoreByParticipantId(p.getId());
                        //Team t = ManagerFactory.getInstance(this).getEntityManager(Team.class).getById(p.getParticipantId());
                        if (ParticipantType.home.toString().equals(p.getRole())) {
                            m.setHomeScore(score);
                        }
                        else if (ParticipantType.away.toString().equals(p.getRole())) {
                            m.setAwayScore(score);
                        }
                    }
                }
                res.putParcelableArrayListExtra(ExtraConstants.EXTRA_MATCHES, matches);

                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
            case ACTION_GENERATE_ROUND: {
                Intent res = new Intent(action);
                long tourId = intent.getLongExtra(ExtraConstants.EXTRA_TOUR_ID, -1);

                ((IMatchManager)ManagerFactory.getInstance(this).getEntityManager(Match.class)).generateRound(tourId);

                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
            case ACTION_FIND_BY_ID_FOR_OVERVIEW: {
                Intent res = new Intent(action);
                long matchId = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);

                Match match = ManagerFactory.getInstance(this).getEntityManager(Match.class).getById(matchId);
                /* TODO create method for this */
                List<Participant> participants = ((IParticipantManager)ManagerFactory.getInstance(this).getEntityManager(Participant.class)).getByMatchId(match.getId());
                for (Participant participant : participants) {
                    match.addParticipant(participant);
                    int score = ((IParticipantStatManager)ManagerFactory.getInstance(this).getEntityManager(ParticipantStat.class)).getScoreByParticipantId(participant.getId());
                    //Team team = ManagerFactory.getInstance(this).getEntityManager(Team.class).getById(participant.getParticipantId());
                    if (ParticipantType.home.toString().equals(participant.getRole())) {
                        match.setHomeScore(score);
                    }
                    else if (ParticipantType.away.toString().equals(participant.getRole())) {
                        match.setAwayScore(score);
                    }
                }
                res.putExtra(ExtraConstants.EXTRA_MATCH, match);

                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
            case ACTION_DELETE: {
                Intent res = new Intent(action);
                long matchId = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);

                ManagerFactory.getInstance(this).getEntityManager(Match.class).delete(matchId);
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
            case ACTION_RESET: {
                Intent res = new Intent(action);
                long matchId = intent.getLongExtra(ExtraConstants.EXTRA_ID, -1);

                ((IMatchManager)ManagerFactory.getInstance(this).getEntityManager(Match.class)).resetMatch(matchId);
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
            case ACTION_UPDATE: {
                Intent res = new Intent(action);
                Match match = intent.getParcelableExtra(ExtraConstants.EXTRA_MATCH);
                Match originalMatch = ManagerFactory.getInstance(this).getEntityManager(Match.class).getById(match.getId());
                match.setPlayed(originalMatch.isPlayed());

                ManagerFactory.getInstance(this).getEntityManager(Match.class).update(match);
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
            case ACTION_GENERATE_BY_LANES: {
                Intent res = new Intent(action);
                long tourId = intent.getLongExtra(ExtraConstants.EXTRA_TOUR_ID, -1);
                int lanes = intent.getIntExtra(ExtraConstants.EXTRA_LANES, 0);
                ((IMatchManager)ManagerFactory.getInstance(this).getEntityManager(Match.class)).generateByLanes(tourId,lanes);
                LocalBroadcastManager.getInstance(this).sendBroadcast(res);
                break;
            }
        }
    }
}
