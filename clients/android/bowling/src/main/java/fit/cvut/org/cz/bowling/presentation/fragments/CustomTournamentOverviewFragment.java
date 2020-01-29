package fit.cvut.org.cz.bowling.presentation.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IMatchManager;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IWinConditionManager;
import fit.cvut.org.cz.bowling.data.entities.WinCondition;
import fit.cvut.org.cz.bowling.data.helpers.WinConditionTypes;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManagerFactory;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.entities.TournamentType;
import fit.cvut.org.cz.tmlibrary.data.helpers.DateFormatter;
import fit.cvut.org.cz.tmlibrary.data.helpers.TournamentTypes;
import fit.cvut.org.cz.tmlibrary.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractDataFragment;

/**
 * Fragment for displaying Tournament Overview.
 */
public abstract class CustomTournamentOverviewFragment extends AbstractDataFragment {
    private TextView type, start, end, matchSum, playerSum, note, win_condition;
    protected TextView teamSum, teamsLabel;
    protected long tournamentId;

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

    public static CustomTournamentOverviewFragment newInstance(long id, Class<? extends CustomTournamentOverviewFragment> clazz){
        CustomTournamentOverviewFragment fragment = null;
        try {
            Constructor<? extends CustomTournamentOverviewFragment> c = clazz.getConstructor();
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
        args.putLong(ExtraConstants.EXTRA_TOUR_ID, id);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View injectView(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.fragment_tournament_overview, container, false);

        type = (TextView) v.findViewById(R.id.tour_type);
        win_condition = (TextView) v.findViewById(R.id.tour_win_condition);
        start = (TextView) v.findViewById(R.id.tour_start);
        end = (TextView) v.findViewById(R.id.tour_end);
        matchSum = (TextView) v.findViewById(R.id.match_sum);
        playerSum = (TextView) v.findViewById(R.id.tour_player_sum);
        teamSum = (TextView) v.findViewById(R.id.team_sum);
        teamsLabel = (TextView) v.findViewById(R.id.teams_label);
        note = (TextView) v.findViewById(R.id.tour_note);

        if (getArguments() != null)
            tournamentId = getArguments().getLong(ExtraConstants.EXTRA_TOUR_ID);

        return v;
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        Tournament tournament = intent.getParcelableExtra(getTournamentKey());

        if (tournament == null) {
            getActivity().setTitle(getResources().getString(R.string.tournamentNotFound));
            return;
        }

        getActivity().setTitle(getResources().getString(R.string.tournament)+" – "+tournament.getName());

        DateFormat dateFormat = DateFormatter.getInstance().getDisplayDateFormat();

        TournamentType tournamentType;
        try {
            int typeId = tournament.getTypeId();
            tournamentType = TournamentTypes.getMyTournamentType(typeId);
        } catch (Exception e) {
            tournamentType = TournamentTypes.teams();
        }

        IWinConditionManager winConditionManager = ManagerFactory.getInstance().getEntityManager(WinCondition.class);
        WinCondition wc = winConditionManager.getByTournamentId(tournament.getId());
        int condition;
        if(wc != null)
        {
            condition = wc.getWinCondition();
        }
        else
        {
            condition = WinConditionTypes.win_condition_default;
        }

        if(condition == WinConditionTypes.win_condition_total_points)
            win_condition.setText(R.string.total_points_win_condition);
        else
            win_condition.setText(R.string.default_win_condition);

        type.setText(tournamentType.value);
        if (tournament.getStartDate() != null)
            start.setText(dateFormat.format(tournament.getStartDate()));
        if (tournament.getEndDate() != null)
            end.setText(dateFormat.format(tournament.getEndDate()));
        matchSum.setText(String.valueOf(intent.getIntExtra(getMatchesSumKey(), 0)));
        playerSum.setText(String.valueOf(intent.getIntExtra(getPlayersSumKey(), 0)));
        teamSum.setText(String.valueOf(intent.getIntExtra(getTeamsSumKey(), 0)));
        note.setText(tournament.getNote());
    }

}
