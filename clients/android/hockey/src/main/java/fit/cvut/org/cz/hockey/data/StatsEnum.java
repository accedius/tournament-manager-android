package fit.cvut.org.cz.hockey.data;

/**
 * Created by atgot_000 on 19. 4. 2016.
 */
public enum StatsEnum {
    participates(1L),
    team_goals(2L),
    goals(3L),
    assists(4L),
    plus_minus_points(5L),
    team_points(6L),
    outcome(7L),
    interventions(8L);



    private Long id;

    StatsEnum( Long id ) { this.id = id; }

    public long getId() { return id; }

    public static StatsEnum getById( Long id )
    {
        for( StatsEnum e : values() ){
            if (e.id.equals( id )) return e;
        }
        return null;
    }
}
