package fit.cvut.org.cz.tmlibrary.business.interfaces;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.business.entities.Player;

/**
 * Created by kevin on 4. 4. 2016.
 */
public interface IPlayerManager {

    void insert(Context context, Player player);
    void update(Context context, Player player);
    void delete(Context context, long id);

    Player getById(Context context, long id);
    ArrayList<Player> getAll(Context context);
}
