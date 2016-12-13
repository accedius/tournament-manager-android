package fit.cvut.org.cz.tmlibrary.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.helpers.DateFormatter;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;

/**
 * Created by Vaclav on 12. 3. 2016.
 */
@DatabaseTable(tableName = DBConstants.tTEAMS)
public class Team extends ShareBase implements Parcelable {
    @DatabaseField(generatedId = true, columnName = DBConstants.cID)
    private long id;

    @DatabaseField(columnName = DBConstants.cTOURNAMENT_ID)
    private long tournamentId;

    @DatabaseField(columnName = DBConstants.cNAME)
    private String name;

    List<Player> players = new ArrayList<>();

    public Team() {}

    public Team(long id, long tournamentId, String name) {
        this.id = id;
        this.tournamentId = tournamentId;
        this.name = name;
    }

    public Team(long tournamentId, String name) {
        this.tournamentId = tournamentId;
        this.name = name;
    }

    protected Team(Parcel in) {
        id = in.readLong();
        name = in.readString();
        players = in.createTypedArrayList(Player.CREATOR);
        tournamentId = in.readLong();
        try {
            String text = in.readString();
            if (text == null) lastModified = null;
            else lastModified = DateFormatter.getInstance().getDBDateFormat().parse(text);
            text = in.readString();
            if (text == null) lastSynchronized = null;
            else lastSynchronized = DateFormatter.getInstance().getDBDateFormat().parse(text);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        uid = in.readString();
        etag = in.readString();
    }

    public static final Creator<Team> CREATOR = new Creator<Team>() {
        @Override
        public Team createFromParcel(Parcel in) {
            return new Team(in);
        }

        @Override
        public Team[] newArray(int size) {
            return new Team[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeTypedList(players);
        dest.writeLong(tournamentId);
        if (lastModified == null) dest.writeString(null);
        else dest.writeString(DateFormatter.getInstance().getDBDateFormat().format(lastModified));
        if (lastSynchronized == null) dest.writeString(null);
        else dest.writeString(DateFormatter.getInstance().getDBDateFormat().format(lastSynchronized));
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public String getEntityType() {
        return "Team";
    }

    @Override
    public String toString() {
        return getName();
    }
}
