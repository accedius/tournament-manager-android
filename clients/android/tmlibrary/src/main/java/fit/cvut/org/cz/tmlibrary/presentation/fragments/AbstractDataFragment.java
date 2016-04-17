package fit.cvut.org.cz.tmlibrary.presentation.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import fit.cvut.org.cz.tmlibrary.R;

/**
 * Created by Vaclav on 19. 3. 2016.
 * Fragment that with couple abstract methods manages sending for data.
 * Children fragment must be able to receive data via BroadcastReceiver
 */
public abstract class AbstractDataFragment extends Fragment {

    /**
     * This methods asks your datasource for data. e.g. start service or async task
     */
    protected abstract void askForData();

    /**
     *
     * @return true if your datasource is currently working to send you back data
     */
    protected abstract boolean isDataSourceWorking();

    /**
     * Broadcast receiver received data and this is result intent. You should bind data onto your view.
     * @param intent with data received
     */
    protected abstract void bindDataOnView(Intent intent);

    /**
     * register receiver for actions you want to receive. You can use built in receiver that just calls bindDataOnView method
     * or extend it for custom logic
     */
    protected abstract void registerReceivers();

    /**
     * Unregister previously registered receivers
     */
    protected abstract void unregisterReceivers();

    /**
     * @param inflater
     * @param container
     * @return inflated layout of fragment, do it here not in onCreateView, it is called during oNCreateView
     */
    protected abstract View injectView(LayoutInflater inflater, ViewGroup container);

    protected ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CoordinatorLayout v = (CoordinatorLayout) inflater.inflate(R.layout.fragment_abstract_data, container, false);
        progressBar = (ProgressBar) v.findViewById(R.id.progress_spinner);
        v.addView(injectView(inflater, v));
        return v;
    }

    /**
     * Default receiver ready for usage
     */
    protected BroadcastReceiver receiver = new DataReceiver();

    protected void customOnResume(){
        registerReceivers();
        if (!isDataSourceWorking())
            askForData();
        progressBar.setVisibility(View.VISIBLE);
    }

    protected void customOnPause(){
        unregisterReceivers();
    }

    @Override
    public void onResume() {
        super.onResume();
        customOnResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        customOnPause();
    }

    public boolean isWorking(){
        return isDataSourceWorking();
    }

    public class DataReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            progressBar.setVisibility(View.GONE);
            bindDataOnView(intent);
            //Toast.makeText(context, "DataReceived", Toast.LENGTH_SHORT).show();
        }
    }
}
