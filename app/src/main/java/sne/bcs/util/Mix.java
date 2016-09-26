package sne.bcs.util;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;

import java.nio.CharBuffer;

/**
 * Collection of helper functions.
 */
public class Mix
{
    private static final String TAG = Mix.class.getName();

    private Mix() {} // not instantiable

    /**
     * Creates a string of spaces that is 'spaces' spaces long.
     * <br/>
     * From {@code http://stackoverflow.com/questions/2804827/create-a-string-with-n-characters}
     *
     * @param spaces The number of spaces to add to the string.
     */
    public static String spaces(int spaces)
    {
        return CharBuffer.allocate(spaces).toString().replace('\0', ' ');
    }

    /**
     * Fixes the orientation of the given activity.
     *
     * @param activity
     */
    public static void fixOrientation(AppCompatActivity activity)
    {
        int orient = activity.getResources().getConfiguration().orientation;
        if (orient == Configuration.ORIENTATION_LANDSCAPE)
        {
            // TODO what to do if the current orientation is SCREEN_ORIENTATION_REVERSE_LANDSCAPE
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        else if (orient == Configuration.ORIENTATION_PORTRAIT)
        {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    /**
     * Check whether the given feature name is one of the available
     * features as returned by getSystemAvailableFeatures().
     *
     * @param ctx the context of the single, global Application object
     *            of the current process. Usually returned by
     *            {@code getApplicationContext()}
     * @param featureName  feature name {@code PackageManager.XXX}
     * @return {@code true} if device is supporting the given feature
     */
    public static boolean hasFeature(Context ctx, String featureName)
    {
        boolean hasFeature = ctx
                .getPackageManager()
                .hasSystemFeature(featureName);
        return hasFeature;
    }
}
