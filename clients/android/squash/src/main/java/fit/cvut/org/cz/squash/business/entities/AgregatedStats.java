package fit.cvut.org.cz.squash.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vaclav on 7. 4. 2016.
 */
public class AgregatedStats implements Parcelable {

    private long playerId;
    private String playerName;

    private int won, lost, draws, setsWon, setsLost, ballsWon, ballsLost;
    private double setsWonAvg, setsLostAvg, ballsWonAvg, ballsLostAvg, matchWinRate, setsWinRate;

    public AgregatedStats(long playerId, String playerName, int won, int lost, int draws, int setsWon, int setsLost, int ballsWon, int ballsLost, double setsWonAvg, double setsLostAvg, double ballsWonAvg, double ballsLostAvg, double matchWinRate, double setsWinRate) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.won = won;
        this.lost = lost;
        this.draws = draws;
        this.setsWon = setsWon;
        this.setsLost = setsLost;
        this.ballsWon = ballsWon;
        this.ballsLost = ballsLost;
        this.setsWonAvg = setsWonAvg;
        this.setsLostAvg = setsLostAvg;
        this.ballsWonAvg = ballsWonAvg;
        this.ballsLostAvg = ballsLostAvg;
        this.matchWinRate = matchWinRate;
        this.setsWinRate = setsWinRate;
    }

    protected AgregatedStats(Parcel in) {
        playerId = in.readLong();
        playerName = in.readString();
        won = in.readInt();
        lost = in.readInt();
        draws = in.readInt();
        setsWon = in.readInt();
        setsLost = in.readInt();
        ballsWon = in.readInt();
        ballsLost = in.readInt();
        setsWonAvg = in.readDouble();
        setsLostAvg = in.readDouble();
        ballsWonAvg = in.readDouble();
        ballsLostAvg = in.readDouble();
        matchWinRate = in.readDouble();
        setsWinRate = in.readDouble();
    }

    public static final Creator<AgregatedStats> CREATOR = new Creator<AgregatedStats>() {
        @Override
        public AgregatedStats createFromParcel(Parcel in) {
            return new AgregatedStats(in);
        }

        @Override
        public AgregatedStats[] newArray(int size) {
            return new AgregatedStats[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(playerId);
        dest.writeString(playerName);
        dest.writeInt(won);
        dest.writeInt(lost);
        dest.writeInt(draws);
        dest.writeInt(setsWon);
        dest.writeInt(setsLost);
        dest.writeInt(ballsWon);
        dest.writeInt(ballsLost);
        dest.writeDouble(setsWonAvg);
        dest.writeDouble(setsLostAvg);
        dest.writeDouble(ballsWonAvg);
        dest.writeDouble(ballsLostAvg);
        dest.writeDouble(matchWinRate);
        dest.writeDouble(setsWinRate);
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getWon() {
        return won;
    }

    public void setWon(int won) {
        this.won = won;
    }

    public int getLost() {
        return lost;
    }

    public void setLost(int lost) {
        this.lost = lost;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public int getSetsWon() {
        return setsWon;
    }

    public void setSetsWon(int setsWon) {
        this.setsWon = setsWon;
    }

    public int getSetsLost() {
        return setsLost;
    }

    public void setSetsLost(int setsLost) {
        this.setsLost = setsLost;
    }

    public int getBallsWon() {
        return ballsWon;
    }

    public void setBallsWon(int ballsWon) {
        this.ballsWon = ballsWon;
    }

    public int getBallsLost() {
        return ballsLost;
    }

    public void setBallsLost(int ballsLost) {
        this.ballsLost = ballsLost;
    }

    public double getSetsWonAvg() {
        return setsWonAvg;
    }

    public void setSetsWonAvg(double setsWonAvg) {
        this.setsWonAvg = setsWonAvg;
    }

    public double getSetsLostAvg() {
        return setsLostAvg;
    }

    public void setSetsLostAvg(double setsLostAvg) {
        this.setsLostAvg = setsLostAvg;
    }

    public double getBallsWonAvg() {
        return ballsWonAvg;
    }

    public void setBallsWonAvg(double ballsWonAvg) {
        this.ballsWonAvg = ballsWonAvg;
    }

    public double getBallsLostAvg() {
        return ballsLostAvg;
    }

    public void setBallsLostAvg(double ballsLostAvg) {
        this.ballsLostAvg = ballsLostAvg;
    }

    public double getMatchWinRate() {
        return matchWinRate;
    }

    public void setMatchWinRate(double matchWinRate) {
        this.matchWinRate = matchWinRate;
    }

    public double getSetsWinRate() {
        return setsWinRate;
    }

    public void setSetsWinRate(double setsWinRate) {
        this.setsWinRate = setsWinRate;
    }
}
