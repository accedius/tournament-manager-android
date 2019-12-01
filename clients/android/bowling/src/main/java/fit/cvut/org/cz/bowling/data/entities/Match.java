package fit.cvut.org.cz.bowling.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import fit.cvut.org.cz.bowling.data.helpers.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.ParticipantType;

/**
 * Match entity and its representation in database
 */
@DatabaseTable(tableName = fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants.tMATCHES)
public class Match extends fit.cvut.org.cz.tmlibrary.data.entities.Match implements Parcelable {
    //if match statistics should be considered valid and propagated to upper levels of application (for example, then match is not finished, but we want stats to be taken in account)
    @DatabaseField(columnName = DBConstants.cVALID_FOR_STATS)
    private boolean validForStats;

    public Match() {}
    public Match(long id, long tournamentId, Date date, boolean played, String note, int period, int round, boolean validForStats) {
        super(id, tournamentId, date, played, note, period, round);
        this.validForStats = validForStats;
    }

    public Match(fit.cvut.org.cz.tmlibrary.data.entities.Match m) {
        super(m);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeByte((byte) (validForStats ? 1 : 0));
    }

    public Match(Parcel in) {
        super(in);
        validForStats = in.readByte() != 0;
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

    public boolean isValidForStats() {
        return validForStats;
    }

    public void setValidForStats(boolean validForStats) {
        this.validForStats = validForStats;
    }
}
