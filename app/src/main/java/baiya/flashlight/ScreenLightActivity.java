package baiya.flashlight;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;

import baiya.flashlight.controler.FlickTask;
import baiya.flashlight.controler.IFlashControl;
import baiya.flashlight.controler.PreferencesManager;
import baiya.flashlight.controler.WarningTask;
import baiya.flashlight.views.VerticalSeekBar;

public class ScreenLightActivity extends Activity
        implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, IFlashControl {

    public static PendingIntent getPendingIntent(Context context, int widgetId) {
        Intent intent = new Intent(context, ScreenLightActivity.class);
        return PendingIntent.getActivity(context, 0, intent, widgetId);
    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, ScreenLightActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.appear, R.anim.nomal);
    }

    private ImageView mFlickBtn;
    private ImageView mTouchBtn;
    private ImageView mWarningBtn;
    private VerticalSeekBar mTimeBar;
    private VerticalSeekBar mLightBar;
    private ImageView mCloseBtn;
    private View mContainer;
    private View mModeContainer;

    private int mScreenColor;

    private FlickTask mFlickTask;
    private WarningTask mWarningTask;

    private final View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (!mTouchBtn.isSelected()) return false;
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
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_screen_light);

        mScreenColor = PreferencesManager.getIntPreference(this, PreferencesManager.SCREEN_COLOR, 0);

        mFlickBtn = (ImageView) findViewById(R.id.screen_flick);
        mTouchBtn = (ImageView) findViewById(R.id.screen_touch);
        mWarningBtn = (ImageView) findViewById(R.id.screen_warn);
        mTimeBar = (VerticalSeekBar) findViewById(R.id.screen_time_bar);
        mLightBar = (VerticalSeekBar) findViewById(R.id.screen_light_bar);
        mContainer = findViewById(R.id.screen_bg);
        mCloseBtn = (ImageView) findViewById(R.id.screen_close);
        mModeContainer = findViewById(R.id.screen_mode_container);

        mFlickBtn.setOnClickListener(this);
        mTouchBtn.setOnClickListener(this);
        mWarningBtn.setOnClickListener(this);
        mCloseBtn.setOnClickListener(this);
        mLightBar.setOnSeekBarChangeListener(this);
        mTimeBar.setOnSeekBarChangeListener(this);
        mContainer.setOnTouchListener(mOnTouchListener);
        mContainer.setOnClickListener(this);
        disappear();
        openFlash();

        mLightBar.setProgress(mLightBar.getMax());
        setScreenLight(getLight());

        mTimeBar.setProgress(0);

    }

    @Override
    protected void onDestroy() {
        if (mFlickTask != null) {
            mFlickTask.stop();
        }
        if (mWarningTask != null) {
            mWarningTask.stop();
        }
        setScreenLight(-1.0f);
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.nomal, R.anim.disappear);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.screen_close:
                finish();
                break;
            case R.id.screen_touch:
                mFlickBtn.setSelected(false);
                selectFlickMode(false);
                mWarningBtn.setSelected(false);
                selectWarningMode(false);
                mTouchBtn.setSelected(!mTouchBtn.isSelected());
                selectTouchMode(mTouchBtn.isSelected());
                appear();
                break;
            case R.id.screen_flick:
                mTouchBtn.setSelected(false);
                selectTouchMode(false);
                mWarningBtn.setSelected(false);
                selectWarningMode(false);
                mFlickBtn.setSelected(!mFlickBtn.isSelected());
                selectFlickMode(mFlickBtn.isSelected());
                appear();
                break;
            case R.id.screen_warn:
                mFlickBtn.setSelected(false);
                selectFlickMode(false);
                mTouchBtn.setSelected(false);
                selectTouchMode(false);
                mWarningBtn.setSelected(!mWarningBtn.isSelected());
                selectWarningMode(mWarningBtn.isSelected());
                disappear();
                break;
            case R.id.screen_bg:
                if (!mTouchBtn.isSelected()) {
                    if (mFlickTask != null) {
                        mFlickTask.stop();
                    }
                    if (mWarningTask != null) {
                        mWarningTask.stop();
                    }
                    if (mFlickBtn.isSelected() || mWarningBtn.isSelected()) {
                        mFlickBtn.setSelected(false);
                        mWarningBtn.setSelected(false);
                        appear();
                        openFlash();
                    } else if (isAppear()) {
                        disappear();
                    } else {
                        appear();
                    }
                }
                break;
        }
    }

    private boolean isAppear() {
        return mModeContainer.getVisibility() == View.VISIBLE;
    }

    private void disappear() {
        mTimeBar.setVisibility(View.INVISIBLE);
        mLightBar.setVisibility(View.INVISIBLE);
        mModeContainer.setVisibility(View.INVISIBLE);
    }

    private void appear() {
        if (mTouchBtn.isSelected()) {
            mTimeBar.setVisibility(View.INVISIBLE);
            mLightBar.setVisibility(View.INVISIBLE);
        } else if (mFlickBtn.isSelected()) {
            mTimeBar.setVisibility(View.VISIBLE);
            mLightBar.setVisibility(View.INVISIBLE);
        } else if (mWarningBtn.isSelected()) {
            mTimeBar.setVisibility(View.INVISIBLE);
            mLightBar.setVisibility(View.INVISIBLE);
        } else {
            mTimeBar.setVisibility(View.INVISIBLE);
            mLightBar.setVisibility(View.VISIBLE);
        }
        mModeContainer.setVisibility(View.VISIBLE);
    }

    private void selectTouchMode(boolean selected) {
        if (selected) {
            closeFlash();
        } else {
            openFlash();
        }
    }

    private void selectFlickMode(boolean selected) {
        if (selected) {
            if (mFlickTask != null) {
                mFlickTask.stop();
            }
            closeFlash();
            mFlickTask = new FlickTask(getInterval(), this);
            mFlickTask.start();
            mTimeBar.setVisibility(View.VISIBLE);
        } else {
            mTimeBar.setVisibility(View.GONE);
            if (mFlickTask != null) {
                mFlickTask.stop();
                mFlickTask = null;
            }
            openFlash();
        }
    }

    private void selectWarningMode(boolean selected) {
        if (selected) {
            if (mWarningTask != null) {
                mWarningTask.stop();
            }
            closeFlash();
            mWarningTask = new WarningTask(this);
            mWarningTask.start();
        } else {
            if (mWarningTask != null) {
                mWarningTask.stop();
                mWarningTask = null;
            }
            openFlash();
        }
    }

    private long getInterval() {
        int progress = mTimeBar.getProgress();
        int max = mTimeBar.getMax();
        return (max - progress) * 400 / max + 100;
    }

    private float getLight() {
        return (float) mLightBar.getProgress() / mLightBar.getMax() * 0.5f + 0.5f ;
    }

    public void setScreenLight(float brightness) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = brightness;
        getWindow().setAttributes(lp);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.screen_light_bar:
                setScreenLight(getLight());
                break;
            case R.id.screen_time_bar:
                if (mFlickTask != null) {
                    mFlickTask.setInterval(getInterval());
                }
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void closeFlash() {
        mContainer.setBackgroundColor(0xff000000);
    }

    @Override
    public void openFlash() {
        if (mWarningBtn.isSelected()) {
            if (mWarningTask != null) {
                if (mWarningTask.getCounter() < 6) {
                    mContainer.setBackgroundColor(0xff0000fd);
                } else {
                    mContainer.setBackgroundColor(0xfffd0000);
                }
            }
        } else {
            mContainer.setBackgroundColor(mScreenColor);
        }
    }
}
