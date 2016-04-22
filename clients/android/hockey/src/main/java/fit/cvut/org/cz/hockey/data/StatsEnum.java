package fit.cvut.org.cz.hockey.data;

/**
 * Created by atgot_000 on 19. 4. 2016.
 */
public enum StatsEnum {
    participates(true),
    team_goals(false),
    goals(true),
    assists(true),
    plus_minus_points(true),
    team_points(true),
    outcome(true),
    interventions(true);



    private boolean forPlayer;

    StatsEnum( boolean forPlayer ) {this.forPlayer = forPlayer;}

    public boolean isForPlayer() {
        return forPlayer;
    }


//    public long getId() { return id; }
//
//    public static StatsEnum getById( Long id )
//    {
//        for( StatsEnum e : values() ){
//            if (e.id.equals( id )) return e;
//        }
//        return null;
//    }
}
