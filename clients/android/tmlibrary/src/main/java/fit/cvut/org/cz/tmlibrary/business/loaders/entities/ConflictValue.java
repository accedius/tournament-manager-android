package fit.cvut.org.cz.tmlibrary.business.loaders.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Entity for ConflictValue.
 */
public class ConflictValue implements Parcelable {
    private String attribute;
    private String leftValue;
    private String rightValue;

    /**
     * ConflictValue constructor.
     * @param attribute attribute of conflict
     * @param leftValue left value
     * @param rightValue right value
     */
    public ConflictValue(String attribute, String leftValue, String rightValue) {
        this.attribute = attribute;
        this.leftValue = leftValue;
        this.rightValue = rightValue;
    }

    /**
     * Constructor from Parcel.
     * @param in parcel
     */
    protected ConflictValue(Parcel in) {
        attribute = in.readString();
        leftValue = in.readString();
        rightValue = in.readString();
    }

    /**
     * Parcelable creator.
     */
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

    /**
     * Attribute getter.
     * @return attribute
     */
    public String getAttribute() {
        return attribute;
    }

    /**
     * Attribute setter.
     * @param attribute attribute to be set
     */
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    /**
     * Left value getter.
     * @return leftValue
     */
    public String getLeftValue() {
        return leftValue;
    }

    /**
     * Left value setter.
     * @param leftValue leftValue to be set
     */
    public void setLeftValue(String leftValue) {
        this.leftValue = leftValue;
    }

    /**
     * Right value getter.
     * @return rightValue
     */
    public String getRightValue() {
        return rightValue;
    }

    /**
     * Right value setter.
     * @param rightValue rightValue to be set
     */
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
