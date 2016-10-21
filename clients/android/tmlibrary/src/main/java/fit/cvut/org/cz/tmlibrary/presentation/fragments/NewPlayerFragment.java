package fit.cvut.org.cz.tmlibrary.presentation.fragments;

import android.content.Intent;
import android.os.Bundle;
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
    protected static final String ARG_ID = "arg_id";

    /**
     * Constructor for this fragment with id of player that needs to update
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

    protected EditText note, name, email;
    protected long playerId = -1;
    protected View v;

    protected Player player = null;

    @Override
    protected View injectView(LayoutInflater inflater, ViewGroup container) {
        v = inflater.inflate(R.layout.fragment_new_player, container, false);

        note = (EditText) v.findViewById(R.id.et_note);
        name = (EditText) v.findViewById(R.id.et_name);
        email = (EditText) v.findViewById(R.id.et_email);

        if (getArguments() != null)
            playerId = getArguments().getLong(ARG_ID , -1);

        return v;
    }

    @Override
    public void customOnResume() {
        if (playerId != -1)
            super.customOnResume();
    }

    @Override
    protected void customOnPause() {
        if (playerId != -1)
            super.customOnPause();
    }

    /**
     *
     * @return String key of player gotten in Bundle of Intent when receiving from service
     */
    protected abstract String getPlayerKey();

    @Override
    protected void bindDataOnView(Intent intent) {
        player = intent.getParcelableExtra(getPlayerKey());
        bindPlayerOnView(player);
    }

    private void bindPlayerOnView(Player c){
        name.setText(c.getName());
        note.setText(c.getNote());
        email.setText(c.getEmail());
    }
}
