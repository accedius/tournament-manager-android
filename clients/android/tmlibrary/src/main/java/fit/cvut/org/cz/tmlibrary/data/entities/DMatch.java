package fit.cvut.org.cz.tmlibrary.data.entities;


import java.util.Date;

/**
 * Created by atgot_000 on 17. 4. 2016.
 */
public class DMatch extends DShareBase {

    private long id, tournamentId;
    private int period, round;
    private Date date;

    public DMatch(){}

    public DMatch( long id, long tourId, int per, int rnd, Date date, String etag, String uid, Date lastModified, Date lastSynchronized )
    {
        this.id = id;
        this.tournamentId = tourId;
        this.period = per;
        this.round = rnd;
        this.date = date;
        this.uid = uid;
        this.etag = etag;
        this.lastModified = lastModified;
        this.lastSynchronized = lastSynchronized;
    }

    public DMatch( long id, long tourId, int per, int rnd, Date date )
    {
        this.id = id;
        this.tournamentId = tourId;
        this.period = per;
        this.round = rnd;
        this.date = date;
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

    public Date getDate() {return date; }

    public void setDate(Date date) {this.date = date;}
}
