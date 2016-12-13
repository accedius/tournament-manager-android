package fit.cvut.org.cz.tmlibrary.business.loaders.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kevin on 28.10.2016.
 */
public class PlayerImportInfo extends ImportInfo implements Parcelable {
    private String email;

    public PlayerImportInfo(String name, String email) {
        super(name);
        this.email = email;
    }

    protected PlayerImportInfo(Parcel in) {
        super(in);
        email = in.readString();
    }

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

    public String getEmail() {
        return email;
    }

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
