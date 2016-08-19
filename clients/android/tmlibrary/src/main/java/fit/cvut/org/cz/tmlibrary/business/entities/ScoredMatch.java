package fit.cvut.org.cz.tmlibrary.business.entities;

import android.os.Parcel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import fit.cvut.org.cz.tmlibrary.business.CompetitionType;
import fit.cvut.org.cz.tmlibrary.business.DateFormatFactory;
import fit.cvut.org.cz.tmlibrary.data.entities.DMatch;

/**
 * Created by Vaclav on 9. 4. 2016.
 */
public class ScoredMatch extends Match {

    private long homeParticipantId, awayParticipantId;

    private ArrayList<Long> homeIds, awayIds;
    private int homeScore, awayScore;
    private String homeName, awayName;

    public ScoredMatch() {}
    public ScoredMatch(DMatch match) {super(match);}

    public ScoredMatch(Parcel in) {
        super(in);
        homeParticipantId = in.readLong();
        awayParticipantId = in.readLong();
        homeName = in.readString();
        awayName = in.readString();
        homeScore = in.readInt();
        awayScore = in.readInt();

        long[] winners = in.createLongArray();
        homeIds = new ArrayList<>();
        for (long winner : winners) homeIds.add(winner);

        long[] losers = in.createLongArray();
        awayIds = new ArrayList<>();
        for (long loser : losers) awayIds.add(loser);
    }

    public static final Creator<ScoredMatch> CREATOR = new Creator<ScoredMatch>() {
        @Override
        public ScoredMatch createFromParcel(Parcel in) {
            return new ScoredMatch(in);
        }

        @Override
        public ScoredMatch[] newArray(int size) {
            return new ScoredMatch[size];
        }
    };

    @Override
    public int describeContents() {
        return 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(homeParticipantId);
        dest.writeLong(awayParticipantId);
        dest.writeString(homeName);
        dest.writeString(awayName);
        dest.writeInt(homeScore);
        dest.writeInt(awayScore);

        long[] winnersArray;
        if( homeIds != null ) {
            winnersArray = new long[homeIds.size()];
            for (int i =0; i<homeIds.size();i++) winnersArray[i] = homeIds.get(i);
        } else winnersArray = new long[0];
        dest.writeLongArray(winnersArray);

        long[] losersArray;
        if( awayIds != null ) {
            losersArray = new long[awayIds.size()];
            for (int i = 0; i < awayIds.size(); i++) losersArray[i] = awayIds.get(i);
        } else losersArray = new long[0];
        dest.writeLongArray(losersArray);

        if (lastModified == null) dest.writeString(null);
        else dest.writeString(DateFormatFactory.getInstance().getDateFormat().format(lastModified));
        if (lastSynchronized == null) dest.writeString(null);
        else dest.writeString(DateFormatFactory.getInstance().getDateFormat().format(lastSynchronized));
        dest.writeString(uid);
        dest.writeString(etag);
    }

    public long getHomeParticipantId() {
        return homeParticipantId;
    }

    public void setHomeParticipantId(long homeParticipantId) {
        this.homeParticipantId = homeParticipantId;
    }

    public long getAwayParticipantId() {
        return awayParticipantId;
    }

    public void setAwayParticipantId(long awayParticipantId) {
        this.awayParticipantId = awayParticipantId;
    }

    public ArrayList<Long> getHomeIds() {
        return homeIds;
    }

    public void setHomeIds(ArrayList<Long> winnersIds) {
        this.homeIds = winnersIds;
    }

    public ArrayList<Long> getAwayIds() {
        return awayIds;
    }

    public void setAwayIds(ArrayList<Long> lossersIds) {
        this.awayIds = lossersIds;
    }

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

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public String getAwayName() {
        return awayName;
    }

    public void setAwayName(String awayName) {
        this.awayName = awayName;
    }
}
