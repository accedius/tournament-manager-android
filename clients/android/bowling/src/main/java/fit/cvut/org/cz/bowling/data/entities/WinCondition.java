package fit.cvut.org.cz.bowling.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import fit.cvut.org.cz.bowling.data.helpers.DBConstants;
import fit.cvut.org.cz.bowling.data.helpers.WinConditionTypes;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntity;

@DatabaseTable(tableName = DBConstants.tWIN_CONDITION)
public class WinCondition implements Parcelable, IEntity {
    @DatabaseField(id = true, columnName = fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants.cTOURNAMENT_ID)
    private long tournamentId;

    @DatabaseField(columnName = DBConstants.cWIN_CONDITION)
    private int winCondition;

    protected WinCondition(Parcel in) {
        tournamentId = in.readLong();
        winCondition = in.readInt();
    }

    public WinCondition() {
        tournamentId = -1;
        winCondition = WinConditionTypes.win_condition_default;
    }

    public static final Creator<WinCondition> CREATOR = new Creator<WinCondition>() {
        @Override
        public WinCondition createFromParcel(Parcel in) {
            return new WinCondition(in);
        }

        @Override
        public WinCondition[] newArray(int size) {
            return new WinCondition[size];
        }
    };

    @Override
    public long getId() {
        return tournamentId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(tournamentId);
        dest.writeInt(winCondition);
    }

    public int getWinCondition() {
        return winCondition;
    }

    public void setWinCondition(int winCondition) {
        this.winCondition = winCondition;
    }
}
