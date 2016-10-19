package fit.cvut.org.cz.tmlibrary.business.serialization;

import android.content.Context;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.enums.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.business.helpers.DateFormatter;

/**
 * Created by kevin on 8.10.2016.
 */
abstract public class CompetitionSerializer extends BaseSerializer<Competition> {
    protected static Context context = null;

    protected CompetitionSerializer(Context context) {
        this.context = context;
    }

    @Override
    public HashMap<String, String> serializeSyncData(Competition entity) {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("sport_context", entity.getSportContext());
        hm.put("name", entity.getName());
        if (entity.getStartDate() == null) {
            hm.put("start_date", null);
        } else {
            hm.put("start_date", entity.getStartDate().toString());
        }
        if (entity.getEndDate() == null) {
            hm.put("end_date", null);
        } else {
            hm.put("end_date", entity.getEndDate().toString());
        }
        hm.put("note", entity.getNote());
        hm.put("type", Integer.toString(entity.getType().id));
        return hm;
    }

    @Override
    public void deserializeSyncData(HashMap<String, String> syncData, Competition entity) {
        SimpleDateFormat dateFormat = DateFormatter.getInstance().getDBDateFormat();
        entity.setName(syncData.get("name"));
        entity.setNote(syncData.get("note"));
        entity.setType(CompetitionTypes.competitionTypes()[Integer.parseInt(syncData.get("type"))]);
        try {
            entity.setStartDate(dateFormat.parse(syncData.get("start_date")));
            entity.setEndDate(dateFormat.parse(syncData.get("end_date")));
        } catch (ParseException e) {} catch (NullPointerException e) {}
    }

    @Override
    public String getEntityType() {
        return "competition";
    }
}
