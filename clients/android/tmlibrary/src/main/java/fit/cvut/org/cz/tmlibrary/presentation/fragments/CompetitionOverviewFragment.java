package fit.cvut.org.cz.tmlibrary.presentation.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;

import fit.cvut.org.cz.tmlibrary.R;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.CompetitionType;
import fit.cvut.org.cz.tmlibrary.data.helpers.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.data.helpers.DateFormatter;
import fit.cvut.org.cz.tmlibrary.presentation.communication.ExtraConstants;

/**
 * Fragment for displaying Competition overview.
 */
public abstract class CompetitionOverviewFragment extends AbstractDataFragment {
    protected TextView type, start, end, tourSum, playerSum, note;
    protected long competitionId;
    protected Competition competition = null;

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
        args.putLong(ExtraConstants.EXTRA_COMP_ID, id);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View injectView(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.fragment_competition_overview, container, false);

        type = (TextView) v.findViewById(R.id.comp_type);
        start = (TextView) v.findViewById(R.id.comp_start);
        end = (TextView) v.findViewById(R.id.comp_end);
        tourSum = (TextView) v.findViewById(R.id.comp_tour_sum);
        playerSum = (TextView) v.findViewById(R.id.comp_player_sum);
        note = (TextView) v.findViewById(R.id.comp_note);

        if (getArguments() != null)
            competitionId = getArguments().getLong(ExtraConstants.EXTRA_COMP_ID);

        return v;
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        competition = intent.getParcelableExtra(getCompetitionKey());

        if (competition == null) {
            getActivity().setTitle(getResources().getString(R.string.competitionNotFound));
            return;
        }

        getActivity().setTitle(getResources().getString(fit.cvut.org.cz.tmlibrary.R.string.competition)+" – "+competition.getName());

        CompetitionType competitionType;
        try {
            int typeId = competition.getTypeId();
            competitionType = CompetitionTypes.getTypeByTypeId(typeId);
        } catch (Exception e) {
            competitionType = CompetitionTypes.individuals();
        }
        type.setText(competitionType.value);

        DateFormat dateFormat = DateFormatter.getInstance().getDisplayDateFormat();

        if (competition.getStartDate() != null)
            start.setText(dateFormat.format(competition.getStartDate()));

        if (competition.getEndDate() != null)
            end.setText(dateFormat.format(competition.getEndDate()));

        tourSum.setText(String.valueOf(intent.getIntExtra(getTournamentsSumKey(), 0)));
        playerSum.setText(String.valueOf(intent.getIntExtra(getPlayersSumKey(), 0)));
        note.setText(competition.getNote());
    }

    public Competition getCompetition() {
        return competition;
    }

}
