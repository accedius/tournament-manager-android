package fit.cvut.org.cz.tournamentmanager.presentation.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;

import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractSelectableListAdapter;
import fit.cvut.org.cz.tournamentmanager.R;
import fit.cvut.org.cz.tournamentmanager.presentation.adapters.SelectPlayersAdapter;
import fit.cvut.org.cz.tournamentmanager.presentation.adapters.vh.SelectPlayersViewHolder;

public class ToolbarActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AbstractSelectableListAdapter<Player, SelectPlayersViewHolder> adapter;
    private Button sel;

    private Player[] data = {new Player("Joe"), new Player("Jane"), new Player("Jake"), new Player("Jill"), new Player("Jack")};
    private SparseBooleanArray selected = new SparseBooleanArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        selected.append(2, true);
        selected.append(4, true);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        adapter = new SelectPlayersAdapter();
        recyclerView.setAdapter(adapter);

        sel = (Button) findViewById(R.id.btn_sel);
        sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.swapData(new ArrayList(Arrays.asList(data)), selected);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar, menu);

        return true;
    }

    private void click(){
        for (Player p: adapter.getSelectedItems()){
            Log.d("Player", p.getName());
        }
    }
}
