package baiya.flashlight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;

import baiya.flashlight.controler.CameraManager;
import baiya.flashlight.views.VerticalSeekBar;

public class LightActivity extends Activity
        implements View.OnClickListener, IFlashControl{

    private ImageView mLightFlickBtn;
    private ImageView mLightTouchBtn;
    private ImageView mLightSosBtn;
    private VerticalSeekBar mVerticalSeekBar;
    private ImageButton mFlashBtn;
    private View mContainer;

    private SosThread mSosThread;
    private FlickTask mFlickTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);

        mLightFlickBtn = (ImageView) findViewById(R.id.light_flick);
        mLightTouchBtn = (ImageView) findViewById(R.id.light_touch);
        mLightSosBtn = (ImageView) findViewById(R.id.light_sos);
        mVerticalSeekBar = (VerticalSeekBar) findViewById(R.id.light_bar_right);
        mFlashBtn = (ImageButton) findViewById(R.id.light_close);
        mContainer = findViewById(R.id.light_container);
        mContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!mLightTouchBtn.isSelected()) return false;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        openFlash();
                        break;
                    case MotionEvent.ACTION_UP:
                        closeFlash();
                        break;
                }
                return true;
            }
        });

        mFlashBtn.setOnClickListener(this);
        mLightFlickBtn.setOnClickListener(this);
        mLightTouchBtn.setOnClickListener(this);
        mLightSosBtn.setOnClickListener(this);
        mVerticalSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                long flickInterval = getFlickInterval();
                if (mFlickTask != null) {
                    mFlickTask.setInterval(flickInterval);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // NOT WORK!!!
            }
        });

        openFlash();
    }

    private long getFlickInterval() {
        int progress = mVerticalSeekBar.getProgress();
        int max = mVerticalSeekBar.getMax();
        return (max - progress) * 400 / max + 100;
    }

    public static void startActivityForResult(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, LightActivity.class);
        activity.startActivityForResult(intent, 10345);
    }

    @Override
    protected void onDestroy() {
        if (mSosThread != null) {
            mSosThread.stopThread();
        }
        if (mFlickTask != null) {
            mFlickTask.stop();
        }
        CameraManager.closeFlash();
        CameraManager.release();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.light_close:
                this.finish();
                break;
            case R.id.light_flick:
                mLightSosBtn.setSelected(false);
                selectSos(false);
                mLightTouchBtn.setSelected(false);
                selectTouch(false);
                v.setSelected(!v.isSelected());
                selectFlick(v.isSelected());
                break;
            case R.id.light_touch:
                mLightSosBtn.setSelected(false);
                selectSos(false);
                mLightFlickBtn.setSelected(false);
                selectFlick(false);
                v.setSelected(!v.isSelected());
                selectTouch(v.isSelected());
                break;
            case R.id.light_sos:
                mLightFlickBtn.setSelected(false);
                selectFlick(false);
                mLightTouchBtn.setSelected(false);
                selectTouch(false);
                v.setSelected(!v.isSelected());
                selectSos(v.isSelected());
                break;
        }
    }

    private void selectFlick(boolean selected) {
        if (selected) {
            if (mFlickTask != null) {
                mFlickTask.stop();
            }
            closeFlash();
            mFlickTask = new FlickTask(getFlickInterval(), this);
            mFlickTask.start();
            mVerticalSeekBar.setVisibility(View.VISIBLE);
        } else {
            mVerticalSeekBar.setVisibility(View.GONE);
            if (mFlickTask != null) {
                mFlickTask.stop();
                mFlickTask = null;
            }
            openFlash();
        }
    }

    private void selectTouch(boolean selected) {
        if (selected) {
            closeFlash();
        } else {
            openFlash();
        }
    }

    private void selectSos(boolean selected) {
        if (selected) {
            if (mSosThread != null) {
                mSosThread.stopThread();
            }
            closeFlash();
            mSosThread = new SosThread(this);
            mSosThread.start();
        } else {
            if (mSosThread != null) {
                mSosThread.stopThread();
                mSosThread = null;
            }
            openFlash();
        }
    }

    @Override
    public void closeFlash() {
        CameraManager.closeFlash();
    }

    @Override
    public void openFlash() {
        CameraManager.openFlash(LightActivity.this);
    }

}
