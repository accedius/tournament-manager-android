package fit.cvut.org.cz.tmlibrary.presentation.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.R;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractDeletableListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.DeletePlayersAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.vh.OneActionViewHolder;

/**
 * Created by Vaclav on 15. 4. 2016.
 */
public abstract class TeamDetailFragment extends AbstractDataFragment {

    public static final String ARG_ID = "arg_id";
    public static final String SAVE_TEAM = "save_team";
    public static final String SAVE_SEND = "save_send";
    protected boolean sendForData = true;


    public static TeamDetailFragment newInstance(long id, Class<? extends TeamDetailFragment> clazz){
        TeamDetailFragment fragment = null;
        try {
            fragment = clazz.getConstructor().newInstance();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);

        fragment.setArguments(args);
        return fragment;
    }

    private TextView name;
    protected Team t = null;
    private RecyclerView recyclerView;
    protected AbstractDeletableListAdapter<Player, ? extends OneActionViewHolder> adapter;

    protected long teamId = -1;

    /**
     *
     * @return Key of Team saved as Extra
     */
    protected abstract String getTeamKey();

    /**
     *
     * @return Key of ArrayList<Player> delivered from activity when adding players to the team
     */
    protected abstract String getExtraPlayersKey();

    /**
     * Called when team in parameter should be updated
     * @param t
     */
    protected abstract void updatePlayers(Team t);

    /**
     *
     * @param t team is suplied for getting teamId easily
     * @return start Intent of activity that returns players that should be added to this team
     */
    protected abstract Intent getSelectActivityStartIntent(Team t);

    /**
     *
     * @return request code for startActivity for result
     */
    protected abstract int getRequestCode();

    /**
     *
     * @return code that activity return when players were selected properly
     */
    protected abstract int getOKResultCode();

    @Override
    protected View injectView(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(fit.cvut.org.cz.tmlibrary.R.layout.fragment_team_detail, container, false);
        name = (TextView) v.findViewById(fit.cvut.org.cz.tmlibrary.R.id.tv_name);
        teamId = getArguments().getLong(ARG_ID, -1);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        adapter = new DeletePlayersAdapter();
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        final FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab_add);
        if (fab != null){
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE)
                        fab.show();
                    else fab.hide();
                }


            });
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isDataSourceWorking()) return;
                    t.setPlayers(adapter.getData());
                    Intent intent = getSelectActivityStartIntent(t);
                    sendForData = false;
                    startActivityForResult(intent, getRequestCode());
                }
            });
        }
        return v;
    }

    @Override
    protected void bindDataOnView(Intent intent) {

        Team t = intent.getParcelableExtra(getTeamKey());
        name.setText(t.getName());
        adapter.swapData(t.getPlayers());
        this.t = t;
        super.customOnPause();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState != null){
            t = savedInstanceState.getParcelable(SAVE_TEAM);
            sendForData = savedInstanceState.getBoolean(SAVE_SEND);

        }
    }

    @Override
    protected void customOnPause() {
    }

    @Override
    protected void customOnResume() {
        if (t != null) {
            name.setText(t.getName());
            adapter.swapData(t.getPlayers());
        }
        if (sendForData)
            super.customOnResume();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (t != null){
            t.setPlayers(adapter.getData());
            outState.putParcelable(SAVE_TEAM, t);
        }
        outState.putBoolean(SAVE_SEND, sendForData);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_finish, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_finish){
            t.setPlayers(adapter.getData());
            updatePlayers(t);
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != getOKResultCode()) return;

        //it will be only one request code so we do not need to check

        ArrayList<Player> players = data.getParcelableArrayListExtra(getExtraPlayersKey());
        for (Player p : players )
            if (!t.getPlayers().contains(p)) t.getPlayers().add(p);
        adapter.swapData(t.getPlayers());
    }
}
