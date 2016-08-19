package fit.cvut.org.cz.hockey.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;

public class HockeyScoredMatch implements Parcelable {

    protected MatchScore matchScore;
    protected ScoredMatch scoredMatch;

    public HockeyScoredMatch() {}

    public HockeyScoredMatch(ScoredMatch sm, MatchScore ms) {
        this.matchScore = ms;
        this.scoredMatch = sm;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(matchScore, flags);
        dest.writeParcelable(scoredMatch, flags);
    }

    public HockeyScoredMatch(Parcel in ) {
        this.matchScore = new MatchScore(in);
        this.scoredMatch = new ScoredMatch(in);
    }

    public static final Creator<HockeyScoredMatch> CREATOR = new Creator<HockeyScoredMatch>() {
        @Override
        public HockeyScoredMatch createFromParcel(Parcel in) {
            return new HockeyScoredMatch(in);
        }

        @Override
        public HockeyScoredMatch[] newArray(int size) {
            return new HockeyScoredMatch[size];
        }
    };

    public ScoredMatch getScoredMatch() {
        return scoredMatch;
    }

    public MatchScore getMatchScore() {
        return matchScore;
    }

}
