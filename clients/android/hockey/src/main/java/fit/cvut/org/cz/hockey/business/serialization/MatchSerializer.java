package fit.cvut.org.cz.hockey.business.serialization;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.business.entities.Match;
import fit.cvut.org.cz.tmlibrary.business.entities.Participant;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.enums.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.business.helpers.DateFormatter;
import fit.cvut.org.cz.tmlibrary.business.serialization.BaseSerializer;
import fit.cvut.org.cz.tmlibrary.business.serialization.FileSerializingStrategy;
import fit.cvut.org.cz.tmlibrary.business.serialization.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.data.ParticipantType;

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

        /* Serialize Teams */
        Team home = ManagerFactory.getInstance(context).teamManager.getById(entity.getHomeParticipantId());
        item.subItems.add(TeamSerializer.getInstance(context).serializeToMinimal(home));
        Team away = ManagerFactory.getInstance(context).teamManager.getById(entity.getAwayParticipantId());
        item.subItems.add(TeamSerializer.getInstance(context).serializeToMinimal(away));
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

        hm.put("score_home", Integer.toString(entity.getHomeScore()));
        hm.put("score_away", Integer.toString(entity.getAwayScore()));

        hm.put("overtime", Boolean.toString(entity.isOvertime()));
        hm.put("shootouts", Boolean.toString(entity.isShootouts()));

        /* Serialize rosters and stats */
        for (Participant participant : entity.getParticipants()) {
            if (ParticipantType.home.toString().equals(participant.getRole())) {
                hm.put("players_home", ManagerFactory.getInstance(context).playerStatManager.getByParticipantId(participant.getId()));
            } else if (ParticipantType.away.toString().equals(participant.getRole())) {
                hm.put("players_away", ManagerFactory.getInstance(context).playerStatManager.getByParticipantId(participant.getId()));
            }
        }
        return hm;
    }

    @Override
    public void deserializeSyncData(HashMap<String, Object> syncData, fit.cvut.org.cz.hockey.business.entities.Match entity) {
        SimpleDateFormat dateFormat = DateFormatter.getInstance().getDBDateFormat();
        try {
            entity.setDate(dateFormat.parse(String.valueOf(syncData.get("date"))));
        } catch (ParseException e) {} catch (NullPointerException e) {}
        entity.setPlayed(Boolean.parseBoolean(String.valueOf(syncData.get("played"))));
        entity.setNote(String.valueOf(syncData.get("note")));
        entity.setPeriod(Integer.parseInt(String.valueOf(syncData.get("period"))));
        entity.setRound(Integer.parseInt(String.valueOf(syncData.get("round"))));
        entity.setOvertime(Boolean.parseBoolean(String.valueOf(syncData.get("overtime"))));
        entity.setShootouts(Boolean.parseBoolean(String.valueOf(syncData.get("shootouts"))));
    }

    @Override
    public String getEntityType() {
        return "Match";
    }
}
