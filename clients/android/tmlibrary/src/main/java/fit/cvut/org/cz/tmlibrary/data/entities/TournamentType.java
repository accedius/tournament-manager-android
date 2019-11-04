package fit.cvut.org.cz.tmlibrary.data.entities;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class for handling tournament type
 */
public class TournamentType implements Parcelable {
    public int id;
    public int value;
    private Resources res = null;

    public TournamentType(Resources r, int id, int value) {
        this.res = r;
        this.id = id;
        this.value = value;
    }

    protected TournamentType(Parcel in) {
        id = in.readInt();
        value = in.readInt();
    }

    public static final Creator<TournamentType> CREATOR = new Creator<TournamentType>() {
        @Override
        public TournamentType createFromParcel(Parcel in) {
            return new TournamentType(in);
        }

        @Override
        public TournamentType[] newArray(int size) {
            return new TournamentType[size];
        }
    };

    public boolean equals(TournamentType ct) {
        if (ct == null)
            return false;
        return id == ct.id;
    }

    @Override
    public String toString() {
        if (res == null)
            return "";
        return res.getString(value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(value);
    }
}
