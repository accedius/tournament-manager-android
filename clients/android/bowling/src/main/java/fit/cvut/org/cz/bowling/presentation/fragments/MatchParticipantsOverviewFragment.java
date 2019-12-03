package fit.cvut.org.cz.bowling.presentation.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.business.entities.ParticipantOverview;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IParticipantStatManager;
import fit.cvut.org.cz.bowling.data.entities.Match;
import fit.cvut.org.cz.bowling.data.entities.ParticipantStat;
import fit.cvut.org.cz.bowling.presentation.adapters.ParticipantOverviewAdapter;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.services.ParticipantService;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManagerFactory;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.entities.TournamentType;
import fit.cvut.org.cz.tmlibrary.data.helpers.TournamentTypes;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;

public class MatchParticipantsOverviewFragment extends AbstractListFragment<ParticipantOverview> {
    private BroadcastReceiver participantStatReceiver = new ParticipantStatReceiver();
    protected static List<ParticipantStat> participantStats = new ArrayList<>();
    protected static List<ParticipantOverview> participantOverviews = new ArrayList<>();

    public static List<ParticipantStat> getParticipantStats() {
        return participantStats;
    }

    public static void setParticipantStats(List<ParticipantStat> participantStats) {
        MatchParticipantsOverviewFragment.participantStats = participantStats;
    }

    public static List<ParticipantOverview> getParticipantOverviews() {
        return participantOverviews;
    }

    public static void setParticipantOverviews(List<ParticipantOverview> participantOverviews) {
        MatchParticipantsOverviewFragment.participantOverviews = participantOverviews;
    }

    public static MatchParticipantsOverviewFragment newInstance(long matchId) {
        MatchParticipantsOverviewFragment fragment = new MatchParticipantsOverviewFragment();

        Bundle args = new Bundle();
        args.putLong(ExtraConstants.EXTRA_MATCH_ID, matchId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    protected AbstractListAdapter getAdapter() {
        return new ParticipantOverviewAdapter() {
            @Override
            protected void setOnClickListeners(View v, final long participantStatId, int position, String name, int score, byte framesPlayedNumber) {
                super.setOnClickListeners(v, participantStatId, position, name, score, framesPlayedNumber);

                v.setOnClickListener( new View.OnClickListener(){
                                          @Override
                                          public void onClick(View v) {
                                          }
                                      }
                );

                v.setOnLongClickListener(new View.OnLongClickListener() {
                                             @Override
                                             public boolean onLongClick(View v) {
                                                 return true; //don't know if we want to have an option to delete participants
                                             }
                                         }
                );
            }
        };
    }

    @Override
    protected String getDataKey() {
        return ExtraConstants.EXTRA_PARTICIPANT_OVERVIEWS;
    }

    @Override
    public void askForData() {
        Intent intent = ParticipantService.newStartIntent(ParticipantService.ACTION_GET_BY_MATCH_ID, getContext());
        intent.putExtra(ExtraConstants.EXTRA_MATCH_ID, getArguments().getLong(ExtraConstants.EXTRA_MATCH_ID, -1));

        getContext().startService(intent);
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        ArrayList<Participant> participants = intent.getParcelableArrayListExtra(ExtraConstants.EXTRA_PARTICIPANTS);
        long matchId = participants.get(0).getMatchId();
        IManagerFactory iManagerFactory = ManagerFactory.getInstance();
        Match match = iManagerFactory.getEntityManager(Match.class).getById(matchId);
        long tournamentId = match.getTournamentId();
        Tournament tournament = iManagerFactory.getEntityManager(Tournament.class).getById(tournamentId);
        TournamentType tournamentType = tournament.getType();
        for(Participant participant : participants) {
            ParticipantStat stat = (ParticipantStat) participant.getParticipantStats().get(0);
            participantStats.add(stat);
            ParticipantOverview overview = new ParticipantOverview();
            overview.setFramesPlayedNumber(stat.getFramesPlayedNumber());
            long participantId = stat.getParticipantId();
            overview.setParticipantStatId(participantId);
            overview.setScore(stat.getScore());
            String name;
            if( tournamentType.equals(TournamentTypes.individuals()) ) {
                Player player = iManagerFactory.getEntityManager(Player.class).getById(participantId);
                name = player.getName();
            } else {
                Team team = iManagerFactory.getEntityManager(Team.class).getById(participantId);
                name = team.getName();
            }
            overview.setName(name);
            participantOverviews.add(overview);
        }
        intent.removeExtra(ExtraConstants.EXTRA_PARTICIPANTS);
        intent.putParcelableArrayListExtra(getDataKey(), (ArrayList<ParticipantOverview>) participantOverviews);
        super.bindDataOnView(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return ParticipantService.isWorking(ParticipantService.ACTION_GET_BY_MATCH_ID);
    }

    @Override
    protected void registerReceivers() {
        IntentFilter filter = new IntentFilter(ParticipantService.ACTION_GET_BY_MATCH_ID);
        //filter.addAction(ParticipantService.ACTION_DELETE);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(participantStatReceiver, filter);
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(participantStatReceiver);
    }

    @Override
    protected FloatingActionButton getFAB(ViewGroup parent) {
        return null;
    }

    public class ParticipantStatReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            progressBar.setVisibility(View.GONE);
            contentView.setVisibility(View.VISIBLE);
            switch (action) {
                case ParticipantService.ACTION_GET_BY_MATCH_ID: {

                    break;
                }
                /*case ParticipantService.ACTION_DELETE: {
                    break;
                }*/
            }
        }
    }

    public static class EditParticipantStatDialog extends DialogFragment {
        ProgressBar progressBar;
        TextView score, framesPlayedNumber;
        ParticipantStat participantStat;
        int arrayPosition;
        String name;

        //Position of a ParticipantStat in a participantStats list in MatchParticipantsOverviewFragment
        public static EditParticipantStatDialog newInstance(int participantStatToEditArrayPosition, String name) {
            EditParticipantStatDialog dialog = new EditParticipantStatDialog();
            Bundle args = new Bundle();
            args.putInt(ExtraConstants.EXTRA_SELECTED, participantStatToEditArrayPosition);
            args.putString(ExtraConstants.EXTRA_PARTICIPANT_NAME, name);
            dialog.setArguments(args);
            return dialog;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setPositiveButton(fit.cvut.org.cz.tmlibrary.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //empty, because must be overridden in onResume in order not to close on bad input and instead check for constraints (https://stackoverflow.com/questions/2620444/how-to-prevent-a-dialog-from-closing-when-a-button-is-clicked)
                }
            });

            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            View v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_match_participant_stats, null);
            builder.setView(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progress_spinner);
            arrayPosition = getArguments().getInt(ExtraConstants.EXTRA_SELECTED);
            name = getArguments().getString(ExtraConstants.EXTRA_PARTICIPANT_NAME);
            if(arrayPosition != -1) {
                progressBar.setVisibility(View.VISIBLE);

            }

            return super.onCreateDialog(savedInstanceState);
        }
    }
}
