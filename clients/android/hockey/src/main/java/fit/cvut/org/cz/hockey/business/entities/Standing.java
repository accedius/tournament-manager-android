package fit.cvut.org.cz.hockey.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by atgot_000 on 19. 4. 2016.
 */
public class Standing implements Parcelable {
    private String name;
    private Long wins, losses, draws, points, goalsGiven, goalsReceived;

    public Standing() {}

    public Standing(String name, Long wins, Long losses, Long draws, Long points, Long goalsGiven, Long goalsReceived) {
        this.name = name;
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
        this.points = points;
        this.goalsGiven = goalsGiven;
        this.goalsReceived = goalsReceived;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getWins() {
        return wins;
    }

    public void setWins(Long wins) {
        this.wins = wins;
    }

    public Long getLosses() {
        return losses;
    }

    public void setLosses(Long losses) {
        this.losses = losses;
    }

    public Long getDraws() {
        return draws;
    }

    public void setDraws(Long draws) {
        this.draws = draws;
    }

    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }

    public Long getGoalsGiven() {
        return goalsGiven;
    }

    public void setGoalsGiven(Long goalsGiven) {
        this.goalsGiven = goalsGiven;
    }

    public Long getGoalsReceived() {
        return goalsReceived;
    }

    public void setGoalsReceived(Long goalsReceived) {
        this.goalsReceived = goalsReceived;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString( name );
        dest.writeLong( wins );
        dest.writeLong( losses );
        dest.writeLong( draws );
        dest.writeLong( goalsGiven );
        dest.writeLong( goalsReceived );
        dest.writeLong( points );
    }

    public Standing(Parcel in)
    {
        this.name = in.readString();
        this.wins = in.readLong();
        this.losses = in.readLong();
        this.draws = in.readLong();
        this.goalsGiven = in.readLong();
        this.goalsReceived = in.readLong();
        this.points = in.readLong();
    }

    public static final Creator<Standing> CREATOR = new Creator<Standing>() {
        @Override
        public Standing createFromParcel(Parcel in) {
            return new Standing(in);
        }

        @Override
        public Standing[] newArray(int size) {
            return new Standing[size];
        }
    };
}
