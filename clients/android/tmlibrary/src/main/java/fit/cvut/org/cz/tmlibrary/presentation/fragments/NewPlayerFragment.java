package fit.cvut.org.cz.tmlibrary.presentation.fragments;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import fit.cvut.org.cz.tmlibrary.R;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;

/**
 * Created by kevin on 4. 4. 2016.
 */
public abstract class NewPlayerFragment extends AbstractDataFragment {

    private static final String ARG_ID = "arg_id";

    /**
     * Constructor for this fragment with id of Player that needs to update
     * @param id
     * @param clazz
     * @return
     */
    public static NewPlayerFragment newInstance(long id, Class<? extends NewPlayerFragment> clazz){
        NewPlayerFragment fragment = null;
        try {
            Constructor<? extends NewPlayerFragment> c = clazz.getConstructor();
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
        args.putLong(ARG_ID, id);

        fragment.setArguments(args);
        return fragment;
    }

    private EditText note, name, email;
    private FloatingActionButton fab;
    protected long PlayerId = -1;

    @Override
    protected View injectView(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.fragment_new_player, container, false);

        note = (EditText) v.findViewById(R.id.et_note);
        name = (EditText) v.findViewById(R.id.et_name);
        email = (EditText) v.findViewById(R.id.et_email);

        if (getArguments() != null)
            PlayerId = getArguments().getLong(ARG_ID , -1);

        fab = (FloatingActionButton) v.findViewById(R.id.fab_edit);
        //tilNote = (TextInputLayout) v.findViewById(R.id.til_note);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDataSourceWorking() && validate(v)){
                    if (PlayerId == -1){
                        Player = new Player(PlayerId, name.getText().toString(), email.getText().toString(), note.getText().toString());
                        savePlayer(Player);
                    }
                    else{
                        Player.setName(name.getText().toString());
                        Player.setEmail(email.getText().toString());
                        Player.setNote(note.getText().toString());
                        updatePlayer(Player);
                    }
                    getActivity().finish();
                }
            }
        });

        return v;
    }

    private Player Player = null;

    private boolean validate(View v){
        if (name.getText().toString().isEmpty()){
            Snackbar.make(v, R.string.invalidName, Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (email.getText().toString().isEmpty()){
            Snackbar.make(v, R.string.invalidEmail, Snackbar.LENGTH_LONG).show();
            return false;
        }
        Snackbar.make(v, "PlayerCreated", Snackbar.LENGTH_SHORT).show();
        return true;
    }

    @Override
    protected void customOnResume() {
        if (PlayerId != -1)
            super.customOnResume();
    }

    @Override
    protected void customOnPause() {
        if (PlayerId != -1)
            super.customOnPause();
    }

    /**
     * Called with new Player in param
     * @param c
     */
    protected abstract void savePlayer(Player c);

    /**
     * Called when Player in param should be updated
     * @param c
     */
    protected abstract void updatePlayer(Player c);

    /**
     *
     * @return String key of Player gotten in Bundle of Intent when receiving from service
     */
    protected abstract String getPlayerKey();

    @Override
    protected void bindDataOnView(Intent intent) {
        Player = intent.getParcelableExtra(getPlayerKey());
        bindPlayerOnView(Player);
    }

    private void bindPlayerOnView(Player c){
        name.setText(c.getName());
        note.setText(c.getNote());
        email.setText(c.getEmail());
    }
}
