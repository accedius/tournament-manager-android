package fit.cvut.org.cz.squash.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.squash.business.ManagerFactory;
import fit.cvut.org.cz.squash.business.entities.Match;
import fit.cvut.org.cz.squash.business.entities.SetRowItem;
import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.entities.Participant;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.PlayerStat;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.business.enums.CompetitionType;
import fit.cvut.org.cz.tmlibrary.business.enums.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.data.ParticipantType;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

/**
 * Handles request belonging to Matches
 * Created by Vaclav on 3. 4. 2016.
 */
public class MatchService extends AbstractIntentServiceWProgress {
    public MatchService() {
        super("Squash Match Service");
    }

    private static final String EXTRA_ACTION = "extra_action";
    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_MATCH_ID = "extra_match_id";
    public static final String EXTRA_MATCHES = "extra_matches";
    public static final String EXTRA_MATCH = "extra_match";
    public static final String EXTRA_SETS = "extra_sets";
    public static final String EXTRA_TYPE = "extra_type";
    public static final String EXTRA_PARTICIPANTS = "extra_participants";
    public static final String EXTRA_RESULT = "extra_result";
    public static final String EXTRA_POSITION = "extra_position";
    public static final String EXTRA_HOME_PLAYERS = "extra_home_players";
    public static final String EXTRA_AWAY_PLAYERS = "extra_away_players";

    public static final String ACTION_GET_MATCHES_BY_TOURNAMENT = "fit.cvut.org.cz.squash.presentation.services.get_matches_by_tournament";
    public static final String ACTION_GET_PARTICIPANTS_FOR_MATCH = "fit.cvut.org.cz.squash.presentation.services.get_participants_for_match";
    public static final String ACTION_GET_MATCH_BY_ID = "fit.cvut.org.cz.squash.presentation.services.get_match_by_id";
    public static final String ACTION_CREATE_MATCH = "fit.cvut.org.cz.squash.presentation.services.create_match";
    public static final String ACTION_UPDATE_MATCH = "fit.cvut.org.cz.squash.presentation.services.update_match";
    public static final String ACTION_RESET_MATCH = "fit.cvut.org.cz.squash.presentation.services.reset_match";
    public static final String ACTION_UPDATE_MATCH_DETAIL = "fit.cvut.org.cz.squash.presentation.services.update_match_detail";
    public static final String ACTION_GET_MATCH_DETAIL = "fit.cvut.org.cz.squash.presentation.services.get_match_detail";
    public static final String ACTION_GET_MATCH_SETS = "fit.cvut.org.cz.squash.presentation.services.get_match_sets";
    public static final String ACTION_DELETE_MATCH = "fit.cvut.org.cz.squash.presentation.services.delete_match";
    public static final String ACTION_GENERATE_ROUND = "fit.cvut.org.cz.squash.presentation.services.generate_round";
    public static final String ACTION_CAN_ADD_MATCH = "fit.cvut.org.cz.squash.presentation.services.can_add_match";

    @Override
    protected String getActionKey() {
        return EXTRA_ACTION;
    }

