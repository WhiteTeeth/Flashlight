package baiya.flashlight.tools;

import android.content.Context;
import android.content.pm.PackageManager;

import baiya.flashlight.R;

public class ApkTools {

    public static String getVersion(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            String versionName = packageManager.getPackageInfo(context.getPackageName(), 0).versionName;
            Object[] objects = new Object[1];
            objects[0] = versionName;
            return context.getString(R.string.about_version, objects);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
