package fit.cvut.org.cz.squash.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;

import fit.cvut.org.cz.squash.business.ManagersFactory;
import fit.cvut.org.cz.squash.business.entities.SetRowItem;
import fit.cvut.org.cz.tmlibrary.business.entities.MatchParticipant;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.business.enums.CompetitionType;
import fit.cvut.org.cz.tmlibrary.business.enums.CompetitionTypes;
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
                result.putExtra(EXTRA_MATCHES, ManagersFactory.getInstance().matchManager.getByTournamentId(this, id));
                Tournament t = ManagersFactory.getInstance().tournamentManager.getById(this, id);
                CompetitionType type = ManagersFactory.getInstance().competitionManager.getById(this, t.getCompetitionId()).getType();
                result.putExtra(EXTRA_TYPE, type.id);
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_GET_MATCH_BY_ID:{
                long tournamentId = intent.getLongExtra(EXTRA_ID, -1);
                long matchId = intent.getLongExtra(EXTRA_MATCH_ID, -1);

                Intent result = new Intent(action);
                result.putExtra(EXTRA_MATCH, ManagersFactory.getInstance().matchManager.getById(this, matchId));
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
                ScoredMatch match = intent.getParcelableExtra(EXTRA_MATCH);
                ManagersFactory.getInstance().matchManager.insert(this, match);
                break;
            }
            case ACTION_UPDATE_MATCH:{
                ScoredMatch match = intent.getParcelableExtra(EXTRA_MATCH);
                ManagersFactory.getInstance().matchManager.update(this, match);
                break;
            }
            case ACTION_RESET_MATCH:{
                long id = intent.getLongExtra(EXTRA_ID, -1);
                ManagersFactory.getInstance().matchManager.resetMatch(this, id);
                Intent result = new Intent(action);
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_UPDATE_MATCH_DETAIL:{
                ArrayList<SetRowItem> sets = intent.getParcelableArrayListExtra(EXTRA_MATCHES);
                ManagersFactory.getInstance().statsManager.updateStatsForMatch(this, intent.getLongExtra(EXTRA_ID, -1), sets);
                break;
            }
            case ACTION_GET_MATCH_DETAIL:{
                Intent result = new Intent(action);
                ScoredMatch sm = ManagersFactory.getInstance().matchManager.getById(this, intent.getLongExtra(EXTRA_ID, -1));
                result.putExtra(EXTRA_MATCH, sm);
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_GET_MATCH_SETS:{
                Intent result = new Intent(action);
                ArrayList<SetRowItem> sets =  ManagersFactory.getInstance().statsManager.getSetsForMatch(this, intent.getLongExtra(EXTRA_ID, -1));
                result.putParcelableArrayListExtra(EXTRA_SETS, sets);
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_DELETE_MATCH:{
                long id = intent.getLongExtra(EXTRA_ID, -1);
                ManagersFactory.getInstance().matchManager.delete(this, id);
                Intent result = new Intent(action);
                result.putExtra(EXTRA_POSITION, intent.getIntExtra(EXTRA_POSITION, -1));
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_GENERATE_ROUND:{
                long id = intent.getLongExtra(EXTRA_ID, -1);
                Intent result = new Intent(action);

                if (enoughParticipants(id)) {
                    result.putExtra(EXTRA_RESULT, true);
                    ManagersFactory.getInstance().matchManager.generateRound(this, id);
                } else result.putExtra(EXTRA_RESULT, false);

                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_CAN_ADD_MATCH:{
                long id = intent.getLongExtra(EXTRA_ID, -1);
                Intent result = new Intent(action);

                if (enoughParticipants(id)) result.putExtra(EXTRA_RESULT, true);
                else result.putExtra(EXTRA_RESULT, false);

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

    private ArrayList<MatchParticipant> getParticipantsForMatch(long tournamentId) {
        Tournament tr = ManagersFactory.getInstance().tournamentManager.getById(this, tournamentId);
        CompetitionType type = ManagersFactory.getInstance().competitionManager.getById(this, tr.getCompetitionId()).getType();

        ArrayList<MatchParticipant> participants = new ArrayList<>();
        if (type.equals(CompetitionTypes.individuals())) {
            ArrayList<Player> players = ManagersFactory.getInstance().playerManager.getPlayersByTournament(this, tournamentId);
            for (Player p : players) participants.add(new MatchParticipant(p.getId(), p.getName()));
        } else { // teams
            ArrayList<Team> teams = ManagersFactory.getInstance().teamsManager.getByTournamentId(this, tournamentId);
            for (Team t : teams) participants.add(new MatchParticipant(t.getId(), t.getName()));
        }
        return participants;
    }

    private boolean enoughParticipants(long id){
        Tournament t = ManagersFactory.getInstance().tournamentManager.getById(this, id);
        CompetitionType type = ManagersFactory.getInstance().competitionManager.getById(this, t.getCompetitionId()).getType();

        return !((type.equals(CompetitionTypes.individuals()) && ManagersFactory.getInstance().playerManager.getPlayersByTournament(this, id).size() < 2)
                || (type.equals(CompetitionTypes.teams()) && ManagersFactory.getInstance().teamsManager.getByTournamentId(this, id).size() < 2));
    }
}
