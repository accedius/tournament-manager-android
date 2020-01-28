package fit.cvut.org.cz.bowling.presentation.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IWinConditionManager;
import fit.cvut.org.cz.bowling.data.entities.WinCondition;
import fit.cvut.org.cz.bowling.data.helpers.CompetitionTypes;
import fit.cvut.org.cz.bowling.data.helpers.DBConstants;
import fit.cvut.org.cz.bowling.data.helpers.WinConditionTypes;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.services.TournamentService;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICompetitionManager;
import fit.cvut.org.cz.tmlibrary.data.DAOFactory;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.CompetitionType;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.entities.TournamentType;
import fit.cvut.org.cz.tmlibrary.data.helpers.DateFormatter;
import fit.cvut.org.cz.tmlibrary.data.helpers.TournamentTypes;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IDAOFactory;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntityDAO;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.NewTournamentFragment;

/**
 * Fragment is used in CreateTournamentActivity to show tournament creation panel
 */
public class NewBowlingTournamentFragment extends NewTournamentFragment {
    private TextView win_condition_label;
    private AppCompatSpinner win_condition;
    protected ArrayAdapter<WC_item> win_condition_adapter;
    private class WC_item {
        private int condition_id;
        private String condition_name;
        WC_item(int i, String s) {
            condition_id = i;
            condition_name = s;
        }

        public int getCondition_id() {
            return condition_id;
        }

        @NonNull
        @Override
        public String toString() {
            return condition_name;
        }
    }

    @Override
    protected void saveTournament(Tournament t) {
        Intent intent = TournamentService.newStartIntent(TournamentService.ACTION_CREATE, getContext());
        intent.putExtra(ExtraConstants.EXTRA_TOURNAMENT, t);

        getContext().startService(intent);
    }

    @Override
    protected void updateTournament(Tournament t) {
        Intent intent = TournamentService.newStartIntent(TournamentService.ACTION_UPDATE, getContext());
        intent.putExtra(ExtraConstants.EXTRA_TOURNAMENT, t);

        getContext().startService(intent);
    }

    @Override
    protected String getTournamentKey() {
        return ExtraConstants.EXTRA_TOURNAMENT;
    }

    @Override
    public void askForData() {
        Intent intent = TournamentService.newStartIntent(TournamentService.ACTION_FIND_BY_ID, getContext());
        intent.putExtra(ExtraConstants.EXTRA_ID, tournamentId);

        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return TournamentService.isWorking(TournamentService.ACTION_FIND_BY_ID);
    }

    @Override
    protected void registerReceivers() {
        Context context = getContext();
        IntentFilter intentFilter = new IntentFilter(TournamentService.ACTION_FIND_BY_ID);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        localBroadcastManager.registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    @Override
    protected boolean isTypeChoosable() {
        return true;
    }

    @Override
    protected View injectView(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(fit.cvut.org.cz.bowling.R.layout.fragment_new_tournament, container, false);

        note = (EditText) v.findViewById(R.id.et_note);
        name = (EditText) v.findViewById(R.id.et_name);
        startDate = (EditText) v.findViewById(R.id.et_startDate);
        endDate = (EditText) v.findViewById(R.id.et_endDate);

        type_label = (TextView) v.findViewById(R.id.tv_tournament_type_label);
        type = (AppCompatSpinner) v.findViewById(R.id.sp_type);
        win_condition_label = (TextView) v.findViewById(R.id.tv_tournament_win_condition_label);
        win_condition = (AppCompatSpinner) v.findViewById(R.id.sp_win_condition);


        if (getArguments() != null) {
            tournamentId = getArguments().getLong(fit.cvut.org.cz.tmlibrary.presentation.communication.ExtraConstants.EXTRA_TOUR_ID, -1);
            competitionId = getArguments().getLong(fit.cvut.org.cz.tmlibrary.presentation.communication.ExtraConstants.EXTRA_COMP_ID, -1);
        }

        if(isTypeChoosable()) {
            type_label.setVisibility(View.VISIBLE);
            type.setVisibility(View.VISIBLE);
        }

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.addAll(TournamentTypes.tournamentTypes(getResources()));

        type.setAdapter(adapter);

        win_condition_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        win_condition_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        WC_item def = new WC_item(WinConditionTypes.win_condition_default,getContext().getResources().getText(R.string.default_win_condition).toString());
        WC_item tot = new WC_item(WinConditionTypes.win_condition_total_points, getContext().getResources().getText(R.string.total_points_win_condition).toString());
        win_condition_adapter.add(def);
        win_condition_adapter.add(tot);

        if (tournamentId == -1) {
            CompetitionType competitionType = getCompetitionType();
            if(competitionType.id == CompetitionTypes.type_teams) {
                type.setSelection(TournamentTypes.type_teams);
                type.setEnabled(false);
            }
            if(competitionType.id == CompetitionTypes.type_individuals) {
                type.setSelection(TournamentTypes.type_individuals);
                type.setEnabled(false);
            }
            setDatepicker(Calendar.getInstance(), Calendar.getInstance());

            win_condition.setEnabled(false);
        } else {
            type.setEnabled(false);

            win_condition.setVisibility(View.VISIBLE);
            win_condition_label.setVisibility(View.VISIBLE);
            int wc = getWinConditionType();
            if(wc == WinConditionTypes.win_condition_default)
                win_condition.setSelection(win_condition_adapter.getPosition(def));
            else if(wc == WinConditionTypes.win_condition_total_points)
                win_condition.setSelection(win_condition_adapter.getPosition(tot));
        }

        //We don't want user to write into editTexts
        startDate.setKeyListener(null);
        endDate.setKeyListener(null);

        return v;
    }

    private CompetitionType getCompetitionType() {
        ICompetitionManager competitionManager = ((ICompetitionManager) ManagerFactory.getInstance(getContext()).getEntityManager(Competition.class));
        Competition competition = competitionManager.getById(competitionId);
        return competition.getType();
    }

    private int getWinConditionType() {
        IWinConditionManager winConditionManager = ((IWinConditionManager) ManagerFactory.getInstance(getContext()).getEntityManager(WinCondition.class));
        WinCondition winCondition = winConditionManager.getById(competitionId);
        if (winCondition == null) return WinConditionTypes.win_condition_default;
        return winCondition.getWinCondition();
    }

    public int getWinCondition() {
        return ((WC_item)win_condition.getSelectedItem()).getCondition_id();
    }
}

