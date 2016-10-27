package fit.cvut.org.cz.tmlibrary.business.helpers;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.business.entities.Conflict;
import fit.cvut.org.cz.tmlibrary.business.entities.ConflictValue;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;

/**
 * Created by kevin on 26.10.2016.
 */
public class ConflictCreator {
    public static Conflict createConflict(Player p1, Player p2) {
        Conflict c = new Conflict();
        ArrayList<ConflictValue> values = new ArrayList<>();
        c.setTitle(p1.getEmail());
        if (p1.getName().equals(p2.getName())) {
            values.add(new ConflictValue("name", p1.getName(), p2.getName()));
        }
        if (p1.getNote().equals(p2.getNote())) {
            values.add(new ConflictValue("name", p1.getNote(), p2.getNote()));
        }
        return c;
    }
}
