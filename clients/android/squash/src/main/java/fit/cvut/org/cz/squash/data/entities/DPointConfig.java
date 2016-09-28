package fit.cvut.org.cz.squash.data.entities;

/**
 * Represents configuration of tournament on data layer. Methods return how many points should be awarded
 * Created by Vaclav on 19. 4. 2016.
 */
public class DPointConfig {
    private long tournamentId;
    private int win, draw, loss;

    public DPointConfig(long tournamentId, int win, int draw, int loss) {
        this.tournamentId = tournamentId;
        this.win = win;
        this.draw = draw;
        this.loss = loss;
    }

    public long getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(long tournamentId) {
        this.tournamentId = tournamentId;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getLoss() {
        return loss;
    }

    public void setLoss(int loss) {
        this.loss = loss;
    }
}
