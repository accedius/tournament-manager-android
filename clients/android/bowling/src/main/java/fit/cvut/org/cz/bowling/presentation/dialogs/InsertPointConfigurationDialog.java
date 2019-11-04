package fit.cvut.org.cz.bowling.presentation.dialogs;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IPointConfigurationManager;
import fit.cvut.org.cz.bowling.data.entities.PointConfiguration;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;

public abstract class InsertPointConfigurationDialog extends DialogFragment {
    private ProgressBar progressBar;
    private TextView sidesNumber;
    private PointConfiguration configuration;
    private boolean forCreation;

    protected long pointConfigurationId, tournamentId;

    public static InsertPointConfigurationDialog newInstance(long id, boolean forCreation, Class<? extends InsertPointConfigurationDialog> clazz) {
        InsertPointConfigurationDialog fragment = null;
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
        args.putBoolean(ExtraConstants.EXTRA_SELECTED, forCreation);
        if (forCreation)
            args.putLong(ExtraConstants.EXTRA_TOUR_ID, id);
        else {
            IPointConfigurationManager iPointConfigurationManager = ManagerFactory.getInstance().getEntityManager(PointConfiguration.class);
            long tournamentId = iPointConfigurationManager.getById(id).tournamentId;
            args.putLong(ExtraConstants.EXTRA_TOUR_ID, tournamentId);
            args.putLong(ExtraConstants.EXTRA_ID, id);
        }
        fragment.setArguments(args);
        return fragment;
    }

    protected abstract void askForData();
    protected abstract void registerReceiver();
    protected abstract void unregisterReceiver();
    protected abstract void insertPointConfiguration(PointConfiguration t);
    protected abstract void editPointConfiguration(PointConfiguration t);
    protected abstract String getPointConfigurationKey();
    protected abstract boolean isDataSourceWorking();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setPositiveButton(fit.cvut.org.cz.tmlibrary.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //empty, because must be overridden in onResume in order not to close on bad input (https://stackoverflow.com/questions/2620444/how-to-prevent-a-dialog-from-closing-when-a-button-is-clicked)
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        View v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_insert_config, null);

        builder.setView(v);
        sidesNumber = (TextView) v.findViewById(R.id.pcv_sides_number);
        progressBar = (ProgressBar) v.findViewById(R.id.progress_spinner);
        forCreation = getArguments().getBoolean(ExtraConstants.EXTRA_SELECTED, true);
        pointConfigurationId = getArguments().getLong(ExtraConstants.EXTRA_ID, -1);
        tournamentId = getArguments().getLong(ExtraConstants.EXTRA_TOUR_ID, -1);

        if (pointConfigurationId != -1) {
            registerReceiver();
            askForData();
            progressBar.setVisibility(View.VISIBLE);
            sidesNumber.setVisibility(View.GONE);
        }

        builder.setTitle(getResources().getString(R.string.point_configuration_settings));
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
                    if (sidesNumber.getText().toString().isEmpty() || isDataSourceWorking()) {
                        return;
                    }

                    if(Long.parseLong(sidesNumber.getText().toString()) < 2){
                        TextInputLayout til = getDialog().findViewById(R.id.text_input_layout);
                        til.setError(getResources().getString(R.string.pc_format_violated));
                        /*sidesNumber.setError(getResources().getString(R.string.pc_format_violated));
                        View rootView = getActivity().findViewById(R.id.rootView);
                        Snackbar.make(rootView, R.string.pc_format_violated, Snackbar.LENGTH_LONG).show();
                        Snackbar.make(v, R.string.pc_format_violated, Snackbar.LENGTH_LONG).show();
                        Toast.makeText(getContext(),getResources().getString(R.string.pc_format_violated), Toast.LENGTH_LONG ).show();*/
                        return;
                    }

                    long sn = Long.parseLong(sidesNumber.getText().toString());
                    IPointConfigurationManager iPointConfigurationManager = ManagerFactory.getInstance(getContext()).getEntityManager(PointConfiguration.class);
                    PointConfiguration resultPC = iPointConfigurationManager.getBySidesNumber(tournamentId, sn);
                    if(resultPC != null) {
                        TextInputLayout til = getDialog().findViewById(R.id.text_input_layout);
                        til.setError(getResources().getString(R.string.pc_already_exists));
                        return;
                    }

                    if (forCreation) {
                        List<Float> pc = new ArrayList<Float>();
                        for (int i = 0; i<sn; i++) {
                            pc.add(i, 0f);
                        }
                        insertPointConfiguration(new PointConfiguration(tournamentId, sn, pc));
                    } else {
                        int csn = (int) configuration.sidesNumber;
                        List<Float> configurationPlacePoints = configuration.getConfigurationPlacePoints();

                        if(sn < csn) {
                            configurationPlacePoints.subList(((int) sn), csn).clear();
                            configuration.setSidesNumber(sn);
                            configuration.setConfigurationPlacePoints(configurationPlacePoints);
                            editPointConfiguration(configuration);
                        } else if (sn > csn) {
                            for (int i = csn; i < sn; i++) {
                                configurationPlacePoints.add(0f);
                            }
                            configuration.setSidesNumber(sn);
                            configuration.setConfigurationPlacePoints(configurationPlacePoints);
                            editPointConfiguration(configuration);
                        }
                    }
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
        sidesNumber.setFocusableInTouchMode(true);
        sidesNumber.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (pointConfigurationId != -1)
            unregisterReceiver();
        super.onDismiss(dialog);
    }

    protected BroadcastReceiver receiver = new PointConfigurationReceiver();

    public class PointConfigurationReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            progressBar.setVisibility(View.GONE);
            sidesNumber.setVisibility(View.VISIBLE);
            configuration = intent.getParcelableExtra(getPointConfigurationKey());
            sidesNumber.setText(String.format(Locale.getDefault(), "%d", configuration.getSidesNumber()) );
        }
    }
}
