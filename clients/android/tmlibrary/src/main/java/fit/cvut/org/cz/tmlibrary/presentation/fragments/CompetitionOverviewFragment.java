package fit.cvut.org.cz.tmlibrary.presentation.fragments;

import android.content.Intent;
import android.os.Bundle;
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

    private TextView start, end, tourSum, playerSum, note;
    private static final String COMP_KEY = "competition_id_key";
    protected long competitionID;

    /**
     *
     * @return Key of Comeptition saved as extra
     */
    protected abstract String getCompetitionKey();

    /**
     *
     * @return Key of int tournaments sum saved as extra
     */
    protected abstract String getTournamentsSumKey();

    /**
     *
     * @return Key of int palyers sum saved as extra
     */
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

    @Override
    protected View injectView(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.fragment_competition_overview, container, false);

        start = (TextView) v.findViewById(R.id.comp_start);
        end = (TextView) v.findViewById(R.id.comp_end);
        tourSum = (TextView) v.findViewById(R.id.comp_tour_sum);
        playerSum = (TextView) v.findViewById(R.id.comp_player_sum);
        note = (TextView) v.findViewById(R.id.comp_note);

        if (getArguments() != null)
            competitionID = getArguments().getLong( COMP_KEY );

        return v;
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        Competition competition = intent.getParcelableExtra(getCompetitionKey());

        if (competition == null) {
            getActivity().setTitle(getResources().getString(R.string.competitionNotFound));
            return;
        }

        getActivity().setTitle(getResources().getString(fit.cvut.org.cz.tmlibrary.R.string.competition)+" – "+competition.getName());

        DateFormat df = new SimpleDateFormat("dd. MM. yyyy");

        if (competition.getStartDate() != null)
            start.setText(df.format(competition.getStartDate()));

        if (competition.getEndDate() != null)
            end.setText(df.format(competition.getEndDate()));

        tourSum.setText(String.valueOf(intent.getIntExtra(getTournamentsSumKey(), 0)));
        playerSum.setText(String.valueOf(intent.getIntExtra(getPlayersSumKey(), 0)));
        note.setText(competition.getNote());
    }




}
