package fit.cvut.org.cz.tournamentmanager.presentation.fragments;

import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Map;

import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractSelectableListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.vh.OneActionViewHolder;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.vh.SimpleOneActionViewHolder;
import fit.cvut.org.cz.tmlibrary.presentation.communication.CrossPackageConstants;
import fit.cvut.org.cz.tournamentmanager.R;
import fit.cvut.org.cz.tournamentmanager.business.ManagerFactory;
import fit.cvut.org.cz.tournamentmanager.business.managers.interfaces.ISettingManager;
import fit.cvut.org.cz.tournamentmanager.data.entities.Setting;
import fit.cvut.org.cz.tournamentmanager.presentation.helpers.PackagesInfo;

/**
 * Fragment for settings
 */
public class SettingsFragment extends Fragment {
    private CoordinatorLayout v;

    private int sportsCount = 0;
    private SparseBooleanArray sparse;
    private ArrayList<Setting> sportSettings;
    private AbstractSelectableListAdapter<Setting, ? extends OneActionViewHolder> adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Map<String, ApplicationInfo> sports = PackagesInfo.getSportContexts(getContext());
        sparse = new SparseBooleanArray();
        sportSettings = new ArrayList<>();
        int i = 0;
        if (sports != null) {
            for (Map.Entry<String, ApplicationInfo> sport: sports.entrySet()) {
                String package_name = sport.getValue().metaData.getString(CrossPackageConstants.PACKAGE_NAME);
                String sport_name = sport.getKey();
                sportSettings.add(new Setting(package_name, sport_name));
                Setting setting = ((ISettingManager)ManagerFactory.getInstance(getContext()).getEntityManager(Setting.class)).getByPackageSport(package_name, sport_name);
                if (setting == null) {
                    sparse.put(i, true);
                }
                i++;
            }
        }
        sportsCount = i;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (sportsCount == 0) {
            View v = inflater.inflate(R.layout.fragment_no_modules, container, false);
            return v;
        }
        v = (CoordinatorLayout) inflater.inflate(fit.cvut.org.cz.tmlibrary.R.layout.fragment_abstract_data, container, false);
        View fragmentView = inflater.inflate(fit.cvut.org.cz.tmlibrary.R.layout.fragment_abstract_list, container, false);
        RecyclerView recyclerView = (RecyclerView) fragmentView.findViewById(fit.cvut.org.cz.tmlibrary.R.id.recycler_view);
        adapter = getAdapter();
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        v.addView(fragmentView);
        adapter.swapData(sportSettings, sparse);
        return v;
    }

    /**
     * AbstractSelectableListAdapter getter.
     * @return AbstractSelectableListAdapter instance.
     */
    protected AbstractSelectableListAdapter<Setting, ? extends OneActionViewHolder> getAdapter() {
        return new SelectSportsAdapter();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (sportsCount == 0)
            return;
        inflater.inflate(R.menu.menu_finish, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_finish) {
            ((ISettingManager)ManagerFactory.getInstance(getContext()).getEntityManager(Setting.class)).deleteAll();
            ArrayList<Setting> settings = adapter.getSelectedItems();
            for (Setting s : sportSettings) {
                if (!settings.contains(s)) {
                    ManagerFactory.getInstance(getContext()).getEntityManager(Setting.class).insert(s);
                }
            }
            Snackbar.make(v, fit.cvut.org.cz.tmlibrary.R.string.settings_saved, Snackbar.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Adapter for display list of Sports.
     */
    class SelectSportsAdapter extends AbstractSelectableListAdapter<Setting, SimpleOneActionViewHolder> {
        @Override
        protected void bindView(SimpleOneActionViewHolder holder, int position) {
            Setting s = data.get(position);
            String sport = getResources().getString(getResources().getIdentifier(s.getSportName(), "string", getContext().getPackageName()));
            holder.name.setText(sport);
        }

        @Override
        public SimpleOneActionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(fit.cvut.org.cz.tmlibrary.R.layout.row_select_item, parent, false);
            return new SimpleOneActionViewHolder(v, this);
        }
    }
}
