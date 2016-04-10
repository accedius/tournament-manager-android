package fit.cvut.org.cz.tmlibrary.business.entities;

import android.os.Parcel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import fit.cvut.org.cz.tmlibrary.business.CompetitionType;
import fit.cvut.org.cz.tmlibrary.business.DateFormatFactory;

/**
 * Created by Vaclav on 9. 4. 2016.
 */
public class ScoredMatch extends ShareBase {

    private long id, tournamentId;
    private long homeTeamId, awayTeamId;
    private CompetitionType type;
    private Date date;
    private boolean played;
    private ArrayList<Long> winnersIds, lossersIds;
    private int period, round;
    private int homeScore, awayScore;
    private String note, homeName, awayName;

    public ScoredMatch(){}


    protected ScoredMatch(Parcel in) {
        id = in.readLong();
        tournamentId = in.readLong();
        homeTeamId = in.readLong();
        awayTeamId = in.readLong();
        played = in.readByte() != 0;
        period = in.readInt();
        round = in.readInt();
        note = in.readString();
        homeName = in.readString();
        awayName = in.readString();
        homeScore = in.readInt();
        awayScore = in.readInt();

        String sDate = in.readString();
        if (sDate == null) date = null;
        else try {
            date = DateFormatFactory.getInstance().getDateFormat().parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        type = CompetitionType.valueOf(in.readString());

        long[] winners = in.createLongArray();
        winnersIds = new ArrayList<>();
        for (long winner : winners) winnersIds.add(winner);

        long[] losers = in.createLongArray();
        lossersIds = new ArrayList<>();
        for (long loser : losers) lossersIds.add(loser);

        try{
            String text = in.readString();
            if (text == null) lastModified = null;
            else lastModified = DateFormatFactory.getInstance().getDateFormat().parse(text);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        uid = in.readString();
        etag = in.readString();
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
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(tournamentId);
        dest.writeLong(homeTeamId);
        dest.writeLong(awayTeamId);
        dest.writeByte((byte) (played ? 1 : 0));
        dest.writeInt(period);
        dest.writeInt(round);
        dest.writeString(note);
        dest.writeString(homeName);
        dest.writeString(awayName);
        dest.writeInt(homeScore);
        dest.writeInt(awayScore);

        if (date == null) dest.writeString(null);
        else dest.writeString(DateFormatFactory.getInstance().getDateFormat().format(date));
        dest.writeString(type.toString());

        long[] winnersArray = new long[winnersIds.size()];
        for (int i =0; i<winnersIds.size();i++) winnersArray[i] = winnersIds.get(i);
        dest.writeLongArray(winnersArray);

        long[] losersArray = new long[lossersIds.size()];
        for (int i =0; i<lossersIds.size();i++) losersArray[i] = lossersIds.get(i);
        dest.writeLongArray(losersArray);

        if (lastModified == null) dest.writeString(null);
        else dest.writeString(DateFormatFactory.getInstance().getDateFormat().format(lastModified));
        dest.writeString(uid);
        dest.writeString(etag);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(long tournamentId) {
        this.tournamentId = tournamentId;
    }

    public long getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(long homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public long getAwayTeamId() {
        return awayTeamId;
    }

    public void setAwayTeamId(long awayTeamId) {
        this.awayTeamId = awayTeamId;
    }

    public CompetitionType getType() {
        return type;
    }

    public void setType(CompetitionType type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isPlayed() {
        return played;
    }

    public void setPlayed(boolean played) {
        this.played = played;
    }

    public ArrayList<Long> getWinnersIds() {
        return winnersIds;
    }

    public void setWinnersIds(ArrayList<Long> winnersIds) {
        this.winnersIds = winnersIds;
    }

    public ArrayList<Long> getLossersIds() {
        return lossersIds;
    }

    public void setLossersIds(ArrayList<Long> lossersIds) {
        this.lossersIds = lossersIds;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
