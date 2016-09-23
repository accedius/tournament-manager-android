package fit.cvut.org.cz.hockey.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by atgot_000 on 19. 4. 2016.
 */
public class Standing implements Parcelable {
    private String name;
    private Long wins, wins_ot, wins_so, losses, losses_ot, losses_so, draws, points, goalsGiven, goalsReceived;
    private long teamId;

    public Standing() {}

    public Standing(String name, Long wins, Long wins_ot, Long wins_so, Long losses, Long losses_ot, Long losses_so, Long draws, Long points, Long goalsGiven, Long goalsReceived, long teamId) {
        this.name = name;
        this.wins = wins;
        this.wins_ot = wins_ot;
        this.wins_so = wins_so;
        this.losses = losses;
        this.losses_ot = losses_ot;
        this.losses_so = losses_so;
        this.draws = draws;
        this.points = points;
        this.goalsGiven = goalsGiven;
        this.goalsReceived = goalsReceived;
        this.teamId = teamId;
    }

    public void addWin() { this.wins++; }
    public void addWinOt() { this.wins_ot++; }
    public void addWinSo() { this.wins_so++; }
    public void addLoss() { this.losses++; }
    public void addLossOt() { this.losses_ot++; }
    public void addLossSo() { this.losses_so++; }
    public void addDraw() { this.draws++; }
    public void addPoints( long points ) { this.points += points; }
    public void addGoalsGiven( long gg ) { this.goalsGiven += gg; }
    public void addGoalsReceived( long gr ) { this.goalsReceived += gr; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getWins() {
        return wins;
    }

    public Long getTotalWins() { return wins+wins_ot+wins_so; }

    public Long getWinsOt() { return wins_ot; }

    public Long getWinsSo() { return wins_so; }

    public void setWins(Long wins) {
        this.wins = wins;
    }

    public Long getLosses() {
        return losses;
    }

    public Long getTotalLosses() { return losses+losses_ot+losses_so; }

    public Long getLossesOt() {
        return losses_ot;
    }

    public Long getLossesSo() {
        return losses_so;
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

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public double getStat(String key) {
        switch (key) {
            case "p": return getPoints();
            case "tw": return getTotalWins();
            case "tl": return getTotalLosses();
            case "w": return getWins();
            case "d": return getDraws();
            case "l": return getLosses();
            case "wot": return getWinsOt();
            case "lot": return getLossesOt();
            case "wso": return getWinsSo();
            case "lso": return getLossesSo();
            default: return 0;
        }
    }
}
