package fit.cvut.org.cz.tmlibrary.presentation.dialogs;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;

import fit.cvut.org.cz.tmlibrary.R;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;

/**
 * Created by Vaclav on 14. 4. 2016.
 */
public abstract class InsertTeamDialog extends DialogFragment{
    public static final String ARG_ID = "arg_id";
    public static final String ARG_TOURNAMENT_ID = "arg_tournament_id";
    private ProgressBar progressBar;
    private TextView name;
    private Team team;

    protected long teamId, tournamentId;

    public static InsertTeamDialog newInstance(long id, boolean forTournament, Class<? extends InsertTeamDialog> clazz) {
        InsertTeamDialog fragment = null;
        try {
            fragment = clazz.getConstructor().newInstance();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Bundle args = new Bundle();
        if (forTournament) args.putLong(ARG_TOURNAMENT_ID, id);
        else args.putLong(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    protected abstract void askForData();
    protected abstract void registerReceiver();
    protected abstract void unregisterReceiver();
    protected abstract void insertTeam(Team t);
    protected abstract void editTeam(Team t);
    protected abstract String getTeamKey();
    protected abstract boolean isDataSourceWorking();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (name.getText().toString().isEmpty() || isDataSourceWorking())
                    return;

                if (tournamentId != -1) {
                    insertTeam(new Team(tournamentId, name.getText().toString()));
                } else if (teamId != -1) {
                    team.setName(name.getText().toString());
                    editTeam(team);
                }
                if (getTargetFragment() != null)
                    getTargetFragment().onActivityResult(0, 1, null);
                dismiss();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        View v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_insert_team, null);

        builder.setView(v);
        name = (TextView) v.findViewById(R.id.tv_name);
        progressBar = (ProgressBar) v.findViewById(R.id.progress_spinner);
        teamId = getArguments().getLong(ARG_ID, -1);
        tournamentId = getArguments().getLong(ARG_TOURNAMENT_ID, -1);

        if (teamId != -1) {
            registerReceiver();
            askForData();
            progressBar.setVisibility(View.VISIBLE);
            name.setVisibility(View.GONE);
        }

        builder.setTitle(getResources().getString(R.string.team_settings));
        return builder.create();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        name.setFocusableInTouchMode(true);
        name.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (teamId != -1)
            unregisterReceiver();
        super.onDismiss(dialog);
    }

    protected BroadcastReceiver receiver = new TeamReceiver();
    public class TeamReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            progressBar.setVisibility(View.GONE);
            name.setVisibility(View.VISIBLE);
            team = intent.getParcelableExtra(getTeamKey());
            name.setText(team.getName());
        }
    }
}
