package fit.cvut.org.cz.squash.presentation.fragments;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import fit.cvut.org.cz.squash.presentation.activities.AddPlayersActivity;
import fit.cvut.org.cz.squash.presentation.services.TeamService;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.presentation.activities.SelectableListActivity;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.TeamDetailFragment;

/**
 * Created by Vaclav on 24. 4. 2016.
 */
public class MatchPlayersFragment extends SquashTeamDetailFragment {

    @Override
    protected View injectView(LayoutInflater inflater, ViewGroup container) {
        View v = super.injectView(inflater, container);
        TextView teamName = (TextView) v. findViewById(fit.cvut.org.cz.tmlibrary.R.id.tv_name);
        teamName.setVisibility(View.GONE);

        return v;
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        Team t = intent.getParcelableExtra(getTeamKey());
        adapter.swapData(t.getPlayers());
        this.t = t;
    }

    @Override
    protected void updatePlayers(Team t) {}

    public ArrayList<Player> getPlayers() {return adapter.getData();}
}
