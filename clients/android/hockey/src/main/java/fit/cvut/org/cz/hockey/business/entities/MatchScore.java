package fit.cvut.org.cz.hockey.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by atgot_000 on 20. 4. 2016.
 */
public class MatchScore implements Parcelable {

    private long matchId;
    private int homeScore, awayScore;
    private boolean shootouts, overtime;

    public MatchScore() {}

    public MatchScore(long matchId, int homeScore, int awayScore, boolean shootouts, boolean overtime) {
        this.matchId = matchId;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.shootouts = shootouts;
        this.overtime = overtime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong( matchId );
        dest.writeInt(homeScore);
        dest.writeInt(awayScore);
        dest.writeByte((byte) (shootouts ? 1 : 0));
        dest.writeByte((byte) (overtime ? 1 : 0));
    }

    public MatchScore( Parcel in )
    {
        this.matchId = in.readLong();
        this.homeScore = in.readInt();
        this.awayScore = in.readInt();
        this.shootouts = in.readByte() != 0;
        this.overtime = in.readByte() != 0;
    }

    public static final Creator<MatchScore> CREATOR = new Creator<MatchScore>() {
        @Override
        public MatchScore createFromParcel(Parcel in) {
            return new MatchScore(in);
        }

        @Override
        public MatchScore[] newArray(int size) {
            return new MatchScore[size];
        }
    };

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }

    public boolean isShootouts() {
        return shootouts;
    }

    public void setShootouts(boolean shootouts) {
        this.shootouts = shootouts;
    }

    public boolean isOvertime() {
        return overtime;
    }

    public void setOvertime(boolean overtime) {
        this.overtime = overtime;
    }

    public long getMatchId() {
        return matchId;
    }

    public void setMatchId(long matchId) {
        this.matchId = matchId;
    }
}
