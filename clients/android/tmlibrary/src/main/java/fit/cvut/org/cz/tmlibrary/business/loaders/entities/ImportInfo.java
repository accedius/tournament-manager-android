package fit.cvut.org.cz.tmlibrary.business.loaders.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kevin on 28.10.2016.
 */
public class ImportInfo implements Parcelable {
    protected String name;

    public ImportInfo(String name) {
        this.name = name;
    }

    protected ImportInfo(Parcel in) {
        name = in.readString();
    }

    public static final Creator<ImportInfo> CREATOR = new Creator<ImportInfo>() {
        @Override
        public ImportInfo createFromParcel(Parcel in) {
            return new ImportInfo(in);
        }

        @Override
        public ImportInfo[] newArray(int size) {
            return new ImportInfo[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }
}
