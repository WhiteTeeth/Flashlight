package baiya.flashlight;

import android.os.Handler;
import android.os.Message;

public class FlickTask {

    private static final int MSG = 1;
    private boolean mRunning = false;
    private boolean mState = false;

    private long mInterval = 0;
    private IFlashControl mFlashControl;

    public FlickTask(long interval, IFlashControl flashControl) {
        mInterval = interval;
        mFlashControl = flashControl;
    }

    public void setInterval(long interval) {
        mInterval = interval;
    }

    public long getInterval() {
        return mInterval;
    }

    synchronized public void start() {
        mRunning = true;
        mState = false;
        mHandler.sendMessage(mHandler.obtainMessage(MSG));
    }

    public void stop() {
        mHandler.removeMessages(MSG);
        mRunning = false;
        mState = false;
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (mRunning) {
                synchronized (FlickTask.this) {
                    long interval = getInterval();
                    long delay = interval < 0 ? 0 : interval;
                    if (mState) {
                        if (mFlashControl != null) mFlashControl.openFlash();
                    } else {
                        if (mFlashControl != null) mFlashControl.closeFlash();
                    }
                    mState = !mState;
                    sendMessageDelayed(obtainMessage(MSG), delay);
                }
            }
            super.handleMessage(msg);
        }
    };

}
