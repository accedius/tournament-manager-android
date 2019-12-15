package fit.cvut.org.cz.bowling.presentation.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.business.entities.FrameOverview;
import fit.cvut.org.cz.bowling.data.entities.Frame;
import fit.cvut.org.cz.bowling.data.entities.Match;
import fit.cvut.org.cz.bowling.data.entities.ParticipantStat;
import fit.cvut.org.cz.bowling.data.entities.PlayerStat;
import fit.cvut.org.cz.bowling.data.entities.Roll;
import fit.cvut.org.cz.bowling.presentation.adapters.FrameOverviewAdapter;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.constraints.ConstraintsConstants;
import fit.cvut.org.cz.bowling.presentation.dialogs.EditDeleteDialog;
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

public class FrameListFragment extends BowlingAbstractMatchStatsListFragment<FrameOverview> {

    protected static List<FrameOverview> frameOverviews = new ArrayList<>();
    protected static int participantSelectedIndex = 0;
    protected static TournamentType tournamentType = null;
    protected static List<List<FrameOverview>> participantsFrameOverviews = new ArrayList<>();
    protected static List<List<Player>> matchPlayers = new ArrayList<>();
    protected static List<Player> participantPlayers = new ArrayList<>();
    protected static ArrayList<Participant> matchParticipants = new ArrayList<>();
    private Spinner participantSpinner = null;
    private ArrayAdapter<Participant> participantArrayAdapter = null;
    protected static boolean bindFromDialogInsertions = false;
    protected static boolean participantsBinded = false;
    protected static final int REQUEST_CODE_UPDATE_LOCALLY = 1337;
    private static Fragment thisFragment;

    final static int maxFrames = ConstraintsConstants.tenPinMatchParticipantMaxFrames;
    final static int maxFrameScore = ConstraintsConstants.tenPinFrameMaxScore;
    final static int maxScore = ConstraintsConstants.tenPinMatchParticipantMaxScore;

    @Override
    public List<Participant> getMatchStats() {
        int i = 0;
        for(Participant participant : matchParticipants) {
            List<FrameOverview> overviews = participantsFrameOverviews.get(i);
            ParticipantStat stat;
            boolean noPreviousStats = participant.getParticipantStats().isEmpty();
            if(noPreviousStats) {
                stat = new ParticipantStat();
                stat.setParticipantId(participant.getId());
            } else {
                stat = (ParticipantStat) participant.getParticipantStats().get(0);
            }
            byte framesNumber = (byte) overviews.size();
            stat.setFramesPlayedNumber(framesNumber);
            if(framesNumber < 1){
                stat.setScore(0);
                stat.setFrames(new ArrayList<Frame>());
            } else {
                stat.setScore(overviews.get(framesNumber - 1).getCurrentScore());
                List<Frame> frames = new ArrayList<>();
                for(FrameOverview overview : overviews) {
                    Frame frame = new Frame();
                    //TODO
                }
            }

            if(noPreviousStats){
                List<ParticipantStat> stats = new ArrayList<>();
                stats.add(stat);
                participant.setParticipantStats(stats);
            }

            ++i;
        }

        return matchParticipants;
    }

