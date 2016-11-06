package fit.cvut.org.cz.tmlibrary.business.enums;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import fit.cvut.org.cz.tmlibrary.data.DBConstants;

/**
 * Created by Vaclav on 2. 4. 2016.
 */
public class CompetitionType implements Parcelable {
    public int id;
    public int value;
    private Resources res = null;

    CompetitionType(Resources r, int id, int value) {
        this.res = r;
        this.id = id;
        this.value = value;
    }

    protected CompetitionType(Parcel in) {
        id = in.readInt();
        value = in.readInt();
    }

    public static final Creator<CompetitionType> CREATOR = new Creator<CompetitionType>() {
        @Override
        public CompetitionType createFromParcel(Parcel in) {
            return new CompetitionType(in);
        }

        @Override
        public CompetitionType[] newArray(int size) {
            return new CompetitionType[size];
        }
    };

    public boolean equals(CompetitionType ct) {
        return id == ct.id;
    }

    @Override
    public String toString() {
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