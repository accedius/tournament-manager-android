package fit.cvut.org.cz.hockey.data.entities;

/**
 * Created by atgot_000 on 22. 4. 2016.
 */
public class DMatchStat {
    private long matchId;
    private boolean overtime, shootouts;

    public DMatchStat() {}

    public DMatchStat(long matchId, boolean overtime, boolean shootouts) {
        this.matchId = matchId;
        this.overtime = overtime;
        this.shootouts = shootouts;
    }

    public long getMatchId() {
        return matchId;
    }

    public void setMatchId(long matchId) {
        this.matchId = matchId;
    }

    public boolean isOvertime() {
        return overtime;
    }

    public void setOvertime(boolean overtime) {
        this.overtime = overtime;
    }

    public boolean isShootouts() {
        return shootouts;
    }

    public void setShootouts(boolean shootouts) {
        this.shootouts = shootouts;
    }
}
