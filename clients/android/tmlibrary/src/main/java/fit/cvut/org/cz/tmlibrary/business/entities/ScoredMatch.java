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
public class ScoredMatch extends ShareBase {

    private long id, tournamentId;
    private long homeParticipantId, awayParticipantId;
    private CompetitionType type;
    private Date date;
    private boolean played;
    private ArrayList<Long> homeIds, awayIds;
    private int period, round;
    private int homeScore, awayScore;
    private String note, homeName, awayName;

    public ScoredMatch(){}

    public static DMatch convertToDMatch( ScoredMatch sm )
    {
        return new DMatch( sm.getId(), sm.getTournamentId(), sm.getPeriod(), sm.getRound(),
                sm.getDate(), sm.getNote(), sm.isPlayed(), sm.getEtag(), sm.getUid(), sm.getLastModified(),
                sm.getLastSynchronized() );
    }


    protected ScoredMatch(Parcel in) {
        id = in.readLong();
        tournamentId = in.readLong();
        homeParticipantId = in.readLong();
        awayParticipantId = in.readLong();
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

        String sType = in.readString();
        if(sType == null) type = null;
        else type = CompetitionType.valueOf(sType);

        long[] winners = in.createLongArray();
        homeIds = new ArrayList<>();
        for (long winner : winners) homeIds.add(winner);

        long[] losers = in.createLongArray();
        awayIds = new ArrayList<>();
        for (long loser : losers) awayIds.add(loser);

        try{
            String text = in.readString();
            if (text == null) lastModified = null;
            else lastModified = DateFormatFactory.getInstance().getDateFormat().parse(text);
            text = in.readString();
            if (text == null) lastSynchronized = null;
            else lastSynchronized = DateFormatFactory.getInstance().getDateFormat().parse(text);
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
        dest.writeLong(homeParticipantId);
        dest.writeLong(awayParticipantId);
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

        if(type == null) dest.writeString( null );
        else dest.writeString(type.toString());

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

    public ScoredMatch( DMatch dm )
    {
        this.id = dm.getId();
        this.tournamentId = dm.getTournamentId();
        this.period = dm.getPeriod();
        this.round = dm.getRound();
        this.note = dm.getNote();
        this.date = dm.getDate();
        this.played = dm.isPlayed();
        this.uid = dm.getUid();
        this.etag = dm.getEtag();
        this.lastSynchronized = dm.getLastSynchronized();
        this.lastModified = dm.getLastModified();
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
