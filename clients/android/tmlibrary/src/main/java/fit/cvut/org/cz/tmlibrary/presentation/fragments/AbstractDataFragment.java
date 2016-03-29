package fit.cvut.org.cz.tmlibrary.presentation.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import fit.cvut.org.cz.tmlibrary.presentation.interfaces.IProgressInterface;
import fit.cvut.org.cz.tmlibrary.presentation.interfaces.IWorkingInterface;

/**
 * Created by Vaclav on 19. 3. 2016.
 * Fragment that with couple abstract methods manages sending for data.
 * Children fragment must be able to receive data via BroadcastReceiver
 */
public abstract class AbstractDataFragment extends Fragment implements IWorkingInterface {

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
     * Default receiver ready for usage
     */
    protected BroadcastReceiver receiver = new DataReceiver();
    private IProgressInterface progressInterface;

    private void displayProgress(){
        if (progressInterface != null)
            progressInterface.showProgress();
    }

    private void hideProgress(){
        if (progressInterface != null)
            progressInterface.hideProgress();
    }

    protected void customOnResume(){
        registerReceivers();
        if (!isDataSourceWorking())
            askForData();
        displayProgress();
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        progressInterface = null;

        if (getParentFragment() != null){
            if (getParentFragment() instanceof IProgressInterface) {
                progressInterface = (IProgressInterface) getParentFragment();
            }
            //TODO pretypovat na aktivitu
        } else if (context instanceof IProgressInterface)
            progressInterface = (IProgressInterface) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        progressInterface = null;
    }

    public boolean isWorking(){
        return isDataSourceWorking();
    }

    public class DataReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            hideProgress();
            bindDataOnView(intent);
            //Toast.makeText(context, "DataReceived", Toast.LENGTH_SHORT).show();
        }
    }
}
