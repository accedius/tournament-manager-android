package fit.cvut.org.cz.tmlibrary.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by kevin on 24.10.2016.
 */
public class Conflict implements Parcelable {
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

    protected Conflict(Parcel in) {
        title = in.readString();
        action = in.readString();
        values = in.createTypedArrayList(ConflictValue.CREATOR);
    }

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
