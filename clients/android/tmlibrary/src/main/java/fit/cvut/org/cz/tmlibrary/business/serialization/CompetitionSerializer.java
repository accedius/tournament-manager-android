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
        hm.put("name", entity.getName());
        hm.put("start_date", entity.getStartDate().toString());
        hm.put("end_date", entity.getEndDate().toString());
        hm.put("note", entity.getNote());
        hm.put("type", Integer.toString(entity.getType().id));
        return hm;
    }

    @Override
    public void deserializeSyncData(String syncData, Competition entity) {
        SimpleDateFormat dateFormat = DateFormatter.getInstance().getDBDateFormat();
        String[] data = new Gson().fromJson(syncData, String[].class);
        entity.setName(data[0]);
        entity.setNote(data[3]);
        entity.setType(CompetitionTypes.competitionTypes()[Integer.parseInt(data[4])]);
        try {
            entity.setStartDate(dateFormat.parse(data[1]));
            entity.setEndDate(dateFormat.parse(data[2]));
        } catch (ParseException e) {}
    }

    @Override
    public String getEntityType() {
        return "competition";
    }
}
