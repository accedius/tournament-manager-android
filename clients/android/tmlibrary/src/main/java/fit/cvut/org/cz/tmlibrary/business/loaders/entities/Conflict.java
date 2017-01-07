package fit.cvut.org.cz.tmlibrary.business.loaders.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Entity for Conflict.
 */
public class Conflict implements Parcelable {
    /**
     * Action for keep local data.
     */
    public static final String KEEP_LOCAL = "keep";
    /**
     * Action for take file data.
     */
    public static final String TAKE_FILE = "take";

    private String title;
    private ArrayList<ConflictValue> values;
    private String action = KEEP_LOCAL;

    /**
     * Empty Conflict constructor.
     */
    public Conflict() {}

    /**
     * Conflict constructor.
     * @param title conflict title
     * @param values list of conflict values
     */
    public Conflict(String title, ArrayList<ConflictValue> values) {
        this.title = title;
        this.values = values;
    }

    /**
     * Constructor from Parcel.
     * @param in parcel
     */
    protected Conflict(Parcel in) {
        title = in.readString();
        action = in.readString();
        values = in.createTypedArrayList(ConflictValue.CREATOR);
    }

    /**
     * Parcelable creator.
     */
    public static final Creator<Conflict> CREATOR = new Creator<Conflict>() {
        @Override
        public Conflict createFromParcel(Parcel in) {
            return new Conflict(in);
        }

        @Override
        public Conflict[] newArray(int size) {
            return new Conflict[size];
        }
    };

    /**
     * Title getter.
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Title setter.
     * @param title title to be set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Values getter.
     * @return values
     */
    public ArrayList<ConflictValue> getValues() {
        return values;
    }

    /**
     * Values setter.
     * @param values values to be set
     */
    public void setValues(ArrayList<ConflictValue> values) {
        this.values = values;
    }

    /**
     * Action getter.
     * @return action
     */
    public String getAction() {
        return action;
    }

    /**
     * Action setter.
     * @param action action to be set
     */
    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(action);
        dest.writeTypedList(values);
    }
}
