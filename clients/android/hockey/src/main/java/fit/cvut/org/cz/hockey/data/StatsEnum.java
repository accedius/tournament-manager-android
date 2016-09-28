package fit.cvut.org.cz.hockey.data;

/**
 * enumeration of statistics that are being saved
 * Created by atgot_000 on 19. 4. 2016.
 *
 */
public enum StatsEnum {
    participates(true),
    team_goals(false),
    goals(true),
    assists(true),
    plus_minus_points(true),
    outcome(true),
    saves(true);

    private boolean forPlayer;

    /**
     *
     * @param forPlayer true means that the statistic is saved for player. If false, then the stat has no player Id and is for parrticipant
     */
    StatsEnum(boolean forPlayer) {this.forPlayer = forPlayer;}

    public boolean isForPlayer() {
        return forPlayer;
    }

}
