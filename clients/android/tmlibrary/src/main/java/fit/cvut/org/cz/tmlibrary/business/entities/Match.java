package fit.cvut.org.cz.tmlibrary.business.entities;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.res.TypedArrayUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.CompetitionType;
import fit.cvut.org.cz.tmlibrary.business.DateFormatFactory;

/**
 * Created by Vaclav on 9. 4. 2016.
 */
public class Match extends ShareBase {

    private long id, tournamentId;
    private long homeTeamId, awayTeamId;
    private CompetitionType type;
    private Date date;
    private boolean played;
    private ArrayList<Long> winnersIds, lossersIds;
    private int period, round;
    private String note;


    protected Match(Parcel in) {
        id = in.readLong();
        tournamentId = in.readLong();
        homeTeamId = in.readLong();
        awayTeamId = in.readLong();
        played = in.readByte() != 0;
        period = in.readInt();
        round = in.readInt();
        note = in.readString();

        String sDate = in.readString();
        if (sDate == null) date = null;
        else try {
            date = DateFormatFactory.getInstance().getDateFormat().parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        type = CompetitionType.valueOf(in.readString());

        long[] winners = in.createLongArray();
        winnersIds = new ArrayList<>();
        for (long winner : winners) winnersIds.add(winner);

        long[] losers = in.createLongArray();
        lossersIds = new ArrayList<>();
        for (long loser : losers) lossersIds.add(loser);

        try{
            String text = in.readString();
            if (text == null) lastModified = null;
            else lastModified = DateFormatFactory.getInstance().getDateFormat().parse(text);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        uid = in.readString();
        etag = in.readString();
    }

    public static final Creator<Match> CREATOR = new Creator<Match>() {
        @Override
        public Match createFromParcel(Parcel in) {
            return new Match(in);
        }

        @Override
        public Match[] newArray(int size) {
            return new Match[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(tournamentId);
        dest.writeLong(homeTeamId);
        dest.writeLong(awayTeamId);
        dest.writeByte((byte) (played ? 1 : 0));
        dest.writeInt(period);
        dest.writeInt(round);
        dest.writeString(note);
        if (date == null) dest.writeString(null);
        else dest.writeString(DateFormatFactory.getInstance().getDateFormat().format(date));
        dest.writeString(type.toString());

        long[] winnersArray = new long[winnersIds.size()];
        for (int i =0; i<winnersIds.size();i++) winnersArray[i] = winnersIds.get(i);
        dest.writeLongArray(winnersArray);

        long[] losersArray = new long[lossersIds.size()];
        for (int i =0; i<lossersIds.size();i++) losersArray[i] = lossersIds.get(i);
        dest.writeLongArray(losersArray);

        if (lastModified == null) dest.writeString(null);
        else dest.writeString(DateFormatFactory.getInstance().getDateFormat().format(lastModified));
        dest.writeString(uid);
        dest.writeString(etag);
    }
}
