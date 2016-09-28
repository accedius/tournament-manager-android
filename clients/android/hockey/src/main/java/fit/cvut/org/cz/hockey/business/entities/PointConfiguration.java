package fit.cvut.org.cz.hockey.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

import fit.cvut.org.cz.hockey.data.entities.DPointConfiguration;

/**
 * Created by atgot_000 on 11. 4. 2016.
 */
public class PointConfiguration implements Parcelable {
    public Long ntW, ntD, ntL, otW, otD, otL, soW, soL;

    public static final Creator<PointConfiguration> CREATOR = new Creator<PointConfiguration>() {
        @Override
        public PointConfiguration createFromParcel(Parcel in) {
            return new PointConfiguration(in);
        }

        @Override
        public PointConfiguration[] newArray(int size) {
            return new PointConfiguration[size];
        }
    };

    public static DPointConfiguration convertToDPointConfiguration(PointConfiguration p) {
        return new DPointConfiguration(p.ntW, p.ntD, p.ntL, p.otW, p.otD, p.otL, p.soW, p.soL);
    }

    public PointConfiguration(Parcel in) {
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

    public PointConfiguration(Long nW, Long nD, Long nL, Long oW, Long oD, Long oL, Long sW, Long sL) {
        this.ntW = nW;
        this.ntD = nD;
        this.ntL = nL;

        this.otW = oW;
        this.otD = oD;
        this.otL = oL;

        this.soW = sW;
        this.soL = sL;
    }

    public PointConfiguration (DPointConfiguration dp) {
        this.ntW = dp.ntW;
        this.ntD = dp.ntD;
        this.ntL = dp.ntL;

        this.otW = dp.otW;
        this.otD = dp.otD;
        this.otL = dp.otL;

        this.soW = dp.soW;
        this.soL = dp.soL;
    }
}
