package fit.cvut.org.cz.tmlibrary.data.interfaces;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.data.entities.DPlayer;

/**
 * Created by kevin on 4. 4. 2016.
 */
public interface IPlayerDAO {

    void insert(Context context, DPlayer competition);
    void update(Context context, DPlayer competition);
    void delete(Context context, long id);

    DPlayer getById(Context context, long id);
    ArrayList<DPlayer> getAll(Context context);
}
