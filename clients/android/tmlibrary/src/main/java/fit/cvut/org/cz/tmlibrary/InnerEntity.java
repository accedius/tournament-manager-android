package fit.cvut.org.cz.tmlibrary;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vaclav on 8. 3. 2016.
 */
public class InnerEntity implements Parcelable {

    private String innerName;

    public InnerEntity(String innerName) {
        this.innerName = innerName;
    }

    protected InnerEntity(Parcel in) {
        innerName = in.readString();
    }

    public static final Creator<InnerEntity> CREATOR = new Creator<InnerEntity>() {
        @Override
        public InnerEntity createFromParcel(Parcel in) {
            return new InnerEntity(in);
        }

        @Override
        public InnerEntity[] newArray(int size) {
            return new InnerEntity[size];
        }
    };

    public String getInnerName() {
        return innerName;
    }

    public void setInnerName(String innerName) {
        this.innerName = innerName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(innerName);
    }

    @Override
    public String toString() {
        return "InnerEntity{" +
                "innerName='" + innerName + '\'' +
                '}';
    }
}
