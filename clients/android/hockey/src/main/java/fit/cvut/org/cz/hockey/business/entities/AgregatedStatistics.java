package fit.cvut.org.cz.hockey.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by atgot_000 on 7. 4. 2016.
 */
public class AgregatedStatistics implements Parcelable {

    private long playerID;
    private String playerName;

    private long goals, assists, points, plusMinusPoints, teamPoints, matches, wins, losses, draws;
    private double avgGoals, avgPoints, avgPlusMinus, avgTeamPoints;

    private void calcAvg()
    {
        this.points = this.goals + this.assists;
        if( this.matches > 0 )
        {
            this.avgGoals = this.goals/this.matches;
            this.avgPoints = this.points/this.matches;
            this.avgPlusMinus = this.plusMinusPoints/this.matches;
            this.avgTeamPoints = this.teamPoints/this.matches;
        }
        else
        {
            this.avgGoals = 0;
            this.avgPoints = 0;
            this.avgPlusMinus = 0;
            this.avgTeamPoints = 0;
        }

    }

    public AgregatedStatistics( long pID, String pName, long matches, long wins, long draws, long losses, long goals, long assists, long plusMinusPoints, long teamPoints )
    {
        this.playerID = pID;
        this.playerName = pName;
        this.matches = matches;
        this.wins = wins;
        this.draws = draws;
        this.losses = losses;
        this.goals = goals;
        this.assists = assists;
        this.plusMinusPoints = plusMinusPoints;
        this.teamPoints = teamPoints;
        calcAvg();
    }

    public static final Creator<AgregatedStatistics> CREATOR = new Creator<AgregatedStatistics>() {
        @Override
        public AgregatedStatistics createFromParcel(Parcel in) {
            return new AgregatedStatistics(in);
        }

        @Override
        public AgregatedStatistics[] newArray(int size) {
            return new AgregatedStatistics[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public AgregatedStatistics( Parcel in )
    {

    }

}
