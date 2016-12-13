package fit.cvut.org.cz.tmlibrary.business.loaders.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kevin on 26.10.2016.
 */
public class ConflictValue implements Parcelable {
    private String attribute;
    private String leftValue;
    private String rightValue;

    public ConflictValue(String attribute, String leftValue, String rightValue) {
        this.attribute = attribute;
        this.leftValue = leftValue;
        this.rightValue = rightValue;
    }

    protected ConflictValue(Parcel in) {
        attribute = in.readString();
        leftValue = in.readString();
        rightValue = in.readString();
    }

    public static final Creator<ConflictValue> CREATOR = new Creator<ConflictValue>() {
        @Override
        public ConflictValue createFromParcel(Parcel in) {
            return new ConflictValue(in);
        }

        @Override
        public ConflictValue[] newArray(int size) {
            return new ConflictValue[size];
        }
    };

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getLeftValue() {
        return leftValue;
    }

    public void setLeftValue(String leftValue) {
        this.leftValue = leftValue;
    }

    public String getRightValue() {
        return rightValue;
    }

    public void setRightValue(String rightValue) {
        this.rightValue = rightValue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(attribute);
        dest.writeString(leftValue);
        dest.writeString(rightValue);
    }
}
