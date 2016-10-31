package fit.cvut.org.cz.tmlibrary.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kevin on 28.10.2016.
 */
public class TournamentImportInfo extends ImportInfo implements Parcelable {
    private int playersCnt = 0;
    private int teamsCnt = 0;
    private int matchesCnt = 0;

    public TournamentImportInfo(String name, int playersCnt, int teamsCnt, int matchesCnt) {
        super(name);
        this.playersCnt = playersCnt;
        this.teamsCnt = teamsCnt;
        this.matchesCnt = matchesCnt;
    }

    protected TournamentImportInfo(Parcel in) {
        super(in);
        playersCnt = in.readInt();
        teamsCnt = in.readInt();
        matchesCnt = in.readInt();
    }

    public static final Creator<TournamentImportInfo> CREATOR = new Creator<TournamentImportInfo>() {
        @Override
        public TournamentImportInfo createFromParcel(Parcel in) {
            return new TournamentImportInfo(in);
        }

        @Override
        public TournamentImportInfo[] newArray(int size) {
            return new TournamentImportInfo[size];
        }
    };

    public int getPlayersCnt() {
        return playersCnt;
    }

    public void setPlayersCnt(int playersCnt) {
        this.playersCnt = playersCnt;
    }

    public int getTeamsCnt() {
        return teamsCnt;
    }

    public void setTeamsCnt(int teamsCnt) {
        this.teamsCnt = teamsCnt;
    }

    public int getMatchesCnt() {
        return matchesCnt;
    }

    public void setMatchesCnt(int matchesCnt) {
        this.matchesCnt = matchesCnt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(playersCnt);
        dest.writeInt(teamsCnt);
        dest.writeInt(matchesCnt);
    }
}
