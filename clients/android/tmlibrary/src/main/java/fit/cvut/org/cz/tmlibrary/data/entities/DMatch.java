package fit.cvut.org.cz.tmlibrary.data.entities;


/**
 * Created by atgot_000 on 17. 4. 2016.
 */
public class DMatch extends DShareBase {

    private long id, tournamentId;
    private int period, round;

    public DMatch(){}

    public DMatch( long id, long tourId, int per, int rnd )
    {
        this.id = id;
        this.tournamentId = tourId;
        this.period = per;
        this.round = rnd;
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

    public void setTournamentId(long id) {
        this.tournamentId = id;
    }

    public int getPeriod() { return  period; }

    public void setPeriod(int period) { this.period = period; }

    public int getRound() { return round; }

    public void setRound( int round ) { this.round = round; }
}