    @Override
    protected void doWork(Intent intent) {
        String action = intent.getStringExtra(EXTRA_ACTION);
        if (action == null) action = intent.getAction();

        switch (action){
            case ACTION_GET_MATCHES_BY_TOURNAMENT:{
                Long id = intent.getLongExtra(EXTRA_ID, -1);
                Intent result = new Intent(action);
                ArrayList<Match> matches = new ArrayList<>(ManagerFactory.getInstance(this).matchManager.getByTournamentId(this, id));
                result.putExtra(EXTRA_MATCHES, matches);
                Tournament t = ManagerFactory.getInstance(this).tournamentManager.getById(this, id);
                CompetitionType type = ManagerFactory.getInstance(this).competitionManager.getById(this, t.getCompetitionId()).getType();
                result.putExtra(EXTRA_TYPE, type.id);
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_GET_MATCH_BY_ID:{
                long tournamentId = intent.getLongExtra(EXTRA_ID, -1);
                long matchId = intent.getLongExtra(EXTRA_MATCH_ID, -1);

                Intent result = new Intent(action);
                result.putExtra(EXTRA_MATCH, ManagerFactory.getInstance(this).matchManager.getById(this, matchId));
                result.putParcelableArrayListExtra(EXTRA_PARTICIPANTS, getParticipantsForMatch(tournamentId));
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_GET_PARTICIPANTS_FOR_MATCH:{
                long tournamentId = intent.getLongExtra(EXTRA_ID, -1);

                Intent result = new Intent(action);
                result.putParcelableArrayListExtra(EXTRA_PARTICIPANTS, getParticipantsForMatch(tournamentId));
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_CREATE_MATCH:{
                Match match = intent.getParcelableExtra(EXTRA_MATCH);
                ManagerFactory.getInstance(this).matchManager.insert(this, match);
                break;
            }
            case ACTION_UPDATE_MATCH:{
                Match match = intent.getParcelableExtra(EXTRA_MATCH);
                ManagerFactory.getInstance(this).matchManager.update(this, match);
                break;
            }
            case ACTION_RESET_MATCH:{
                long id = intent.getLongExtra(EXTRA_ID, -1);
                ManagerFactory.getInstance(this).matchManager.resetMatch(this, id);
                Intent result = new Intent(action);
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_UPDATE_MATCH_DETAIL:{
                long matchId = intent.getLongExtra(EXTRA_ID, -1);
                ArrayList<SetRowItem> sets = intent.getParcelableArrayListExtra(EXTRA_MATCHES);
                Match match = ManagerFactory.getInstance(this).matchManager.getById(this, matchId);
                Tournament tournament = ManagerFactory.getInstance(this).tournamentManager.getById(this, match.getTournamentId());
                Competition competition = ManagerFactory.getInstance(this).competitionManager.getById(this, tournament.getCompetitionId());
                ManagerFactory.getInstance(this).matchManager.updateMatch(this, matchId, sets);

                List<Participant> participants = ManagerFactory.getInstance(this).participantManager.getByMatchId(this, matchId);
                for (Participant participant : participants) {
                    // Remove original stats and add new ones (way to handle removed items)
                    // only for teams competition, individuals does not change participants
                    if (CompetitionTypes.teams().equals(competition.getType())) {
                        ManagerFactory.getInstance(this).playerStatManager.deleteByParticipantId(this, participant.getId());
                    }

                    if (ParticipantType.home.toString().equals(participant.getRole())) {
                        ArrayList<PlayerStat> homePlayers = intent.getParcelableArrayListExtra(EXTRA_HOME_PLAYERS);
                        for (PlayerStat player : homePlayers) {
                            player.setParticipantId(participant.getId());
                            ManagerFactory.getInstance(this).playerStatManager.insert(this, player);
                        }
                    } else if (ParticipantType.away.toString().equals(participant.getRole())) {
                        ArrayList<PlayerStat> awayPlayers = intent.getParcelableArrayListExtra(EXTRA_AWAY_PLAYERS);
                        for (PlayerStat player : awayPlayers) {
                            player.setParticipantId(participant.getId());
                            ManagerFactory.getInstance(this).playerStatManager.insert(this, player);
                        }
                    }
                }
                break;
            }
            case ACTION_GET_MATCH_DETAIL:{
                Intent result = new Intent(action);
                Match sm = ManagerFactory.getInstance(this).matchManager.getById(this, intent.getLongExtra(EXTRA_ID, -1));
                result.putExtra(EXTRA_MATCH, sm);
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_GET_MATCH_SETS:{
                Intent result = new Intent(action);
                List<SetRowItem> sets = ManagerFactory.getInstance(this).statisticManager.getMatchSets(this, intent.getLongExtra(EXTRA_ID, -1));
                result.putParcelableArrayListExtra(EXTRA_SETS, new ArrayList<>(sets));
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_DELETE_MATCH:{
                long id = intent.getLongExtra(EXTRA_ID, -1);
                ManagerFactory.getInstance(this).matchManager.delete(this, id);
                Intent result = new Intent(action);
                result.putExtra(EXTRA_POSITION, intent.getIntExtra(EXTRA_POSITION, -1));
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_GENERATE_ROUND:{
                long id = intent.getLongExtra(EXTRA_ID, -1);
                Intent result = new Intent(action);

                result.putExtra(EXTRA_RESULT, enoughParticipants(id));
                if (enoughParticipants(id))
                    ManagerFactory.getInstance(this).matchManager.generateRound(this, id);

                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_CAN_ADD_MATCH:{
                long id = intent.getLongExtra(EXTRA_ID, -1);
                Intent result = new Intent(action);
                result.putExtra(EXTRA_RESULT, enoughParticipants(id));
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
        }
    }

    public static Intent newStartIntent(String action, Context context){
        Intent intent = new Intent(context, MatchService.class);
        intent.putExtra(EXTRA_ACTION, action);

        return intent;
    }

    private ArrayList<Participant> getParticipantsForMatch(long tournamentId) {
        Tournament tr = ManagerFactory.getInstance(this).tournamentManager.getById(this, tournamentId);
        CompetitionType type = ManagerFactory.getInstance(this).competitionManager.getById(this, tr.getCompetitionId()).getType();

        ArrayList<Participant> participants = new ArrayList<>();
        if (type.equals(CompetitionTypes.individuals())) {
            List<Player> players = ManagerFactory.getInstance(this).tournamentManager.getTournamentPlayers(this, tournamentId);
            for (Player player : players) {
                Participant participant = new Participant(-1, player.getId(), null);
                participant.setName(player.getName());
                participants.add(participant);
            }
        } else { // teams
            List<Team> teams = ManagerFactory.getInstance(this).teamManager.getByTournamentId(this, tournamentId);
            for (Team team : teams) {
                Participant participant = new Participant(-1, team.getId(), null);
                participant.setName(team.getName());
                participants.add(participant);
            }
        }
        return participants;
    }

    private boolean enoughParticipants(long id){
        Tournament t = ManagerFactory.getInstance(this).tournamentManager.getById(this, id);
        CompetitionType type = ManagerFactory.getInstance(this).competitionManager.getById(this, t.getCompetitionId()).getType();

        return !((type.equals(CompetitionTypes.individuals()) && ManagerFactory.getInstance(this).tournamentManager.getTournamentPlayers(this, id).size() < 2)
                || (type.equals(CompetitionTypes.teams()) && ManagerFactory.getInstance(this).teamManager.getByTournamentId(this, id).size() < 2));
    }
}
