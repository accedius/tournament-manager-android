package fit.cvut.org.cz.tmlibrary.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.business.DateFormatter;
import fit.cvut.org.cz.tmlibrary.data.entities.DTeam;

/**
 * Created by Vaclav on 12. 3. 2016.
 */
public class Team extends ShareBase implements Parcelable {
    private long id;
    private long tournamentId;
    private String name;
    ArrayList<Player> players = new ArrayList<>();

    public Team(long tournamentId, String name) {
        this.tournamentId = tournamentId;
        this.name = name;
    }

    public Team(DTeam dTeam){
        id = dTeam.getId();
        tournamentId = dTeam.getTournamentId();
        name = dTeam.getName();
        players = new ArrayList<>();
        this.uid = dTeam.getUid();
        this.etag = dTeam.getEtag();
        this.lastModified = dTeam.getLastModified();
        this.lastSynchronized = dTeam.getLastSynchronized();
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

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public static DTeam convertToDTeam(Team t){
        return new DTeam(t.getId(), t.getTournamentId(), t.getName(), t.getEtag(), t.getUid(), t.getLastModified(), t.getLastSynchronized());
    }

    public void addPlayer(Player player) {
        players.add(player);
    }
}
