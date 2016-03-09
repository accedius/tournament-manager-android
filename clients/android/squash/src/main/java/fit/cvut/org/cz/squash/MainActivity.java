package fit.cvut.org.cz.squash;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.ConceptEntity;
import fit.cvut.org.cz.tmlibrary.ConceptService;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private ProgressBar bar;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        textView = (TextView) findViewById(R.id.text);
        bar = (ProgressBar) findViewById(R.id.progress);

        textView.setVisibility(View.GONE);
        bar.setVisibility(View.VISIBLE);

        Intent intent = new Intent(this, ConceptService.class);
        startService(intent);

        receiver = new ConceptReceiver();

        registerReceiver(receiver, new IntentFilter("akce"));
    }

    private BroadcastReceiver receiver;

    public class ConceptReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d("RCVR", "RECEIVED");
            StringBuilder builder = new StringBuilder();
            ArrayList<ConceptEntity> data =  intent.getParcelableArrayListExtra(ConceptService.ARG_DATA);
            for (ConceptEntity e: data){
                builder.append(e.toString());
            }

            textView.setText(builder.toString());
            textView.setVisibility(View.VISIBLE);
            bar.setVisibility(View.GONE);

        }
    }

}
