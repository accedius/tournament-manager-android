package fit.cvut.org.cz.squash.presentation.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;

import fit.cvut.org.cz.squash.business.ManagersFactory;
import fit.cvut.org.cz.squash.business.entities.SetRowItem;
import fit.cvut.org.cz.tmlibrary.business.CompetitionType;
import fit.cvut.org.cz.tmlibrary.business.entities.NewMatchSpinnerParticipant;
import fit.cvut.org.cz.tmlibrary.business.entities.Participant;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.presentation.services.AbstractIntentServiceWProgress;

/**
 * Created by Vaclav on 3. 4. 2016.
 */
public class MatchService extends AbstractIntentServiceWProgress {

    public MatchService() {
        super("Squash Match Service");
    }

    private static final String EXTRA_ACTION = "extra_action";
    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_MATCHES = "extra_matches";
    public static final String EXTRA_MATCH = "extra_match";
    public static final String EXTRA_TYPE = "extra_type";
    public static final String EXTRA_PARTICIPANTS = "extra_participants";

    public static final String ACTION_GET_MATCHES_BY_TOURNAMENT = "fit.cvut.org.cz.squash.presentation.services.get_matches_by_tournament";
    public static final String ACTION_GET_PARTICIPANTS_FOR_MATCH = "fit.cvut.org.cz.squash.presentation.services.get_participants_for_match";
    public static final String ACTION_GET_MATCH_BY_ID = "fit.cvut.org.cz.squash.presentation.services.get_match_by_id";
    public static final String ACTION_CREATE_MATCH = "fit.cvut.org.cz.squash.presentation.services.create_match";
    public static final String ACTION_UPDATE_MATCH_DETAIL = "fit.cvut.org.cz.squash.presentation.services.update_match_detail";
    public static final String ACTION_GET_MATCH_DETAIL = "fit.cvut.org.cz.squash.presentation.services.get_match_detail";
    public static final String ACTION_DELETE_MATCH = "fit.cvut.org.cz.squash.presentation.services.delete_match";

    @Override
    protected String getActionKey() {
        return EXTRA_ACTION;
    }

    @Override
    protected void doWork(Intent intent) {

        String action = intent.getStringExtra(EXTRA_ACTION);
        if (action == null) action = intent.getAction();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        switch (action){
            case ACTION_GET_MATCHES_BY_TOURNAMENT:{
                Long id = intent.getLongExtra(EXTRA_ID, -1);
                Intent result = new Intent(action);
                result.putExtra(EXTRA_MATCHES, ManagersFactory.getInstance().matchManager.getByTournamentId(this, id));
                Tournament t = ManagersFactory.getInstance().tournamentManager.getById(this, id);
                CompetitionType type = ManagersFactory.getInstance().competitionManager.getById(this, t.getCompetitionId()).getType();
                result.putExtra(EXTRA_TYPE, type.toString());
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_GET_PARTICIPANTS_FOR_MATCH:{
                long tournamentId = intent.getLongExtra(EXTRA_ID, -1);
                Tournament tr = ManagersFactory.getInstance().tournamentManager.getById(this, tournamentId);
                CompetitionType type = ManagersFactory.getInstance().competitionManager.getById(this, tr.getCompetitionId()).getType();

                ArrayList<NewMatchSpinnerParticipant> participants = new ArrayList<>();
                if (type == CompetitionType.Individuals){
                    ArrayList<Player> players = ManagersFactory.getInstance().playerManager.getPlayersByTournament(this, tournamentId);
                    for (Player p : players) participants.add(new NewMatchSpinnerParticipant(p.getId(), p.getName()));

                } else { // teams
                    ArrayList<Team> teams = ManagersFactory.getInstance().teamsManager.getByTournamentId(this, tournamentId);
                    for (Team t : teams) participants.add(new NewMatchSpinnerParticipant(t.getId(), t.getName()));
                }

                Intent result = new Intent(action);
                result.putParcelableArrayListExtra(EXTRA_PARTICIPANTS, participants);
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_CREATE_MATCH:{
                ScoredMatch match = intent.getParcelableExtra(EXTRA_MATCH);
                ManagersFactory.getInstance().matchManager.insert(this, match);
                break;
            }
            case ACTION_UPDATE_MATCH_DETAIL:{
                ArrayList<SetRowItem> sets = intent.getParcelableArrayListExtra(EXTRA_MATCHES);
                ManagersFactory.getInstance().statsManager.updateStatsForMatch(this, intent.getLongExtra(EXTRA_ID, -1), sets);
                break;
            }
            case ACTION_GET_MATCH_DETAIL:{
                Intent result = new Intent(action);
                ArrayList<SetRowItem> sets =  ManagersFactory.getInstance().statsManager.getSetsForMatch(this, intent.getLongExtra(EXTRA_ID, -1));
                result.putParcelableArrayListExtra(EXTRA_MATCHES, sets);
                LocalBroadcastManager.getInstance(this).sendBroadcast(result);
                break;
            }
            case ACTION_DELETE_MATCH:{
                long id = intent.getLongExtra(EXTRA_ID, -1);
                ManagersFactory.getInstance().matchManager.delete(this, id);
                break;
            }
        }


    }

    public static Intent newStartIntent(String action, Context context){
        Intent intent = new Intent(context, MatchService.class);
        intent.putExtra(EXTRA_ACTION, action);

        return intent;
    }
}
