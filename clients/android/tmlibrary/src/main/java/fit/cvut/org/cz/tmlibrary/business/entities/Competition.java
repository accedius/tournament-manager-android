package fit.cvut.org.cz.tmlibrary.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;

/**
 * Created by Vaclav on 12. 3. 2016.
 */
public class Competition implements Parcelable {

    private long id;
    private String uid;
    private String name;
    private Date startDate;
    private Date endDate;
    private List<Tournament> tournaments;
    private List<Player> players;
    private String note;
    private String type;

    public Competition(long id, String uid, String name, Date startDate, Date endDate, List<Tournament> tournaments, List<Player> players, String note, String type) {
        this.id = id;
        this.uid = uid;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tournaments = tournaments;
        this.players = players;
        this.note = note;
        this.type = type;
    }

    protected Competition(Parcel in) {
        id = in.readLong();
        uid = in.readString();
        name = in.readString();
        tournaments = in.createTypedArrayList(Tournament.CREATOR);
        players = in.createTypedArrayList(Player.CREATOR);
        note = in.readString();
        type = in.readString();
    }

    public static final Creator<Competition> CREATOR = new Creator<Competition>() {
        @Override
        public Competition createFromParcel(Parcel in) {
            return new Competition(in);
        }

        @Override
        public Competition[] newArray(int size) {
            return new Competition[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<Tournament> getTournaments() {
        return tournaments;
    }

    public void setTournaments(List<Tournament> tournaments) {
        this.tournaments = tournaments;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(uid);
        dest.writeString(name);
        dest.writeTypedList(tournaments);
        dest.writeTypedList(players);
        dest.writeString(note);
        dest.writeString(type);
    }
}
