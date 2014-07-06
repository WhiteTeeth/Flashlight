package baiya.flashlight;

import android.os.Handler;
import android.os.Message;

import baiya.flashlight.tools.Logger;

public class WarningTask {

    private int mCounter = 0;
    public static final int MSG = 1;
    private IFlashControl mFlashControl;

    public WarningTask(IFlashControl flashControl) {
        mFlashControl = flashControl;
    }

    public int getCounter() {
        return mCounter;
    }

    synchronized public void start() {
        mCounter = 0;
        mHandler.sendMessage(mHandler.obtainMessage(MSG));
    }

    public void stop() {
        Logger.i("stop");
        mHandler.removeMessages(MSG);
        mCounter = 0;
        Logger.i("stop");
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mCounter = mCounter % 12;
            Logger.d("handler");
            int delay;
            if (mCounter == 0 || mCounter == 6) {
                if (mFlashControl != null) {
                    mFlashControl.closeFlash();
                }
                delay = 300;
            } else if (mCounter % 2 == 1){
                if (mFlashControl != null) {
                    mFlashControl.openFlash();
                }
                delay = 100;
            } else {
                if (mFlashControl != null) {
                    mFlashControl.closeFlash();
                }
                delay = 100;
            }
            Logger.d("handler");
            sendMessageDelayed(obtainMessage(MSG), delay);
            mCounter ++;
        }
    };

}
