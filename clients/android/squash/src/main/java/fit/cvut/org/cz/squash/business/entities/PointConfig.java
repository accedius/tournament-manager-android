package fit.cvut.org.cz.squash.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

import fit.cvut.org.cz.squash.data.entities.DPointConfig;

/**
 * Created by Vaclav on 19. 4. 2016.
 */
public class PointConfig implements Parcelable {

    private long tournamentId;
    private int win, draw, loss;

    public PointConfig(long tournamentId, int win, int draw, int loss) {
        this.tournamentId = tournamentId;
        this.win = win;
        this.draw = draw;
        this.loss = loss;
    }

    public PointConfig(DPointConfig c){
        tournamentId = c.getTournamentId();
        win = c.getWin();
        draw = c.getDraw();
        loss = c.getLoss();
    }

    protected PointConfig(Parcel in) {
        tournamentId = in.readLong();
        win = in.readInt();
        draw = in.readInt();
        loss = in.readInt();
    }

    public static final Creator<PointConfig> CREATOR = new Creator<PointConfig>() {
        @Override
        public PointConfig createFromParcel(Parcel in) {
            return new PointConfig(in);
        }

        @Override
        public PointConfig[] newArray(int size) {
            return new PointConfig[size];
        }
    };

    public static DPointConfig convertToDPointConfig(PointConfig p){
        return new DPointConfig(p.getTournamentId(), p.getWin(), p.getDraw(), p.getLoss());
    }

    public long getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(long tournamentId) {
        this.tournamentId = tournamentId;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getLoss() {
        return loss;
    }

    public void setLoss(int loss) {
        this.loss = loss;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(tournamentId);
        dest.writeInt(win);
        dest.writeInt(draw);
        dest.writeInt(loss);
    }
}
