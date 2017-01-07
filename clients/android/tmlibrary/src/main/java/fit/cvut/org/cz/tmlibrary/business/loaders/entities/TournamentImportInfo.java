package fit.cvut.org.cz.tmlibrary.business.loaders.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Entity for Import info about Tournament.
 */
public class TournamentImportInfo extends ImportInfo implements Parcelable {
    private int playersCnt = 0;
    private int teamsCnt = 0;
    private int matchesCnt = 0;

    /**
     * TournamentImportInfo constructor.
     * @param name tournament name
     * @param playersCnt players count
     * @param teamsCnt teams count
     * @param matchesCnt matches count
     */
    public TournamentImportInfo(String name, int playersCnt, int teamsCnt, int matchesCnt) {
        super(name);
        this.playersCnt = playersCnt;
        this.teamsCnt = teamsCnt;
        this.matchesCnt = matchesCnt;
    }

    /**
     * Constructor from Parcel.
     * @param in parcel
     */
    protected TournamentImportInfo(Parcel in) {
        super(in);
        playersCnt = in.readInt();
        teamsCnt = in.readInt();
        matchesCnt = in.readInt();
    }

    /**
     * Parcelable creator.
     */
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

    /**
     * PlayersCount getter.
     * @return playersCnt
     */
    public int getPlayersCnt() {
        return playersCnt;
    }

    /**
     * PlayersCount setter.
     * @param playersCnt count to be set
     */
    public void setPlayersCnt(int playersCnt) {
        this.playersCnt = playersCnt;
    }

    /**
     * TeamsCount getter.
     * @return teamsCnt
     */
    public int getTeamsCnt() {
        return teamsCnt;
    }

    /**
     * TeamsCount setter.
     * @param teamsCnt count to be set
     */
    public void setTeamsCnt(int teamsCnt) {
        this.teamsCnt = teamsCnt;
    }

    /**
     * MatchesCount getter.
     * @return matchesCnt
     */
    public int getMatchesCnt() {
        return matchesCnt;
    }

    /**
     * MatchesCount setter.
     * @param matchesCnt count to be set
     */
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
