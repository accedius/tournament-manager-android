package fit.cvut.org.cz.tmlibrary.business.serialization;

import android.content.Context;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.business.helpers.DateFormatter;

/**
 * Created by kevin on 8.10.2016.
 */
abstract public class TournamentSerializer extends BaseSerializer<Tournament> {
    protected static Context context = null;

    protected TournamentSerializer(Context context) {
        this.context = context;
    }

    @Override
    public HashMap<String, String> serializeSyncData(Tournament entity) {
        HashMap<String, String> hm = new HashMap<>();
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
        return hm;
    }

    @Override
    public void deserializeSyncData(HashMap<String, String> syncData, Tournament entity) {
        SimpleDateFormat dateFormat = DateFormatter.getInstance().getDBDateFormat();
        entity.setName(syncData.get("name"));
        entity.setNote(syncData.get("note"));
        try {
            entity.setStartDate(dateFormat.parse(syncData.get("start_date")));
            entity.setEndDate(dateFormat.parse(syncData.get("end_date")));
        } catch (ParseException e) {}
    }

    @Override
    public String getEntityType() {
        return "Tournament";
    }
}
