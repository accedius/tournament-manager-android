package fit.cvut.org.cz.tmlibrary.business.entities;

import android.os.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import fit.cvut.org.cz.tmlibrary.business.CompetitionType;
import fit.cvut.org.cz.tmlibrary.business.DateFormatter;
import fit.cvut.org.cz.tmlibrary.data.entities.DMatch;

/**
 * Created by Vaclav on 20. 4. 2016.
 */
public class Match extends ShareBase {

    private long id, tournamentId;
    private CompetitionType type;
    private Date date;
    private boolean played;
    private String note;
    private int period, round;

    private ArrayList<Participant> participants;

    public static DMatch convertToDMatch(Match m){
        return new DMatch(m.getId(), m.getTournamentId(), m.getPeriod(), m.getRound(), m.getDate(), m.getNote(), m.isPlayed(), m.getEtag(),
                m.getUid(), m.getLastModified(), m.getLastSynchronized());
    }

    public Match() {}

    public Match(long id, long tournamentId, CompetitionType type, Date date, boolean played, String note, int period, int round) {
        this.id = id;
        this.tournamentId = tournamentId;
        this.type = type;
        this.date = date;
        this.played = played;
        this.note = note;
        this.period = period;
        this.round = round;
    }

    public Match(DMatch match){
        this.id = match.getId();
        this.tournamentId = match.getTournamentId();
        this.type = null;
        this.date = match.getDate();
        this.played = match.isPlayed();
        this.note = match.getNote();
        this.period = match.getPeriod();
        this.round = match.getRound();
        this.etag = match.getEtag();
        this.uid = match.getUid();
        this.lastModified = match.getLastModified();
        this.lastSynchronized = match.getLastSynchronized();
    }

    protected Match(Parcel in) {
        id = in.readLong();
        tournamentId = in.readLong();
        played = in.readByte() != 0;
        note = in.readString();
        period = in.readInt();
        round = in.readInt();

        SimpleDateFormat dateFormat = DateFormatter.getInstance().getDBDateFormat();

        try{
            String  text = in.readString();
            if (text == null) date = null;
            else date = dateFormat.parse(text);

            text = in.readString();
            if (text == null) lastModified = null;
            else lastModified = dateFormat.parse(text);

            text = in.readString();
            if (text == null) lastSynchronized = null;
            else lastSynchronized = dateFormat.parse(text);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        uid = in.readString();
        etag = in.readString();
    }

    public static final Creator<Match> CREATOR = new Creator<Match>() {
        @Override
        public Match createFromParcel(Parcel in) {
            return new Match(in);
        }

        @Override
        public Match[] newArray(int size) {
            return new Match[size];
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
        dest.writeByte((byte) (played ? 1 : 0));
        dest.writeString(note);
        dest.writeInt(period);
        dest.writeInt(round);

        SimpleDateFormat dateFormat = DateFormatter.getInstance().getDBDateFormat();

        if (date == null) dest.writeString(null);
        else dest.writeString(dateFormat.format(date));
        if (lastModified == null) dest.writeString(null);
        else dest.writeString(dateFormat.format(lastModified));
        if (lastSynchronized == null) dest.writeString(null);
        else dest.writeString(dateFormat.format(lastSynchronized));
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public ArrayList<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<Participant> participants) {
        this.participants = participants;
    }
}
