package fit.cvut.org.cz.tmlibrary.presentation.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Simple Intent service with added methods to check if service is currently handling requests.
 */
public abstract class AbstractIntentServiceWProgress extends JobIntentService {
    protected static final int JOB_ID = 1074;
    protected String mName;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AbstractIntentServiceWProgress(String name) {
        super();
        mName = name;
    }

    private static ArrayList<String> workingKeys = new ArrayList<>();

    public static boolean isWorking(String key) {
        return workingKeys.contains(key);
    }

    /**
     * Replacement for a startService(), enqueue an intent in context to be done according to android inner schedule manager
     * @param context context of work
     * @param work intent to be done
     * @param clazz class of service
     */
    public static <T extends AbstractIntentServiceWProgress> void enqueueWork(Context context, Intent work, Class<T> clazz) {
        enqueueWork(context, clazz, JOB_ID, work);
    }

    /*/**
     * If when you do not call super when overriding it wont work
     * @param intent start intent
     *
     */
    /*@Override
    protected void onHandleIntent(Intent intent) {
        String key = intent.getStringExtra(getActionKey());
        workingKeys.add(key);
        try {
            doWork(intent);
        } catch (SQLException e) {}
        workingKeys.remove(key);
    }*/

    /**
     * @param intent start intent
     */
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        String key = intent.getStringExtra(getActionKey());
        workingKeys.add(key);
        try {
            doWork(intent);
        } catch (SQLException e) {
            Log.e(getClass().getName(),"Error in AbstractIntentServiceWProgress during onHandleWork", e);
        }
        workingKeys.remove(key);
    }

    /**
     *
     * @return Key of action you put into extras as string when calling this service
     */
    protected abstract String getActionKey();

    /**
     * Do not handle your work in onHandleIntent do it in this method instead. This method is calledInOnHandleIntent and
     * intent passed to service is passed here as well.
     * @param intent that was passed to service
     */
    protected abstract void doWork(Intent intent) throws SQLException;
}
