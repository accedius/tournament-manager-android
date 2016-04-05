package fit.cvut.org.cz.tmlibrary.presentation.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import fit.cvut.org.cz.tmlibrary.R;
import fit.cvut.org.cz.tmlibrary.business.entities.Competition;

/**
 * Created by atgot_000 on 1. 4. 2016.
 */
public abstract class CompetitionOverviewFragment extends AbstractDataFragment {

    private TextView name, start, end, tourSum, playerSum, note;
    private static final String COMP_KEY = "competition_id_key";
    protected long competitionID;

    protected abstract String getCompetitionKey();

    //U těchhle by se dalo nahoru předat competition. To by ale nemělo být potřeba, protože fragment ví competitionID
    protected abstract String getTournamentsSumKey();
    protected abstract String getPlayersSumKey();


    public static CompetitionOverviewFragment newInstance(long id, Class<? extends CompetitionOverviewFragment> clazz){
        CompetitionOverviewFragment fragment = null;
        try {
            Constructor<? extends CompetitionOverviewFragment> c = clazz.getConstructor();
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
        args.putLong(COMP_KEY, id);

        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_competition_overview, container, false);

        name = (TextView) v.findViewById(R.id.comp_name);
        start = (TextView) v.findViewById(R.id.comp_start);
        end = (TextView) v.findViewById(R.id.comp_end);
        tourSum = (TextView) v.findViewById(R.id.comp_tour_sum);
        playerSum = (TextView) v.findViewById(R.id.comp_player_sum);
        note = (TextView) v.findViewById(R.id.comp_note);

        if( getArguments() != null )
            competitionID = getArguments().getLong( COMP_KEY );

        return v;
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        Competition competition = intent.getParcelableExtra(getCompetitionKey());

        if( competition == null )
        {
            name.append( "Competition not found" ); //PROZATIMNI RESENI
            return;
        }

        DateFormat df = new SimpleDateFormat("dd. MM. yyyy");

        name.append(competition.getName());
        start.append( df.format(competition.getStartDate()) );
        end.append( df.format(competition.getStartDate()) );
        tourSum.append( String.valueOf(intent.getIntExtra(getTournamentsSumKey(), 0)));
        playerSum.append( String.valueOf(intent.getIntExtra(getPlayersSumKey(), 0)));
        note.append(competition.getNote());
    }


}