    public static FrameListFragment newInstance(long matchId) {
        FrameListFragment fragment = new FrameListFragment();

        Bundle args = new Bundle();
        args.putLong(ExtraConstants.EXTRA_MATCH_ID, matchId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        thisFragment = this;
        bindFromDialogInsertions = false;
        participantSelectedIndex = 0;
        participantsFrameOverviews = null;
        frameOverviews = null;
        matchPlayers = null;
        participantPlayers = null;
        participantsBinded = false;

        Long matchId = getArguments().getLong(ExtraConstants.EXTRA_MATCH_ID, -1);
        IManagerFactory iManagerFactory = ManagerFactory.getInstance();
        Match match = iManagerFactory.getEntityManager(Match.class).getById(matchId);
        long tournamentId = match.getTournamentId();
        Tournament tournament = iManagerFactory.getEntityManager(Tournament.class).getById(tournamentId);
        tournamentType = TournamentTypes.getMyTournamentType(tournament.getTypeId());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected AbstractListAdapter getAdapter() {
        return new FrameOverviewAdapter(tournamentType) {
            @Override
            protected void setOnClickListeners(View v, final int position, final long playerId, final byte frameNumber, List<Byte> rolls, String playerName, int currentScore) {
                super.setOnClickListeners(v, position, playerId, frameNumber, rolls, playerName, currentScore);

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int index = frameNumber - 1;
                        EditFrameListDialog dialog = EditFrameListDialog.newInstance(index);
                        dialog.setTargetFragment(thisFragment, 0);
                        dialog.show(getFragmentManager(), "dialogEdit");
                    }
                });

                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int index = frameNumber - 1;
                        String title = "Frame #" + frameNumber;
                        EditDeleteFrameDialog dialog = EditDeleteFrameDialog.newInstance(index, title);
                        dialog.setTargetFragment(thisFragment, 0);
                        dialog.show(getFragmentManager(), "dialogEditDelete");
                        return true;
                    }
                });
            }
        };
    }

    @Override
    protected View injectView(LayoutInflater inflater, ViewGroup container) {

        View fragmentView = inflater.inflate(R.layout.fragment_match_complex_stats, container, false);

        recyclerView = (RecyclerView) fragmentView.findViewById(R.id.complex_stats_recycler_view);
        adapter = getAdapter();
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        int orientation = getActivity().getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_PORTRAIT) {
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        } else {
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        }
        recyclerView.setLayoutManager(linearLayoutManager);
        participantSpinner = fragmentView.findViewById(R.id.participant_spinner);

        final FloatingActionButton fab = getFAB((ViewGroup) fragmentView);
        if (fab != null) {
            this.fab = fab;
            ((ViewGroup) fragmentView).addView(fab);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && frameOverviews.size()<10)
                        fab.show();

                    else fab.hide();
                }
            });
        }

        return fragmentView;
    }

    private int getNextRollsScore(final int positionFrom, final int rollsNumber) {
        int rollsRemaining = rollsNumber;
        int arraySize = frameOverviews.size();
        int readPosition = positionFrom + 1;
        int score = 0;
        while (rollsRemaining > 0 && readPosition < arraySize) {
            List<Byte> frameRolls = frameOverviews.get(readPosition).getRolls();
            score += frameRolls.get(0);
            if(frameRolls.size() >= rollsRemaining) {
                if(rollsRemaining == 2) {
                    score += frameRolls.get(1);
                }
                rollsRemaining = 0;
            } else {
                --rollsRemaining;
                ++readPosition;
            }
        }
        return score;
    }

    private void updateScores(final int positionFrom) {
        int arraySize = frameOverviews.size();
        for(int i = positionFrom; i < arraySize; ++i) {
            FrameOverview f = frameOverviews.get(i);
            int currentFrameScore = i == 0 ? 0 : frameOverviews.get(i-1).getCurrentScore();
            currentFrameScore += f.getFrameScore();
            if(f.getFrameScore() == maxFrameScore) {
                if(f.getRolls().get(0) == maxFrameScore){
                    currentFrameScore += getNextRollsScore(i, 2);
                } else {
                    currentFrameScore += getNextRollsScore(i, 1);
                }
            }
            f.setCurrentScore(currentFrameScore);
        }
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        if(bindFromDialogInsertions) {
            bindFromDialogInsertions = false;
        } else {
            if(participantsFrameOverviews == null) {
                matchPlayers = new ArrayList<>();
                matchParticipants = intent.getParcelableArrayListExtra(ExtraConstants.EXTRA_PARTICIPANTS);
                if(!matchParticipants.isEmpty()){
                    participantsFrameOverviews = new ArrayList<>();
                }
                IManagerFactory iManagerFactory = ManagerFactory.getInstance();
                int index = 0;
                for(Participant participant : matchParticipants) {

                    matchPlayers.add(new ArrayList<Player>());
                    participantPlayers = matchPlayers.get(index);

                    List<PlayerStat> playerStats = (List<PlayerStat>) participant.getPlayerStats();
                    for(PlayerStat stat : playerStats) {
                        Player player = iManagerFactory.getEntityManager(Player.class).getById(stat.getPlayerId());
                        participantPlayers.add(player);
                    }

                    String name;
                    long participantId = participant.getParticipantId();
                    if (tournamentType.equals(TournamentTypes.individuals())) {
                        Player player = iManagerFactory.getEntityManager(Player.class).getById(participantId);
                        name = player.getName();
                    } else {
                        Team team = iManagerFactory.getEntityManager(Team.class).getById(participantId);
                        name = team.getName();
                    }
                    participant.setName(name);

                    participantsFrameOverviews.add(new ArrayList<FrameOverview>());
                    frameOverviews = participantsFrameOverviews.get(index);

                    if(participant.getParticipantStats() != null && participant.getParticipantStats().size() > 0){
                        ParticipantStat participantStat = (ParticipantStat) participant.getParticipantStats().get(0);
                        List<Frame> frames = participantStat.getFrames();
                        byte i = 1;
                        byte frameScore = 0;

                        for(Frame frame : frames) {
                            List<Byte> rolls = new ArrayList<>();
                            List<Roll> playedRolls = frame.getRolls();

                            for(Roll roll : playedRolls) {
                                byte rollPoints = roll.getPoints();
                                frameScore += rollPoints;
                                rolls.add(rollPoints);
                            }

                            long playerId = frame.getPlayerId();
                            Player player = iManagerFactory.getEntityManager(Player.class).getById(playerId);
                            String playerName = player.getName();
                            FrameOverview frameOverview = new FrameOverview(i, rolls, playerName, 0, playerId, frameScore);
                            frameOverviews.add(frameOverview);
                        }

                    }

                    updateScores(0);
                    ++index;
                }
            }
            intent.removeExtra(ExtraConstants.EXTRA_PARTICIPANTS);
        }
        if(participantsFrameOverviews == null) {
            fab.hide();
            return;
        }
        if(!participantsBinded){
            bindParticipantsOnView(matchParticipants);
            participantsBinded = true;
        }
        if(frameOverviews.size() >= 10 || participantSelectedIndex == -1) {
            fab.hide();
        } else if(fab.isOrWillBeHidden()) {
            fab.show();
        }
        intent.putParcelableArrayListExtra(getDataKey(), (ArrayList<FrameOverview>) frameOverviews);

        super.bindDataOnView(intent);
    }

    private void bindParticipantsOnView(ArrayList<Participant> matchParticipants) {
        participantArrayAdapter = new ArrayAdapter<Participant>(getContext(), android.R.layout.simple_spinner_item, matchParticipants);
        participantArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        participantSpinner.setAdapter(participantArrayAdapter);
        participantSpinner.setSelection(participantSelectedIndex);
        participantSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                participantSelectedIndex = position;
                frameOverviews = participantsFrameOverviews.get(position);
                participantPlayers = matchPlayers.get(position);
                bindDataOnView(new Intent());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected FloatingActionButton getFAB(ViewGroup parent) {
        Context context = getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        FloatingActionButton fab = (FloatingActionButton) layoutInflater.inflate(R.layout.floatingbutton_add, parent, false);

        fab.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       int index = frameOverviews.size();
                                       EditFrameListDialog dialog = EditFrameListDialog.newInstance(index);
                                       dialog.setTargetFragment(thisFragment, 0);
                                       dialog.show(getFragmentManager(), "dialogCreate");
                                   }
                               }
        );

        return fab;
    }

    @Override
    protected String getDataKey() {
        return ExtraConstants.EXTRA_FRAME_OVERVIEWS;
    }

    @Override
    public void askForData() {
        if(bindFromDialogInsertions) {
            Intent intent = new Intent();
            bindDataOnView(intent);
        } else {
            Intent intent = ParticipantService.newStartIntent(ParticipantService.ACTION_GET_BY_MATCH_ID_WITH_ALL_CONTENTS, getContext());
            Long matchId = getArguments().getLong(ExtraConstants.EXTRA_MATCH_ID, -1);
            intent.putExtra(ExtraConstants.EXTRA_MATCH_ID, matchId);
            getContext().startService(intent);
        }
    }

    @Override
    protected boolean isDataSourceWorking() {
        return ParticipantService.isWorking(ParticipantService.ACTION_GET_BY_MATCH_ID_WITH_ALL_CONTENTS);
    }

    @Override
    protected void registerReceivers() {
        IntentFilter filter = new IntentFilter(ParticipantService.ACTION_GET_BY_MATCH_ID_WITH_ALL_CONTENTS);
        //filter.addAction(ParticipantService.ACTION_DELETE);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(participantReceiver, filter);
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(participantReceiver);
    }


    private BroadcastReceiver participantReceiver = new ParticipantReceiver();

    public class ParticipantReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            progressBar.setVisibility(View.GONE);
            contentView.setVisibility(View.VISIBLE);
            switch (action) {
                case ParticipantService.ACTION_GET_BY_MATCH_ID_WITH_ALL_CONTENTS: {
                    participantsBinded = false;
                    bindDataOnView(intent);
                    break;
                }
                /*case ParticipantService.ACTION_DELETE: {
                    break;
                }*/
            }
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_UPDATE_LOCALLY) {
            updateScores(resultCode);
            bindDataOnView(new Intent());
        } else {
            progressBar.setVisibility(View.VISIBLE);
            contentView.setVisibility(View.GONE);
        }
    }

    public static class EditFrameListDialog extends DialogFragment {
        TextView roll1, roll2, roll3;
        int position;
        FrameOverview frameOverview = null;
        int frameNumber;
        boolean toCreate = false;

        public static EditFrameListDialog newInstance(int arrayListPosition) {
            EditFrameListDialog dialog = new EditFrameListDialog();
            Bundle args = new Bundle();
            args.putInt(ExtraConstants.EXTRA_SELECTED, arrayListPosition);
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

            View v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_frame_throws, null);
            builder.setView(v);
            roll1 = v.findViewById(R.id.throw_1_input);
            roll2 = v.findViewById(R.id.throw_2_input);
            roll3 = v.findViewById(R.id.throw_3_input);
            position = getArguments().getInt(ExtraConstants.EXTRA_SELECTED);
            frameNumber = position + 1;
            if(position >= 0 && position < frameOverviews.size() ) {
                frameOverview = frameOverviews.get(position);
            } else if (position == frameOverviews.size()) {
                toCreate = true;
                frameOverview = new FrameOverview();
                frameOverview.setFrameNumber( (byte) frameNumber);
                if(tournamentType.equals(TournamentTypes.individuals())){
                    Player player = participantPlayers.get(0);
                    frameOverview.setPlayerName(player.getName());
                    frameOverview.setPlayerId(player.getId());
                } else {
                    //TODO
                }
            }
            if (frameNumber == maxFrames)
                v.findViewById(R.id.throw_3_input_layout).setVisibility(View.VISIBLE);
            builder.setTitle(getResources().getString(R.string.frame_num) + frameNumber + ": " + frameOverview.getPlayerName());
            return builder.create();
        }

        @Override
        public void onResume() {
            super.onResume();
            final AlertDialog dialog = (AlertDialog) getDialog();
            if(dialog != null) {
                Button positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean returnToken = false;
                        int pinsInRoll1 = 0;
                        int pinsInRoll2 = 0;
                        int pinsInRoll3 = 0;
                        if (!roll1.getText().toString().isEmpty() ) {
                            pinsInRoll1 = Integer.parseInt(roll1.getText().toString());
                        }

                        if (!roll2.getText().toString().isEmpty() ) {
                            pinsInRoll2 = Integer.parseInt(roll2.getText().toString());
                        }

                        if (frameNumber == maxFrames && !roll3.getText().toString().isEmpty() ) {
                            pinsInRoll3 = Integer.parseInt(roll3.getText().toString());
                        }

                        ArrayList<Byte> rolls = new ArrayList<>();

                        if( pinsInRoll1 > maxFrameScore || pinsInRoll1 < 0){
                            TextInputLayout til = getDialog().findViewById(R.id.throw_1_input_layout);
                            til.setError(getResources().getString(R.string.flf_roll_format_violated) + " " + maxFrameScore + "!");
                            returnToken = true;
                        } else rolls.add((byte) pinsInRoll1);

                        if( pinsInRoll2 > maxFrameScore || pinsInRoll2 < 0 ) {
                            TextInputLayout til = getDialog().findViewById(R.id.throw_2_input_layout);
                            til.setError(getResources().getString(R.string.flf_roll_format_violated) + " " + maxFrameScore + "!");
                            returnToken = true;
                        } else if (frameNumber == maxFrames || pinsInRoll1 != maxFrameScore) {
                            rolls.add((byte) pinsInRoll2);
                        }

                        if (frameNumber == maxFrames) {
                            if (pinsInRoll3 > maxFrameScore || pinsInRoll3 < 0) {
                                TextInputLayout til = getDialog().findViewById(R.id.throw_3_input_layout);
                                til.setError(getResources().getString(R.string.flf_roll_format_violated) + " " + maxFrameScore + "!");
                                returnToken = true;
                            } else if (pinsInRoll1 + pinsInRoll2 >= maxFrameScore) {
                                rolls.add((byte) pinsInRoll3);
                            }
                        }

                        if(returnToken)
                            return;

                        if ( pinsInRoll1 + pinsInRoll2 > maxFrameScore && ( frameNumber != maxFrames || pinsInRoll1 != maxFrameScore) ) {
                            TextInputLayout til = getDialog().findViewById(R.id.throw_2_input_layout);
                            til.setError(getResources().getString(R.string.flf_regular_frame_too_many_pins_error) + " " + maxFrameScore + "!");
                            returnToken = true;
                        }

                        if (pinsInRoll1 + pinsInRoll2 < maxFrameScore
                                && frameNumber == maxFrames
                                && pinsInRoll3 > 0) {
                            TextInputLayout til = getDialog().findViewById(R.id.throw_2_input_layout);
                            til.setError(getResources().getString(R.string.flf_last_frame_invalid_extra_roll_error));
                            returnToken = true;
                        }

                        if(returnToken)
                            return;

                        frameOverview.setRolls(rolls);
                        byte score = (byte)(pinsInRoll1 + pinsInRoll2 + pinsInRoll3);
                        int resultCodeUpdateFrom = position >= 2 ? position - 2 : 0 ;
                        frameOverview.setFrameScore(score);
                        if(toCreate) {
                            frameOverviews.add(frameOverview);
                        }

                        if (getTargetFragment() != null)
                            getTargetFragment().onActivityResult(REQUEST_CODE_UPDATE_LOCALLY, resultCodeUpdateFrom, null);
                        dialog.dismiss();
                    }
                });
            }
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            if(frameOverview != null && frameOverview.getRolls()!=null && !frameOverview.getRolls().isEmpty() ) {
                String previousRoll1, previousRoll2, previousRoll3;
                int playedRolls = frameOverview.getRolls().size();
                previousRoll1 = String.format(Locale.getDefault(), "%d", frameOverview.getRolls().get(0));
                roll1.setText(previousRoll1);
                if(playedRolls > 1) {
                    previousRoll2 = String.format(Locale.getDefault(), "%d", frameOverview.getRolls().get(1));
                    roll2.setText(previousRoll2);
                }
                if(playedRolls > 2) {
                    previousRoll3 = String.format(Locale.getDefault(), "%d", frameOverview.getRolls().get(2));
                    roll3.setText(previousRoll3);
                }
            }
            roll1.setFocusableInTouchMode(true);
            roll1.requestFocus();
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    public static class EditDeleteFrameDialog extends DialogFragment {
        int position;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            position = getArguments().getInt(ExtraConstants.EXTRA_POSITION);
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
            String[] items;
            if(position == frameOverviews.size()-1){
                items = new String[]{ getActivity().getString(R.string.edit), getActivity().getString(R.string.delete) };
            } else {
                items = new String[]{ getActivity().getString(R.string.edit)};
            }
            builder.setItems( items, supplyListener());

            builder.setTitle(getArguments().getString(ExtraConstants.EXTRA_TITLE));
            return builder.create();
        }

        protected DialogInterface.OnClickListener supplyListener() {
            return new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            EditFrameListDialog dialogEdit = EditFrameListDialog.newInstance(position);
                            dialogEdit.setTargetFragment(thisFragment, 0);
                            dialogEdit.show(getFragmentManager(), "dialogEdit");
                            dialog.dismiss();
                            break;
                        case 1:
                            frameOverviews.remove(position);
                            if (getTargetFragment() != null){
                                int resultCodeUpdateFrom = position >= 2 ? position - 2 : 0 ;
                                getTargetFragment().onActivityResult(REQUEST_CODE_UPDATE_LOCALLY, resultCodeUpdateFrom, null);
                            }
                            dialog.dismiss();
                            break;
                    }
                    dialog.dismiss();
                }
            };
        }

        public static EditDeleteFrameDialog newInstance (int position, String title) {
            EditDeleteFrameDialog dialog = new EditDeleteFrameDialog();
            Bundle args = new Bundle();
            args.putInt(ExtraConstants.EXTRA_POSITION, position);
            args.putString(ExtraConstants.EXTRA_TITLE, title);
            dialog.setArguments(args);
            return dialog;
        }
    }
}
