package fit.cvut.org.cz.squash.business.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;

public class SquashScoredMatch implements Parcelable {
    protected ScoredMatch scoredMatch;
    List<SetRowItem> sets;

    public SquashScoredMatch() {}

    public SquashScoredMatch(ScoredMatch sm, ArrayList<SetRowItem> s) {
        this.scoredMatch = sm;
        this.sets = s;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(scoredMatch, flags);
        dest.writeTypedList(sets);
    }

    public SquashScoredMatch(Parcel in) {
        this.scoredMatch = new ScoredMatch(in);
        in.readTypedList(this.sets, SetRowItem.CREATOR);
    }

    public static final Creator<SquashScoredMatch> CREATOR = new Creator<SquashScoredMatch>() {
        @Override
        public SquashScoredMatch createFromParcel(Parcel in) {
            return new SquashScoredMatch(in);
        }

        @Override
        public SquashScoredMatch[] newArray(int size) {
            return new SquashScoredMatch[size];
        }
    };

    public ScoredMatch getScoredMatch() {
        return scoredMatch;
    }

    public ArrayList<SetRowItem> getSets() {
        return new ArrayList(sets);
    }

}
