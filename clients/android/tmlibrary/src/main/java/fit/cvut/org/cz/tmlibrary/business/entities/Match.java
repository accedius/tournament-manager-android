package fit.cvut.org.cz.tmlibrary.business.entities;

import android.os.Parcel;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.enums.CompetitionType;
import fit.cvut.org.cz.tmlibrary.business.helpers.DateFormatter;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;

/**
 * Created by Vaclav on 20. 4. 2016.
 */
@DatabaseTable(tableName = DBConstants.tMATCHES)
public class Match extends ShareBase {
    @DatabaseField(generatedId = true, columnName = DBConstants.cID)
    private long id;

    @DatabaseField(columnName = DBConstants.cTOURNAMENT_ID)
    private long tournamentId;

    @DatabaseField(columnName = DBConstants.cTYPE)
    private int typeId;
    private CompetitionType type;

    @DatabaseField(columnName = DBConstants.cDATE)
    private Date date;

    @DatabaseField(columnName = DBConstants.cPLAYED)
    private boolean played;

    @DatabaseField(columnName = DBConstants.cNOTE)
    private String note;

    @DatabaseField(columnName = DBConstants.cPERIOD)
    private int period;

    @DatabaseField(columnName = DBConstants.cROUND)
    private int round;

    protected List<Participant> participants = new ArrayList<>();

    public Match() {}

    public Match(Match m) {
        this.id = m.id;
        this.tournamentId = m.tournamentId;
        this.type = m.type;
//        this.typeId = m.type.id;
        this.date = m.date;
        this.played = m.played;
        this.note = m.note;
        this.period = m.period;
        this.round = m.round;
        this.participants = m.participants;
    }

    public Match(long id, long tournamentId, CompetitionType type, Date date, boolean played, String note, int period, int round) {
        this.id = id;
        this.tournamentId = tournamentId;
        this.type = type;
//        this.typeId = type.id;
        this.date = date;
        this.played = played;
        this.note = note;
        this.period = period;
        this.round = round;
    }

    protected Match(Parcel in) {
        id = in.readLong();
        tournamentId = in.readLong();
        played = in.readByte() != 0;
        note = in.readString();
        period = in.readInt();
        round = in.readInt();

        SimpleDateFormat dateFormat = DateFormatter.getInstance().getDBDateFormat();

        try {
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
        in.readTypedList(participants, Participant.CREATOR);
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
        dest.writeTypedList(participants);
    }

    public void addParticipant(Participant participant) {
        participants.add(participant);
    }

    public List<Participant> getParticipants() {
        return participants;
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

    public String getEntityType() {
        return "Match";
    }
}
