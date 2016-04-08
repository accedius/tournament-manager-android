package fit.cvut.org.cz.tmlibrary.presentation.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import fit.cvut.org.cz.tmlibrary.R;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;

/**
 * Created by atgot_000 on 8. 4. 2016.
 */
public abstract class TournamentOverviewFragment extends AbstractDataFragment {

    private TextView name, start, end, matchSum, playerSum, teamSum, note;
    private Button confBut;
    private static final String TOUR_KEY = "tournament_id_key";
    protected long tournamentID;

    /**
     *
     * @return String key of tournament received in Bundle from datasource
     */
    protected abstract String getTournamentKey();

    /**
     *
     * @return String key of number of matches played received in Bundle from datasource
     */
    protected abstract String getMatchesSumKey();

    /**
     *
     * @return String key of number of players in the tournament received in Bundle from datasource
     */
    protected abstract String getPlayersSumKey();

    /**
     *
     * @return String key of number of players participating in the tournament received in Bundle from datasource
     */
    protected abstract String getTeamsSumKey();

    /**
     * Called when the button for point gain configuration has been clicked
     */
    protected abstract void pointConfigureClicked();

    public static TournamentOverviewFragment newInstance(long id, Class<? extends TournamentOverviewFragment> clazz){
        TournamentOverviewFragment fragment = null;
        try {
            Constructor<? extends TournamentOverviewFragment> c = clazz.getConstructor();
            fragment = c.newInstance();
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
        args.putLong(TOUR_KEY, id);

        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_tournament_overview, container, false);

        name = (TextView) v.findViewById(R.id.tour_name);
        start = (TextView) v.findViewById(R.id.tour_start);
        end = (TextView) v.findViewById(R.id.tour_end);
        matchSum = (TextView) v.findViewById(R.id.match_sum);
        playerSum = (TextView) v.findViewById(R.id.tour_player_sum);
        teamSum = (TextView) v.findViewById(R.id.team_sum);
        note = (TextView) v.findViewById(R.id.tour_note);
        confBut = (Button) v.findViewById(R.id.conf_but);

        if( getArguments() != null )
            tournamentID = getArguments().getLong( TOUR_KEY );


        confBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pointConfigureClicked();
            }
        });


        return v;
    }

    @Override
    protected void bindDataOnView(Intent intent) {

        Tournament tournament = intent.getParcelableExtra(getTournamentKey());

        if( tournament == null )
        {
            name.setText( "Tournament not found" ); //PROZATIMNI RESENI
            return;
        }

        DateFormat df = new SimpleDateFormat("dd. MM. yyyy");

        name.setText(tournament.getName());
        start.setText(df.format(tournament.getStartDate()));
        end.setText(df.format(tournament.getStartDate()));
        matchSum.setText(String.valueOf(intent.getIntExtra(getMatchesSumKey(), 0)));
        playerSum.setText(String.valueOf(intent.getIntExtra(getPlayersSumKey(), 0)));
        teamSum.setText(String.valueOf(intent.getIntExtra(getTeamsSumKey(), 0)));
        note.setText(tournament.getNote());
    }

}
