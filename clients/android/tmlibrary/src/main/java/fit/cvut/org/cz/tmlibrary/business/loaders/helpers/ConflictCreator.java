package fit.cvut.org.cz.tmlibrary.business.loaders.helpers;

import android.content.res.Resources;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.R;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.Conflict;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.ConflictValue;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;

/**
 * Class for Conflict creating.
 */
public class ConflictCreator {
    /**
     * Create conflict from given Players.
     * @param p1 first Player
     * @param p2 second Player
     * @param res resources for attribute translation
     * @return created conflict
     */
    public static Conflict createConflict(Player p1, Player p2, Resources res) {
        Conflict conflict = new Conflict();
        ArrayList<ConflictValue> values = new ArrayList<>();
        conflict.setTitle(p1.getEmail());
        if (!p1.getName().equals(p2.getName())) {
            values.add(new ConflictValue(res.getString(R.string.name), p1.getName(), p2.getName()));
        }
        if (!p1.getNote().equals(p2.getNote())) {
            values.add(new ConflictValue(res.getString(R.string.note), p1.getNote(), p2.getNote()));
        }
        conflict.setValues(values);
        return conflict;
    }
}
