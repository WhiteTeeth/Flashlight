package baiya.flashlight.tools;

import android.util.Log;

import java.util.Locale;

/**
 * <ul>
 * <li><b>name : </b>		ExLog		</li>
 * <li><b>description :</b>	Log工具				</li>
 * <br>开发阶段将LOG_LEVEL 设置为6
 * <br>在发布时将LOG_LEVEL 设置为0
 * <li><b>author : </b>	桥下一粒砂			</li>
 * <li><b>e-mail : </b>		chenyoca@gmail.com	</li>
 * <li><b>weibo : </b>		@桥下一粒砂			</li>
 * <li><b>date : </b>		2012-8-10 下午9:54:03		</li>
 * </ul>
 */
public class Logger {

    static final String TAG = Logger.class.getSimpleName();

    public static final int LOG_LEVEL = 6;
    public static final int ERROR = 1;
    public static final int WARN = 2;
    public static final int INFO = 3;
    public static final int DEBUG = 4;
    public static final int VERBOS = 5;


    public static void e(String tag, String msg) {
        if (LOG_LEVEL > ERROR)
            Log.e(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (LOG_LEVEL > WARN)
            Log.w(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (LOG_LEVEL > INFO)
            Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (LOG_LEVEL > DEBUG)
            Log.d(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (LOG_LEVEL > VERBOS)
            Log.v(tag, msg);
    }

    /**
     * 输出log日志，tag为所在类名，附带行信息
     *
     * @param msg
     */
    public static void e(String msg) {
        if (LOG_LEVEL > ERROR) {
            StackTraceElement ele = Thread.currentThread().getStackTrace()[3];
            int line = ele.getLineNumber();
            String clazz = ele.getClassName();
            Log.e(clazz, "line -> " + line + " :::: " + msg);
        }
    }

    /**
     * 输出log日志，tag为所在类名，附带行信息
     *
     * @param msg
     */
    public static void w(String msg) {
        if (LOG_LEVEL > WARN) {
            StackTraceElement ele = Thread.currentThread().getStackTrace()[3];
            int line = ele.getLineNumber();
            String clazz = ele.getClassName();
            Log.w(clazz, "line -> " + line + " :::: " + msg);
        }
    }

    /**
     * 输出log日志，tag为所在类名，附带行信息
     *
     * @param msg
     */
    public static void i(String msg) {
        if (LOG_LEVEL > INFO) {
            StackTraceElement ele = Thread.currentThread().getStackTrace()[3];
            int line = ele.getLineNumber();
            String clazz = ele.getClassName();
            Log.i(clazz, "line -> " + line + " :::: " + msg);
        }
    }

    /**
     * 输出log日志，tag为所在类名，附带行信息
     *
     * @param msg
     */
    public static void d(String msg) {
        if (LOG_LEVEL > DEBUG) {
            StackTraceElement ele = Thread.currentThread().getStackTrace()[3];
            int line = ele.getLineNumber();
            String clazz = ele.getClassName();
            Log.d(clazz, "line -> " + line + " :::: " + msg);
        }
    }

    /**
     * 输出log日志，tag为所在类名，附带行信息
     *
     * @param msg
     */
    public static void v(String msg) {
        if (LOG_LEVEL > VERBOS) {
            StackTraceElement ele = Thread.currentThread().getStackTrace()[3];
            int line = ele.getLineNumber();
            String clazz = ele.getClassName();
            Log.v(clazz, "line -> " + line + " :::: " + msg);
        }
    }

    /**
     * 输出调试信息(System.out)，并在调试输入信息中附带当前代码在哪个类哪一行的额外数据。
     *
     * @param message 调试信息
     */
    public static void l(String message) {
        if (LOG_LEVEL > VERBOS) {
            StackTraceElement ele = Thread.currentThread().getStackTrace()[3];
            int line = ele.getLineNumber();
            String clazz = ele.getClassName();
            System.out.println(":::: @" + clazz + " -> " + line + " :::: " + message);
        }
    }

    /**
     * 取得当前代码所在的方法名
     *
     * @return 当前方法名
     */
    public static String getCurrentMethodName() {
        // 0 getThreadStackTrce
        // 1 getStackTrace
        // 2 * this method: getCurrentMethodName
        // 3 your method
        return Thread.currentThread().getStackTrace()[3].getMethodName();
    }

    /**
     * 输出方法调用链
     *
     * @param object 对象
     */
    public static void logCurrentMethodChain(Object object) {
        StackTraceElement[] es = Thread.currentThread().getStackTrace();
        long time = System.currentTimeMillis();
        Log.d(TAG, String.format("###### Object(%s) Method Chain ###### @Time( %d )", object.getClass().getSimpleName(), time));
        for (StackTraceElement e : es) {
            String msg = String.format("### Method Chain ### Caller:%s  ->：%s", e.getClassName(), e.getMethodName());
            d(TAG, msg);
        }
    }

    /**
     * 输出当前方法调用
     *
     * @param object 对象
     */
    public static void logCurrentMethod(Object object) {
        String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
        long time = System.currentTimeMillis();
        String msg = String.format(Locale.CHINA, "###### Calling Method ###### Object(%s) -> %s @Time( %d )", object.getClass().getSimpleName(), methodName, time);
        d(TAG, msg);
    }

}