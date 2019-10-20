package fit.cvut.org.cz.bowling.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IMatchManager;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IParticipantStatManager;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IPlayerStatManager;
import fit.cvut.org.cz.bowling.data.entities.Match;
import fit.cvut.org.cz.bowling.data.entities.ParticipantStat;
import fit.cvut.org.cz.bowling.data.entities.PlayerStat;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
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
            case ACTION_UPDATE_FOR_OVERVIEW: {
                Intent res = new Intent(action);
                Match match = intent.getParcelableExtra(ExtraConstants.EXTRA_MATCH_SCORE);
                Match original = ManagerFactory.getInstance(this).getEntityManager(Match.class).getById(match.getId());
                if (!original.isPlayed()) {
                    ((IMatchManager)ManagerFactory.getInstance(this).getEntityManager(Match.class)).beginMatch(match.getId());
                }
                match.setTournamentId(original.getTournamentId());
                match.setDate(original.getDate());
                match.setNote(original.getNote());
                match.setPeriod(original.getPeriod());
                match.setRound(original.getRound());
                ManagerFactory.getInstance(this).getEntityManager(Match.class).update(match);
                List<Participant> participants = ((IParticipantManager)ManagerFactory.getInstance(this).getEntityManager(Participant.class)).getByMatchId(match.getId());
                for (Participant participant : participants) {
                    int score = ParticipantType.home.toString().equals(participant.getRole()) ? match.getHomeScore() : match.getAwayScore();
                    List<ParticipantStat> stats = ((IParticipantStatManager)ManagerFactory.getInstance(this).getEntityManager(ParticipantStat.class)).getByParticipantId(participant.getId());
                    ParticipantStat stat;
                    if (stats.isEmpty()) {
                        stat = new ParticipantStat(participant.getId(), score);
                        ManagerFactory.getInstance(this).getEntityManager(ParticipantStat.class).insert(stat);
                    } else {
                        stat = stats.get(0);
                        stat.setScore(score);
                        ManagerFactory.getInstance(this).getEntityManager(ParticipantStat.class).update(stat);
                    }

                    // Remove original stats and add new ones (way to handle removed items)
                    ((IPlayerStatManager)ManagerFactory.getInstance(this).getEntityManager(PlayerStat.class)).deleteByParticipantId(participant.getId());
                }

                ArrayList<PlayerStat> homeStats = intent.getParcelableArrayListExtra(ExtraConstants.EXTRA_HOME_STATS);

                for (PlayerStat playerStat : homeStats)
                    ManagerFactory.getInstance(this).getEntityManager(PlayerStat.class).insert(playerStat);

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
        }
    }
}
