package fit.cvut.org.cz.tournamentmanager.presentation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.tournamentmanager.R;

/**
 * Created by kevin on 6.5.2016.
 */
public class PlayerSportFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PlayerStatsFragment psf = new PlayerStatsFragment();
        psf.setArguments(getArguments());

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.player_stats, psf)
                .commit();

        PlayerCompetitionsListFragment pclf = new PlayerCompetitionsListFragment();
        pclf.setArguments(getArguments());

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_player_competitions_list, pclf)
                .commit();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_player_sport, container, false);
    }
}
