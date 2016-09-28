package fit.cvut.org.cz.tmlibrary.data.entities;

import java.util.Date;

/**
 * Created by atgot_000 on 17. 4. 2016.
 */
public class DParticipant extends DShareBase {
    private long id;
    private long teamId;
    private long matchId;
    private String role;

    public DParticipant(){}

    public DParticipant(long id, long teamId, long matchId, String role) {
        this.id = id;
        this.teamId = teamId;
        this.matchId = matchId;
        this.role = role;
    }

    public DParticipant(long id, long teamId, long matchId, String role, String etag, String uid, Date lastModified, Date lastSynchronized) {
        this.id = id;
        this.teamId = teamId;
        this.matchId = matchId;
        this.role = role;
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

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public long getMatchId() {
        return matchId;
    }

    public void setMatchId(long matchId) {
        this.matchId = matchId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
