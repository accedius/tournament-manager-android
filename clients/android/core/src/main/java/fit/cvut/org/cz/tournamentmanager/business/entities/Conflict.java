package fit.cvut.org.cz.tournamentmanager.business.entities;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kevin on 24.10.2016.
 */
public class Conflict {
    private String title;
    private ArrayList<ConflictValue> values;

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
}
