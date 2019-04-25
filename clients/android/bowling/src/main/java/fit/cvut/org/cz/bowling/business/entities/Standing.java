package fit.cvut.org.cz.bowling.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

import fit.cvut.org.cz.bowling.business.entities.communication.Constants;

public class Standing implements Parcelable {
    private String name;
    private int wins, wins_ot, wins_so, losses, losses_ot, losses_so, draws, points, goalsGiven, goalsReceived;
    private long teamId;

    public Standing() {}

    public Standing(String name, long teamId) {
        this(name, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, teamId);
    }

    public Standing(String name, int wins, int wins_ot, int wins_so, int losses, int losses_ot, int losses_so, int draws, int points, int goalsGiven, int goalsReceived, long teamId) {
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
    public void addPoints(long points) { this.points += points; }
    public void addGoalsGiven(long gg) { this.goalsGiven += gg; }
    public void addGoalsReceived(long gr) { this.goalsReceived += gr; }

    public int getMatches() {
        return getTotalWins()+getDraws()+getTotalLosses();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWins() {
        return wins;
    }

    public int getTotalWins() { return wins+wins_ot+wins_so; }

    public int getWinsOt() { return wins_ot; }

    public int getWinsSo() { return wins_so; }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getTotalLosses() { return losses+losses_ot+losses_so; }

    public int getLossesOt() {
        return losses_ot;
    }

    public int getLossesSo() {
        return losses_so;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getGoalsGiven() {
        return goalsGiven;
    }

    public void setGoalsGiven(int goalsGiven) {
        this.goalsGiven = goalsGiven;
    }

    public int getGoalsReceived() {
        return goalsReceived;
    }

    public void setGoalsReceived(int goalsReceived) {
        this.goalsReceived = goalsReceived;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(wins);
        dest.writeInt(wins_ot);
        dest.writeInt(wins_so);
        dest.writeInt(losses);
        dest.writeInt(losses_ot);
        dest.writeInt(losses_so);
        dest.writeInt(draws);
        dest.writeInt(goalsGiven);
        dest.writeInt(goalsReceived);
        dest.writeInt(points);
        dest.writeLong(teamId);
    }

    public Standing(Parcel in) {
        this.name = in.readString();
        this.wins = in.readInt();
        this.wins_ot = in.readInt();
        this.wins_so = in.readInt();
        this.losses = in.readInt();
        this.losses_ot = in.readInt();
        this.losses_so = in.readInt();
        this.draws = in.readInt();
        this.goalsGiven = in.readInt();
        this.goalsReceived = in.readInt();
        this.points = in.readInt();
        this.teamId = in.readLong();
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
            case Constants.MATCHES: return getMatches();
            case Constants.POINTS: return getPoints();
            case Constants.WINS_TOTAL: return getTotalWins();
            case Constants.LOSSES_TOTAL: return getTotalLosses();
            case Constants.WINS: return getWins();
            case Constants.DRAWS: return getDraws();
            case Constants.LOSSES: return getLosses();
            case Constants.WINS_OT: return getWinsOt();
            case Constants.LOSSES_OT: return getLossesOt();
            case Constants.WINS_SO: return getWinsSo();
            case Constants.LOSSES_SO: return getLossesSo();
            case Constants.SCORE: return getGoalsGiven()-getGoalsReceived();
            default: return 0;
        }
    }
}
