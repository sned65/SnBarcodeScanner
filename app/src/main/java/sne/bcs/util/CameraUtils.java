package sne.bcs.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;

import java.util.Collections;
import java.util.List;

/**
 * Collection of helper functions to work with camera.
 */
public class CameraUtils
{
    private static final String TAG = CameraUtils.class.getSimpleName();

    private CameraUtils() {} // not instantiable

    /**
     * @param ctx the context of the single, global Application object
     *            of the current process. Usually returned by
     *            {@code getApplicationContext()}
     * @return {@code true} if the device has at least one camera pointing
     * in some direction, or can support an external camera being connected to it.
     */
    public static boolean hasAnyCamera(Context ctx)
    {
        return Mix.hasFeature(ctx, PackageManager.FEATURE_CAMERA_ANY);
    }

    /**
     * @param ctx the context of the single, global Application object
     *            of the current process. Usually returned by
     *            {@code getApplicationContext()}
     * @return {@code true} if device is supporting flashlight
     */
    public static boolean hasFlash(Context ctx)
    {
        return Mix.hasFeature(ctx, PackageManager.FEATURE_CAMERA_FLASH);
    }

    /**
     * @param ctx the context of the single, global Application object
     *            of the current process. Usually returned by
     *            {@code getApplicationContext()}
     * @return {@code true} if device is supporting autofocus
     */
    public static boolean hasAutofocus(Context ctx)
    {
        return Mix.hasFeature(ctx, PackageManager.FEATURE_CAMERA_AUTOFOCUS);
    }

    /**
     * @param ctx the context of the single, global Application object
     *            of the current process. Usually returned by
     *            {@code getApplicationContext()}
     * @return {@code true} if the device has a camera facing away from the screen.
     */
    public static boolean hasCamera(Context ctx)
    {
        return Mix.hasFeature(ctx, PackageManager.FEATURE_CAMERA);
    }

    /**
     * Gets the supported focus modes.
     *
     * @return a list of supported focus modes for rear camera,
     * or empty list if there is no rear camera.
     */
    public static List<String> rearCameraFocusModes()
    {

        int nc = Camera.getNumberOfCameras();
        int chosen = -1;
        Camera.CameraInfo info = new Camera.CameraInfo();
        for (int i = 0; i < nc; ++i)
        {
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK)
            {
                chosen = i;
                break;
            }
        }

        if (chosen == -1) return Collections.EMPTY_LIST;

        Camera camera = null;
        try
        {
            camera = Camera.open(chosen);
            Camera.Parameters pars = camera.getParameters();
            List<String> focusModes = pars.getSupportedFocusModes();
            return focusModes;
        }
        finally
        {
            if (camera != null)
                camera.release();
        }
    }

    /**
     *
     * @return space-separated list of supported focus modes for rear camera
     * @see CameraUtils#rearCameraFocusModes()
     */
    public static String rearCameraFocusModesListString()
    {
        StringBuilder sb = new StringBuilder();
        List<String> focusModes = rearCameraFocusModes();
        for (String m : focusModes)
        {
            sb.append(" ").append(m);
        }
        return sb.toString();
    }
}
