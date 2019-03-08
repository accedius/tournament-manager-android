package fit.cvut.org.cz.tmlibrary.business.loaders.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Entity for Import info.
 */
public class ImportInfo implements Parcelable {
    /**
     * Info name.
     */
    protected String name;

    /**
     * ImportInfo constructor.
     * @param name info name
     */
    public ImportInfo(String name) {
        this.name = name;
    }

    /**
     * Constructor from Parcel.
     * @param in parcel
     */
    protected ImportInfo(Parcel in) {
        name = in.readString();
    }

    /**
     * Parcelable creator.
     */
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

    /**
     * Name getter.
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Name setter.
     * @param name name to be set
     */
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
