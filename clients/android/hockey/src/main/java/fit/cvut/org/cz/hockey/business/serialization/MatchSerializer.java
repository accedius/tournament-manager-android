package fit.cvut.org.cz.hockey.business.serialization;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.business.entities.Match;
import fit.cvut.org.cz.hockey.business.entities.MatchPlayerStatistic;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.enums.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.business.helpers.DateFormatter;
import fit.cvut.org.cz.tmlibrary.business.serialization.BaseSerializer;
import fit.cvut.org.cz.tmlibrary.business.serialization.FileSerializingStrategy;
import fit.cvut.org.cz.tmlibrary.business.serialization.ServerCommunicationItem;

/**
 * Created by kevin on 8.10.2016.
 */
public class MatchSerializer extends BaseSerializer<Match> {
    protected static MatchSerializer instance = null;
    protected MatchSerializer(Context context) {
        this.context = context;
    }

    public static MatchSerializer getInstance(Context context) {
        strategy = new FileSerializingStrategy();
        if (instance == null) {
            instance = new MatchSerializer(context);
        }
        return instance;
    }

    @Override
    public ServerCommunicationItem serialize(fit.cvut.org.cz.hockey.business.entities.Match entity) {
        /* Serialize Match itself */
        ServerCommunicationItem item = new ServerCommunicationItem(strategy.getUid(entity), entity.getEtag(), entity.getServerToken(), getEntityType(), getEntityType());
        item.setId(entity.getId());
        item.setModified(entity.getLastModified());
        item.setSyncData(serializeSyncData(entity));

        /* Serialize Teams */
        // TODO serialize participants - teams
        /*Team home = ManagerFactory.getInstance().teamManager.getById(context, entity.getHomeParticipantId());
        item.subItems.add(TeamSerializer.getInstance(context).serializeToMinimal(home));
        Team away = ManagerFactory.getInstance().teamManager.getById(context, entity.getAwayParticipantId());
        item.subItems.add(TeamSerializer.getInstance(context).serializeToMinimal(away));*/
        return item;
    }

    @Override
    public Match deserialize(ServerCommunicationItem item) {
        Match sm = new Match();
        sm.setEtag(item.getEtag());
        sm.setLastModified(item.getModified());
        sm.setType(CompetitionTypes.teams());
        deserializeSyncData(item.syncData, sm);
        return sm;
    }

    @Override
    public HashMap<String, String> serializeSyncData(Match entity) {
        HashMap<String, String> hm = new HashMap<>();
        if (entity.getDate() == null) {
            hm.put("date", null);
        } else {
            hm.put("date", DateFormatter.getInstance().getDBDateFormat().format(entity.getDate()));
        }
        hm.put("played", Boolean.toString(entity.isPlayed()));
        hm.put("note", entity.getNote());
        hm.put("period", Integer.toString(entity.getPeriod()));
        hm.put("round", Integer.toString(entity.getRound()));

        /*
        TODO set score
        hm.put("score_home", Integer.toString(entity.getHomeScore()));
        hm.put("score_away", Integer.toString(entity.getAwayScore()));
        */

        Match ms = ManagerFactory.getInstance(context).statisticsManager.getMatchScoreByMatchId(context, entity.getId());
        if (ms != null) {
            hm.put("overtime", Boolean.toString(ms.isOvertime()));
            hm.put("shootouts", Boolean.toString(ms.isShootouts()));
        } else {
            hm.put("overtime", Boolean.toString(false));
            hm.put("shootouts", Boolean.toString(false));
        }

        /* Serialize rosters and stats */
        int homePlayerId=0;
        // TODO get home players
        /*
        for (Long playerId : entity.getHomeIds()) {
            MatchPlayerStatistic stat = ManagerFactory.getInstance().statisticsManager.getPlayerStatsInMatch(context, playerId, entity.getId());
            Player p = ManagerFactory.getInstance().packagePlayerManager.getPlayerById(context, stat.getPlayerId());
            homePlayerId++;
            hm.put("player_home_"+homePlayerId, strategy.getUid(p));
            hm.put("stat_goals_"+strategy.getUid(p), Integer.toString(stat.getGoals()));
            hm.put("stat_assists_"+strategy.getUid(p), Integer.toString(stat.getAssists()));
            hm.put("stat_saves_"+strategy.getUid(p), Integer.toString(stat.getSaves()));
            hm.put("stat_plus_minus_points_"+strategy.getUid(p), Integer.toString(stat.getPlusMinusPoints()));
        }
        hm.put("players_home", Integer.toString(homePlayerId));

        int awayPlayerId=0;
        for (Long playerId : entity.getAwayIds()) {
            MatchPlayerStatistic stat = ManagerFactory.getInstance().statisticsManager.getPlayerStatsInMatch(context, playerId, entity.getId());
            Player p = ManagerFactory.getInstance().packagePlayerManager.getPlayerById(context, stat.getPlayerId());
            awayPlayerId++;
            hm.put("player_away_"+awayPlayerId, strategy.getUid(p));
            hm.put("stat_goals_"+strategy.getUid(p), Integer.toString(stat.getGoals()));
            hm.put("stat_assists_"+strategy.getUid(p), Integer.toString(stat.getAssists()));
            hm.put("stat_saves_"+strategy.getUid(p), Integer.toString(stat.getSaves()));
            hm.put("stat_plus_minus_points_"+strategy.getUid(p), Integer.toString(stat.getPlusMinusPoints()));
        }
        hm.put("players_away", Integer.toString(awayPlayerId));
        */
        return hm;
    }

    @Override
    public void deserializeSyncData(HashMap<String, String> syncData, fit.cvut.org.cz.hockey.business.entities.Match entity) {
        SimpleDateFormat dateFormat = DateFormatter.getInstance().getDBDateFormat();
        try {
            entity.setDate(dateFormat.parse(syncData.get("date")));
        } catch (ParseException e) {} catch (NullPointerException e) {}
        entity.setPlayed(Boolean.parseBoolean(syncData.get("played")));
        entity.setNote(syncData.get("note"));
        entity.setPeriod(Integer.parseInt(syncData.get("period")));
        entity.setRound(Integer.parseInt(syncData.get("round")));
    }

    @Override
    public String getEntityType() {
        return "Match";
    }
}
