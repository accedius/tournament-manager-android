package fit.cvut.org.cz.hockey.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by atgot_000 on 20. 4. 2016.
 */
public class MatchPlayerStatistic implements Parcelable {

    private long playerId;
    private String name;
    private int goals, assists, plusMinusPoints, interventions;

    public MatchPlayerStatistic() {};

    public static final Creator<MatchPlayerStatistic> CREATOR = new Creator<MatchPlayerStatistic>() {
        @Override
        public MatchPlayerStatistic createFromParcel(Parcel in) {
            return new MatchPlayerStatistic(in);
        }

        @Override
        public MatchPlayerStatistic[] newArray(int size) {
            return new MatchPlayerStatistic[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(playerId);
        dest.writeString(name);
        dest.writeInt(goals);
        dest.writeInt(assists);
        dest.writeInt(plusMinusPoints);
        dest.writeInt(interventions);
    }

    public MatchPlayerStatistic( Parcel in )
    {
        this.playerId = in.readLong();
        this.name = in.readString();
        this.goals = in.readInt();
        this.assists = in.readInt();
        this.plusMinusPoints = in.readInt();
        this.interventions = in.readInt();
    }

    public int getGoals() {
        return goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public int getAssists() {
        return assists;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public int getPlusMinusPoints() {
        return plusMinusPoints;
    }

    public void setPlusMinusPoints(int plusMinusPoints) {
        this.plusMinusPoints = plusMinusPoints;
    }

    public int getInterventions() {
        return interventions;
    }

    public void setInterventions(int interventions) {
        this.interventions = interventions;
    }


    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
