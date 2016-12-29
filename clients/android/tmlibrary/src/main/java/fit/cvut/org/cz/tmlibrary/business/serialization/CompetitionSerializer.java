package fit.cvut.org.cz.tmlibrary.business.serialization;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.helpers.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.data.helpers.DateFormatter;

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
        hm.put(Constants.SPORT, entity.getSportContext());
        hm.put(Constants.NAME, entity.getName());
        if (entity.getStartDate() == null) {
            hm.put(Constants.START, null);
        } else {
            hm.put(Constants.START, DateFormatter.getInstance().getDBDateFormat().format(entity.getStartDate()));
        }
        if (entity.getEndDate() == null) {
            hm.put(Constants.END, null);
        } else {
            hm.put(Constants.END, DateFormatter.getInstance().getDBDateFormat().format(entity.getEndDate()));
        }
        hm.put(Constants.NOTE, entity.getNote());
        hm.put(Constants.TYPE, Integer.toString(entity.getType().id));
        return hm;
    }

    @Override
    public void deserializeSyncData(HashMap<String, Object> syncData, Competition entity) {
        SimpleDateFormat dateFormat = DateFormatter.getInstance().getDBDateFormat();

        entity.setName(String.valueOf(syncData.get(Constants.NAME)));
        entity.setNote(String.valueOf(syncData.get(Constants.NOTE)));
        entity.setType(CompetitionTypes.competitionTypes()[Integer.parseInt(String.valueOf(syncData.get(Constants.TYPE)))]);
        try {
            entity.setStartDate(dateFormat.parse(String.valueOf(syncData.get(Constants.START))));
        } catch (ParseException e) {} catch (NullPointerException e) {}

        try {
            entity.setEndDate(dateFormat.parse(String.valueOf(syncData.get(Constants.END))));
        } catch (ParseException e) {} catch (NullPointerException e) {}
    }

    @Override
    public String getEntityType() {
        return Constants.COMPETITION;
    }
}
