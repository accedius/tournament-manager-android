package fit.cvut.org.cz.tmlibrary.business.serialization.serializers;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import fit.cvut.org.cz.tmlibrary.data.helpers.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.data.helpers.DateFormatter;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;

/**
 * Created by kevin on 8.10.2016.
 */
abstract public class CompetitionSerializer extends BaseSerializer<Competition> {
    protected CompetitionSerializer(Context context) {
        this.context = context;
    }

    @Override
    public HashMap<String, Object> serializeSyncData(Competition entity) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("sport_context", entity.getSportContext());
        hm.put("name", entity.getName());
        if (entity.getStartDate() == null) {
            hm.put("start_date", null);
        } else {
            hm.put("start_date", DateFormatter.getInstance().getDBDateFormat().format(entity.getStartDate()));
        }
        if (entity.getEndDate() == null) {
            hm.put("end_date", null);
        } else {
            hm.put("end_date", DateFormatter.getInstance().getDBDateFormat().format(entity.getEndDate()));
        }
        hm.put("note", entity.getNote());
        hm.put("type", Integer.toString(entity.getType().id));
        return hm;
    }

    @Override
    public void deserializeSyncData(HashMap<String, Object> syncData, Competition entity) {
        SimpleDateFormat dateFormat = DateFormatter.getInstance().getDBDateFormat();

        entity.setName(String.valueOf(syncData.get("name")));
        entity.setNote(String.valueOf(syncData.get("note")));
        entity.setType(CompetitionTypes.competitionTypes()[Integer.parseInt(String.valueOf(syncData.get("type")))]);
        try {
            entity.setStartDate(dateFormat.parse(String.valueOf(syncData.get("start_date"))));
        } catch (ParseException e) {} catch (NullPointerException e) {}

        try {
            entity.setEndDate(dateFormat.parse(String.valueOf(syncData.get("end_date"))));
        } catch (ParseException e) {} catch (NullPointerException e) {}
    }

    @Override
    public String getEntityType() {
        return "competition";
    }
}
