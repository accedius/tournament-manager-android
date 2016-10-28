package fit.cvut.org.cz.tmlibrary.business.entities;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.business.entities.ConflictValue;

/**
 * Created by kevin on 24.10.2016.
 */
public class Conflict {
    public static final String KEEP_LOCAL = "keep";
    public static final String TAKE_FILE = "take";

    private String title;
    private ArrayList<ConflictValue> values;
    private String action = KEEP_LOCAL;

    public Conflict() {}

    public Conflict(String title, ArrayList<ConflictValue> values) {
        this.title = title;
        this.values = values;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<ConflictValue> getValues() {
        return values;
    }

    public void setValues(ArrayList<ConflictValue> values) {
        this.values = values;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
