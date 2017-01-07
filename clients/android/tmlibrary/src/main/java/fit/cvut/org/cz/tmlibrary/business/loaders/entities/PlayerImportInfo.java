package fit.cvut.org.cz.tmlibrary.business.loaders.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Entity for Import info about Player.
 */
public class PlayerImportInfo extends ImportInfo implements Parcelable {
    private String email;

    /**
     * PlayerImportInfo constructor.
     * @param name player name
     * @param email player email
     */
    public PlayerImportInfo(String name, String email) {
        super(name);
        this.email = email;
    }

    /**
     * Constructor from Parcel.
     * @param in parcel
     */
    protected PlayerImportInfo(Parcel in) {
        super(in);
        email = in.readString();
    }

    /**
     * Parcelable creator.
     */
    public static final Creator<PlayerImportInfo> CREATOR = new Creator<PlayerImportInfo>() {
        @Override
        public PlayerImportInfo createFromParcel(Parcel in) {
            return new PlayerImportInfo(in);
        }

        @Override
        public PlayerImportInfo[] newArray(int size) {
            return new PlayerImportInfo[size];
        }
    };

    /**
     * Email getter.
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Email setter.
     * @param email email to be set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
    }
}
