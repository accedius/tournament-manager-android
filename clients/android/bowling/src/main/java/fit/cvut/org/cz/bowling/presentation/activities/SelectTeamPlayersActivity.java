package fit.cvut.org.cz.bowling.presentation.activities;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.bowling.data.entities.PlayerStat;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.fragments.SelectTeamPlayersFragment;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.presentation.activities.SelectableListActivity;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractSelectableListFragment;

public class SelectTeamPlayersActivity extends SelectableListActivity<Player> {
    /**
     * Creates a new intent to start this activity
     * @param context Context of intent
     * @param teamId Id of a team, represented by participant in a match
     * @return Intent that can be used to start this activity
     */
    public static Intent newStartIntent(Context context, long teamId, ArrayList<PlayerStat> alreadySelectedPlayers) {
        Intent intent = new Intent(context, AddParticipantsActivity.class);
        intent.putExtra(ExtraConstants.EXTRA_TEAM_ID, teamId);
        intent.putExtra(ExtraConstants.EXTRA_SELECTED, alreadySelectedPlayers);

        return intent;
    }

    @Override
    protected AbstractSelectableListFragment<Player> getListFragment() {
        long teamId = getIntent().getLongExtra(ExtraConstants.EXTRA_TEAM_ID, -1);
        List<PlayerStat> alreadySelectedPlayers = getIntent().getParcelableArrayListExtra(ExtraConstants.EXTRA_SELECTED);
        return SelectTeamPlayersFragment.newInstance(teamId, alreadySelectedPlayers);
    }
}
