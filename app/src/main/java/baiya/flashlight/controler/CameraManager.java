package baiya.flashlight.controler;

import android.content.Context;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;

public class CameraManager {

    private static Camera sCamera = Camera.open();

    public static Camera getCamera(Context context) {
        if (!hasFlash(context)) return null;
        if (sCamera == null) {
            sCamera = Camera.open();
        }
        return sCamera;
    }

    private static void ensureCamera(Context context) {
        getCamera(context);
    }

    public static void openFlash(Context context) {
        ensureCamera(context);
        if (sCamera == null) return;
        Camera.Parameters parameters = sCamera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        sCamera.setParameters(parameters);
        sCamera.cancelAutoFocus();
        sCamera.startPreview();
    }

    public static void closeFlash() {
        if (sCamera == null) return;
        Camera.Parameters parameters = sCamera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        sCamera.setParameters(parameters);
    }

    public static void release() {
        if (sCamera != null) {
            sCamera.release();
            sCamera = null;
        }
    }

    public static boolean hasFlash(Context context) {
        PackageManager pm = context.getPackageManager();
        FeatureInfo[] featureInfos = pm.getSystemAvailableFeatures();
        for (FeatureInfo f : featureInfos) {
            if (PackageManager.FEATURE_CAMERA_FLASH.equals(f.name)) {
                return true;
            }
        }
        return false;
    }

}
