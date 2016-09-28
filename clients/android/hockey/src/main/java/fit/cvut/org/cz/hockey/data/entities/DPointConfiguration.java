package fit.cvut.org.cz.hockey.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by atgot_000 on 11. 4. 2016.
 */
public class DPointConfiguration implements Parcelable {
    public Long ntW, ntD, ntL, otW, otD, otL, soW, soL;

    public static final Creator<DPointConfiguration> CREATOR = new Creator<DPointConfiguration>() {
        @Override
        public DPointConfiguration createFromParcel(Parcel in) {
            return new DPointConfiguration(in);
        }

        @Override
        public DPointConfiguration[] newArray(int size) {
            return new DPointConfiguration[size];
        }
    };

    public DPointConfiguration(Parcel in) {
        this.ntW = in.readLong();
        this.ntD = in.readLong();
        this.ntL = in.readLong();

        this.otW = in.readLong();
        this.otD = in.readLong();
        this.otL = in.readLong();

        this.soW = in.readLong();
        this.soL = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(ntW);
        dest.writeLong(ntD);
        dest.writeLong(ntL);

        dest.writeLong(otW);
        dest.writeLong(otD);
        dest.writeLong(otL);

        dest.writeLong(soW);
        dest.writeLong(soL);
    }

    public DPointConfiguration(Long nW, Long nD, Long nL, Long oW, Long oD, Long oL, Long sW, Long sL) {
        this.ntW = nW;
        this.ntD = nD;
        this.ntL = nL;

        this.otW = oW;
        this.otD = oD;
        this.otL = oL;

        this.soW = sW;
        this.soL = sL;
    }
}
