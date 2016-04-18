package fit.cvut.org.cz.tmlibrary.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

import fit.cvut.org.cz.tmlibrary.business.entities.Player;

/**
 * Created by Vaclav on 12. 3. 2016.
 */
public class DTeam extends DShareBase {

    private long id;
    private long tournamentId;
    private String name;

    public DTeam(long id, long tournamentId, String name) {
        this.id = id;
        this.tournamentId = tournamentId;
        this.name = name;
    }

    public DTeam(long id, long tournamentId, String name, String etag, String uid, Date lastModified, Date lastSynchronized) {
        this.id = id;
        this.tournamentId = tournamentId;
        this.name = name;
        this.uid = uid;
        this.etag = etag;
        this.lastModified = lastModified;
        this.lastSynchronized = lastSynchronized;
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
}
