package fit.cvut.org.cz.bowling.presentation.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.business.entities.ParticipantOverview;
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

public class ParticipantsOverviewFragment extends AbstractListFragment<ParticipantOverview> {
    private BroadcastReceiver participantReceiver = new ParticipantReceiver();
    protected static ArrayList<ParticipantOverview> participantOverviews = new ArrayList<>();
    protected static boolean bindFromDialogInsertions = false;
    private Fragment thisFragment;

    public static ParticipantsOverviewFragment newInstance(long matchId) {
        ParticipantsOverviewFragment fragment = new ParticipantsOverviewFragment();

        Bundle args = new Bundle();
        args.putLong(ExtraConstants.EXTRA_MATCH_ID, matchId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            participantOverviews = savedInstanceState.getParcelableArrayList(ExtraConstants.EXTRA_DATA);
        }
        thisFragment = this;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        participantOverviews = new ArrayList<>(adapter.getData());
        outState.putParcelableArrayList(ExtraConstants.EXTRA_DATA, participantOverviews);
    }

    @Override
    protected AbstractListAdapter getAdapter() {
        return new ParticipantOverviewAdapter() {
            @Override
            protected void setOnClickListeners(View v, final long participantStatId, int position, final String name, int score, byte framesPlayedNumber) {
                super.setOnClickListeners(v, participantStatId, position, name, score, framesPlayedNumber);

                v.setOnClickListener( new View.OnClickListener(){
                                          @Override
                                          public void onClick(View v) {
                                              int index = 0;
                                              while(index < participantOverviews.size()){
                                                  ParticipantOverview overview = participantOverviews.get(index);
                                                  if(overview.getParticipantStatId() == participantStatId) {
                                                      break;
                                                  }
                                                  ++index;
                                              }
                                              EditParticipantOverviewDialog dialog = EditParticipantOverviewDialog.newInstance(index, name);
                                              dialog.setTargetFragment(thisFragment, 0);
                                              dialog.show(getFragmentManager(), "dialog");
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
        if(bindFromDialogInsertions) {
            Intent intent = new Intent();
            bindDataOnView(intent);
        } else {
            Intent intent = ParticipantService.newStartIntent(ParticipantService.ACTION_GET_BY_MATCH_ID, getContext());
            Long matchId = getArguments().getLong(ExtraConstants.EXTRA_MATCH_ID, -1);
            intent.putExtra(ExtraConstants.EXTRA_MATCH_ID, matchId);
            getContext().startService(intent);
        }
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        if(bindFromDialogInsertions) {
            bindFromDialogInsertions = false;
        } else {
            ArrayList<Participant> participants = intent.getParcelableArrayListExtra(ExtraConstants.EXTRA_PARTICIPANTS);
            long matchId = participants.get(0).getMatchId();
            IManagerFactory iManagerFactory = ManagerFactory.getInstance();
            Match match = iManagerFactory.getEntityManager(Match.class).getById(matchId);
            long tournamentId = match.getTournamentId();
            Tournament tournament = iManagerFactory.getEntityManager(Tournament.class).getById(tournamentId);
            TournamentType tournamentType = tournament.getType();
            for (Participant participant : participants) {
                ParticipantOverview overview = new ParticipantOverview();
                long participantId = participant.getParticipantId();
                overview.setParticipantId(participantId);
                if(participant.getParticipantStats().isEmpty()){
                    Byte framesNumber = -1;
                    overview.setParticipantStatId(-1);
                    overview.setFramesPlayedNumber(framesNumber);
                    overview.setScore(-1);
                } else {
                    ParticipantStat stat = (ParticipantStat) participant.getParticipantStats().get(0); // 0 because it only can have 1 ParticipantStat
                    overview.setFramesPlayedNumber(stat.getFramesPlayedNumber());
                    long participantStatId = stat.getId();
                    overview.setParticipantStatId(participantStatId);
                    overview.setScore(stat.getScore());
                }
                String name;
                if (tournamentType.equals(TournamentTypes.individuals())) {
                    Player player = iManagerFactory.getEntityManager(Player.class).getById(participantId);
                    name = player.getName();
                } else {
                    Team team = iManagerFactory.getEntityManager(Team.class).getById(participantId);
                    name = team.getName();
                }
                overview.setName(name);
                participantOverviews.add(overview);
            }
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
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(participantReceiver, filter);
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(participantReceiver);
    }

    @Override
    protected FloatingActionButton getFAB(ViewGroup parent) {
        return null;
    }

    public class ParticipantReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            progressBar.setVisibility(View.GONE);
            contentView.setVisibility(View.VISIBLE);
            switch (action) {
                case ParticipantService.ACTION_GET_BY_MATCH_ID: {
                    bindDataOnView(intent);
                    break;
                }
                /*case ParticipantService.ACTION_DELETE: {
                    break;
                }*/
            }
        }
    }

    public static class EditParticipantOverviewDialog extends DialogFragment {
        ProgressBar progressBar;
        TextView score, framesPlayedNumber;
        ParticipantOverview participantOverview = null;
        int arrayPosition;
        String name;

        //Position of a ParticipantOverview in a participantOverviews list in MatchParticipantsOverviewFragment
        public static EditParticipantOverviewDialog newInstance(int participantStatToEditArrayPosition, String name) {
            EditParticipantOverviewDialog dialog = new EditParticipantOverviewDialog();
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
            score = v.findViewById(R.id.total_score_input);
            framesPlayedNumber = v.findViewById(R.id.played_frames_input);
            arrayPosition = getArguments().getInt(ExtraConstants.EXTRA_SELECTED);
            name = getArguments().getString(ExtraConstants.EXTRA_PARTICIPANT_NAME);
            if(arrayPosition >= 0 && arrayPosition < participantOverviews.size() ) {
                participantOverview = participantOverviews.get(arrayPosition);
            }
            builder.setTitle( getResources().getString(R.string.participant_overview_label) + " " + participantOverview.getName());
            return builder.create();
        }

        @Override
        public void onResume() {
            super.onResume();
            final AlertDialog dialog = (AlertDialog) getDialog();
            if(dialog != null) {
                Button positiveButton = (Button) dialog.getButton(Dialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean returnToken = false;
                        if (framesPlayedNumber.getText().toString().isEmpty() ) {
                            TextInputLayout til = getDialog().findViewById(R.id.played_frames_input);
                            til.setError(getResources().getString(R.string.mpof_frames_played_format_violated));
                            returnToken = true;
                        }

                        if(score.getText().toString().isEmpty()) {
                            TextInputLayout til = getDialog().findViewById(R.id.total_score_input);
                            til.setError(getResources().getString(R.string.mpof_score_format_violated));
                            returnToken = true;
                        }

                        if(returnToken)
                            return;

                        Byte framesNumber = Byte.parseByte(framesPlayedNumber.getText().toString());
                        Integer totalScore = Integer.parseInt(score.getText().toString());

                        if( framesNumber > 10 || framesNumber < 1){
                            TextInputLayout til = getDialog().findViewById(R.id.played_frames_input);
                            til.setError(getResources().getString(R.string.mpof_frames_played_format_violated));
                            returnToken = true;
                        }

                        if(totalScore > 300 || totalScore < 0) {
                            TextInputLayout til = getDialog().findViewById(R.id.total_score_input);
                            til.setError(getResources().getString(R.string.mpof_score_format_violated));
                            returnToken = true;
                        }

                        if(returnToken)
                            return;

                        participantOverview.setScore(totalScore);
                        participantOverview.setFramesPlayedNumber(framesNumber);

                        if (getTargetFragment() != null)
                            getTargetFragment().onActivityResult(0, 1, null);
                        dialog.dismiss();
                    }
                });
            }
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            framesPlayedNumber.setFocusableInTouchMode(true);
            framesPlayedNumber.requestFocus();
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }
}
