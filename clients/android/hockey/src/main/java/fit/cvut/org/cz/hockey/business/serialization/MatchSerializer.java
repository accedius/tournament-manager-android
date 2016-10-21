package fit.cvut.org.cz.hockey.business.serialization;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.business.entities.MatchPlayerStatistic;
import fit.cvut.org.cz.hockey.business.entities.MatchScore;
import fit.cvut.org.cz.hockey.business.serialization.TeamSerializer;
import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.entities.Match;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.business.enums.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.business.helpers.DateFormatter;
import fit.cvut.org.cz.tmlibrary.business.serialization.BaseSerializer;
import fit.cvut.org.cz.tmlibrary.business.serialization.ISharedEntitySerializer;
import fit.cvut.org.cz.tmlibrary.business.serialization.PlayerSerializer;
import fit.cvut.org.cz.tmlibrary.business.serialization.ServerCommunicationItem;

/**
 * Created by kevin on 8.10.2016.
 */
public class MatchSerializer extends BaseSerializer<ScoredMatch> {
    protected static MatchSerializer instance = null;
    protected MatchSerializer(Context context) {
        this.context = context;
    }

    public static MatchSerializer getInstance(Context context) {
        if (instance == null) {
            instance = new MatchSerializer(context);
        }
        return instance;
    }

    @Override
    public ServerCommunicationItem serialize(ScoredMatch entity) {
        /* Serialize Match itself */
        ServerCommunicationItem item = new ServerCommunicationItem(strategy.getUid(entity), entity.getEtag(), entity.getServerToken(), getEntityType(), getEntityType());
        item.setId(entity.getId());
        item.setModified(entity.getLastModified());
        item.setSyncData(serializeSyncData(entity));

        /* Serialize Teams */
        Team home = ManagerFactory.getInstance().teamManager.getById(context, entity.getHomeParticipantId());
        item.subItems.add(TeamSerializer.getInstance(context).serializeToMinimal(home));
        Team away = ManagerFactory.getInstance().teamManager.getById(context, entity.getAwayParticipantId());
        item.subItems.add(TeamSerializer.getInstance(context).serializeToMinimal(away));
        return item;
    }

    @Override
    public ScoredMatch deserialize(ServerCommunicationItem item) {
        ScoredMatch sm = new ScoredMatch();
        sm.setEtag(item.getEtag());
        sm.setLastModified(item.getModified());
        sm.setType(CompetitionTypes.teams());
        deserializeSyncData(item.syncData, sm);
        return sm;
    }

    @Override
    public HashMap<String, String> serializeSyncData(ScoredMatch entity) {
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

        hm.put("score_home", Integer.toString(entity.getHomeScore()));
        hm.put("score_away", Integer.toString(entity.getAwayScore()));

        MatchScore ms = ManagerFactory.getInstance().statisticsManager.getMatchScoreByMatchId(context, entity.getId());
        if (ms != null) {
            hm.put("overtime", Boolean.toString(ms.isOvertime()));
            hm.put("shootouts", Boolean.toString(ms.isShootouts()));
        } else {
            hm.put("overtime", Boolean.toString(false));
            hm.put("shootouts", Boolean.toString(false));
        }

        /* Serialize rosters and stats */
        int homePlayerId=0;
        for (Long playerId : entity.getHomeIds()) {
            MatchPlayerStatistic stat = ManagerFactory.getInstance().statisticsManager.getPlayerStatsInMatch(context, playerId, entity.getId());
            Player p = ManagerFactory.getInstance().packagePlayerManager.getPlayerById(context, stat.getPlayerId());
            homePlayerId++;
            hm.put("player_home_"+homePlayerId, strategy.getUid(p));
            hm.put("stat_goals_"+strategy.getUid(p), Integer.toString(stat.getGoals()));
            hm.put("stat_assits_"+strategy.getUid(p), Integer.toString(stat.getAssists()));
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
            hm.put("stat_assits_"+strategy.getUid(p), Integer.toString(stat.getAssists()));
            hm.put("stat_saves_"+strategy.getUid(p), Integer.toString(stat.getSaves()));
            hm.put("stat_plus_minus_points_"+strategy.getUid(p), Integer.toString(stat.getPlusMinusPoints()));
        }
        hm.put("players_away", Integer.toString(awayPlayerId));
        return hm;
    }

    @Override
    public void deserializeSyncData(HashMap<String, String> syncData, ScoredMatch entity) {
        SimpleDateFormat dateFormat = DateFormatter.getInstance().getDBDateFormat();
        try {
            entity.setDate(dateFormat.parse(syncData.get("date")));
        } catch (ParseException e) {}
        entity.setPlayed(Boolean.parseBoolean(syncData.get("played")));
        entity.setNote(syncData.get("note"));
        entity.setPeriod(Integer.parseInt(syncData.get("period")));
        entity.setRound(Integer.parseInt(syncData.get("round")));

        entity.setHomeScore(Integer.parseInt(syncData.get("score_home")));
        entity.setAwayScore(Integer.parseInt(syncData.get("score_away")));
    }

    @Override
    public String getEntityType() {
        return "ScoredMatch";
    }
}
