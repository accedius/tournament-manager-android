package fit.cvut.org.cz.squash.business.serialization;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import fit.cvut.org.cz.squash.business.ManagerFactory;
import fit.cvut.org.cz.squash.business.entities.Match;
import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.business.enums.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.business.helpers.DateFormatter;
import fit.cvut.org.cz.tmlibrary.business.serialization.BaseSerializer;
import fit.cvut.org.cz.tmlibrary.business.serialization.FileSerializingStrategy;
import fit.cvut.org.cz.tmlibrary.business.serialization.PlayerSerializer;
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
    public ServerCommunicationItem serialize(Match entity) {
        /* Serialize Match itself */
        ServerCommunicationItem item = new ServerCommunicationItem(strategy.getUid(entity), entity.getEtag(), entity.getServerToken(), getEntityType(), getEntityType());
        item.setId(entity.getId());
        item.setModified(entity.getLastModified());
        item.setSyncData(serializeSyncData(entity));

        /* Serialize Participants */
        Tournament t = ManagerFactory.getInstance(context).tournamentManager.getById(entity.getTournamentId());
        Competition c = ManagerFactory.getInstance(context).competitionManager.getById(t.getCompetitionId());
        if (c.getType().equals(CompetitionTypes.teams())) {
            Team home = ManagerFactory.getInstance(context).teamManager.getById(entity.getHomeParticipantId());
            item.subItems.add(TeamSerializer.getInstance(context).serializeToMinimal(home));
            Team away = ManagerFactory.getInstance(context).teamManager.getById(entity.getAwayParticipantId());
            item.subItems.add(TeamSerializer.getInstance(context).serializeToMinimal(away));
        } else {
            Player home = ManagerFactory.getInstance(context).corePlayerManager.getPlayerById(entity.getHomeParticipantId());
            item.subItems.add(PlayerSerializer.getInstance(context).serializeToMinimal(home));
            Player away = ManagerFactory.getInstance(context).corePlayerManager.getPlayerById(entity.getAwayParticipantId());
            item.subItems.add(PlayerSerializer.getInstance(context).serializeToMinimal(away));
        }

        return item;
    }

    @Override
    public Match deserialize(ServerCommunicationItem item) {
        Match sm = new Match();
        sm.setEtag(item.getEtag());
        sm.setLastModified(item.getModified());
        deserializeSyncData(item.syncData, sm);
        return sm;
    }

    @Override
    public HashMap<String, Object> serializeSyncData(Match entity) {
        HashMap<String, Object> hm = new HashMap<>();
        if (entity.getDate() == null) {
            hm.put("date", null);
        } else {
            hm.put("date", DateFormatter.getInstance().getDBDateFormat().format(entity.getDate()));
        }
        hm.put("played", Boolean.toString(entity.isPlayed()));
        hm.put("note", entity.getNote());
        hm.put("period", Integer.toString(entity.getPeriod()));
        hm.put("round", Integer.toString(entity.getRound()));

        hm.put("score_home", Integer.toString(entity.getHomeWonSets()));
        hm.put("score_away", Integer.toString(entity.getAwayWonSets()));

        /* Serialize sets */
        /*ArrayList<SetRowItem> sets = ManagerFactory.getInstance(context).statisticManager.getSetsForMatch(entity.getId());
        hm.put("sets", Integer.toString(sets.size()));
        int set_id = 1;
        for (SetRowItem set : sets) {
            hm.put("set_home_"+set_id, Integer.toString(set.getHomeScore()));
            hm.put("set_away_"+set_id, Integer.toString(set.getAwayScore()));
            set_id++;
        }*/

        /* Serialize players */
        /*ArrayList<Player> homePlayers = ManagerFactory.getInstance(context).statisticManager.getPlayersForMatch(entity.getId(), "home");
        hm.put("players_home", Integer.toString(homePlayers.size()));

        int homePlayerId = 1;
        for (Player p : homePlayers) {
            hm.put("player_home_"+homePlayerId, strategy.getUid(p));
            homePlayerId++;
        }

        ArrayList<Player> awayPlayers = ManagerFactory.getInstance(context).statisticManager.getPlayersForMatch(entity.getId(), "away");
        hm.put("players_away", Integer.toString(awayPlayers.size()));

        int awayPlayerId = 1;
        for (Player p : awayPlayers) {
            hm.put("player_away_"+awayPlayerId, strategy.getUid(p));
            awayPlayerId++;
        }*/
        return hm;
    }

    @Override
    public void deserializeSyncData(HashMap<String, Object> syncData, Match entity) {
        SimpleDateFormat dateFormat = DateFormatter.getInstance().getDBDateFormat();
        /*try {
            entity.setDate(dateFormat.parse(syncData.get("date")));
        } catch (ParseException e) {} catch (NullPointerException e) {}
        entity.setPlayed(Boolean.parseBoolean(syncData.get("played")));
        entity.setNote(syncData.get("note"));
        entity.setPeriod(Integer.parseInt(syncData.get("period")));
        entity.setRound(Integer.parseInt(syncData.get("round")));*/
    }

    @Override
    public String getEntityType() {
        return "ScoredMatch";
    }
}
