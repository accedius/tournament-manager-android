package fit.cvut.org.cz.tmlibrary.business;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.business.entities.NewMatchSpinnerParticipant;
import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IScoredMatchGenerator;

/**
 * Created by kevin on 13.4.2016.
 *
 * Class serves to generate all-play-all matches
 */
public class RoundRobinScoredMatchGenerator implements IScoredMatchGenerator {

    @Override
    public ArrayList<ScoredMatch> generateRound(ArrayList<NewMatchSpinnerParticipant> participants, int round) {
        ArrayList<ScoredMatch> matches = new ArrayList<>();
        int periods_number = participants.size() - (participants.size() + 1) % 2;
        int participants_even_number = participants.size() + participants.size() % 2;
        boolean oddness = participants.size() % 2 == 1 ? true : false;
        boolean round_oddness = round % 2 == 1 ? true : false;

        if (participants.size() <= 1)
            return matches;

        // array of participant indices - serves to generate matches
        ArrayList<Integer> arr = new ArrayList<>();
        for (int i = 0; i < participants_even_number; i++)
            arr.add(i);

        // each iteration generates one period
        int home_idx, away_idx;
        for (int period = 1; period <= periods_number; period++) {
            // each iteration generates one match
            for (int j = 0; j < participants_even_number / 2; j++) {
                if (round_oddness) {
                    home_idx = arr.get(j);
                    away_idx = arr.get(participants_even_number - 1 - j);
                }
                else {
                    home_idx = arr.get(participants_even_number - 1 - j);
                    away_idx = arr.get(j);
                }

                // skip if participants number is odd and participant does not exist
                if (oddness && (home_idx == participants.size() || away_idx == participants.size()))
                    continue;

                matches.add(createMatch(participants, home_idx, away_idx, period, round));
            }
            shift(arr);
        }
        return matches;
    }

    private ScoredMatch createMatch(ArrayList<NewMatchSpinnerParticipant> participants, int home_idx, int away_idx, int period, int round) {
        ScoredMatch sm = new ScoredMatch();
        sm.setPeriod(period);
        sm.setRound(round);
        sm.setHomeParticipantId(participants.get(home_idx).getParticipantId());
        sm.setAwayParticipantId(participants.get(away_idx).getParticipantId());
        return sm;
    }

    private void shift(ArrayList<Integer> arr) {
        int last_idx = arr.get(arr.size()-1);
        for (int i = arr.size()-1; i > 1; i--)
            arr.set(i, arr.get(i-1));
        arr.set(1, last_idx);
    }
}
