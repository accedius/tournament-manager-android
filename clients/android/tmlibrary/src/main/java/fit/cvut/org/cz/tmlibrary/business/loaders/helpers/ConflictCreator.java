package fit.cvut.org.cz.tmlibrary.business.loaders.helpers;

import android.content.res.Resources;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.R;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.Conflict;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.ConflictValue;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;

/**
 * Created by kevin on 26.10.2016.
 */
public class ConflictCreator {
    public static Conflict createConflict(Player p1, Player p2, Resources res) {
        Conflict c = new Conflict();
        ArrayList<ConflictValue> values = new ArrayList<>();
        c.setTitle(p1.getEmail());
        if (!p1.getName().equals(p2.getName())) {
            values.add(new ConflictValue(res.getString(R.string.name), p1.getName(), p2.getName()));
        }
        if (!p1.getNote().equals(p2.getNote())) {
            values.add(new ConflictValue(res.getString(R.string.note), p1.getNote(), p2.getNote()));
        }
        c.setValues(values);
        return c;
    }
}
