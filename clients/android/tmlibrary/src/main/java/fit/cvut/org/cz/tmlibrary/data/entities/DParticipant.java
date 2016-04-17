package fit.cvut.org.cz.tmlibrary.data.entities;

import java.util.ArrayList;

/**
 * Created by atgot_000 on 17. 4. 2016.
 */
public class DParticipant {

    private long id;
    private long teamId;
    private long matchId;
    private String role;
    private ArrayList<Long> playerIds;

    public DParticipant(){}

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

    public ArrayList<Long> getPlayerIds() {
        return playerIds;
    }

    public void setPlayerIds(ArrayList<Long> playerIds) {
        this.playerIds = playerIds;
    }
}
