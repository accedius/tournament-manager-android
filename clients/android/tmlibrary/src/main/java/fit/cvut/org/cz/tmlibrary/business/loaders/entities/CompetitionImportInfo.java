package fit.cvut.org.cz.tmlibrary.business.loaders.entities;

import android.os.Parcel;
import android.os.Parcelable;

import fit.cvut.org.cz.tmlibrary.data.entities.CompetitionType;

/**
 * Entity for Import info about Competition.
 */
public class CompetitionImportInfo extends ImportInfo implements Parcelable {

    private CompetitionType type;

    /**
     * CompetitionImportInfo constructor.
     * @param name competition name
     * @param type competition type
     */
    public CompetitionImportInfo(String name, CompetitionType type) {
        super(name);
        this.type = type;
    }

    /**
     * Constructor from Parcel.
     * @param in parcel
     */
    protected CompetitionImportInfo(Parcel in) {
        super(in);
        type = in.readParcelable(CompetitionType.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeParcelable(type, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Parcelable creator.
     */
    public static final Creator<CompetitionImportInfo> CREATOR = new Creator<CompetitionImportInfo>() {
        @Override
        public CompetitionImportInfo createFromParcel(Parcel in) {
            return new CompetitionImportInfo(in);
        }

        @Override
        public CompetitionImportInfo[] newArray(int size) {
            return new CompetitionImportInfo[size];
        }
    };

    /**
     * Type getter.
     * @return type
     */
    public CompetitionType getType() {
        return type;
    }

    /**
     * Type setter.
     * @param type type to be set
     */
    public void setType(CompetitionType type) {
        this.type = type;
    }
}
