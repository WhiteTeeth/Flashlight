package baiya.flashlight;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import baiya.flashlight.controler.PreferencesManager;
import baiya.flashlight.tools.ApkTools;

public class SettingActivity extends Activity implements View.OnClickListener{

    public static void startActivity(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, SettingActivity.class);
        activity.startActivity(intent);
    }

    private LinearLayout mBackContainer;
    private LinearLayout mOpenLightContainer;
    private CheckBox mOpenLightBox;
    private TextView mRateBtn;
    private TextView mShareBtn;
    private TextView mFeedbackBtn;
    private TextView mAboutBtn;
    private TextView mUpdateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mBackContainer = (LinearLayout) findViewById(R.id.setting_back);
        mOpenLightContainer = (LinearLayout) findViewById(R.id.setting_light_on_open_container);
        mOpenLightBox = (CheckBox) findViewById(R.id.setting_light_box);
        mRateBtn = (TextView) findViewById(R.id.setting_rate);
        mShareBtn = (TextView) findViewById(R.id.setting_share);
        mFeedbackBtn = (TextView) findViewById(R.id.setting_feedback);
        mAboutBtn = (TextView) findViewById(R.id.setting_about_us);
        mUpdateBtn = (TextView) findViewById(R.id.setting_update);

        mBackContainer.setOnClickListener(this);
        mOpenLightContainer.setOnClickListener(this);
        mRateBtn.setOnClickListener(this);
        mShareBtn.setOnClickListener(this);
        mFeedbackBtn.setOnClickListener(this);
        mAboutBtn.setOnClickListener(this);
        mUpdateBtn.setOnClickListener(this);

        String version = ApkTools.getVersion(this);
        if (version != null) {
            mUpdateBtn.setText(getString(R.string.setting_update) + " (" + version + ")");
        } else {
            mUpdateBtn.setText(getString(R.string.setting_update));
        }

        boolean lightOnStart = PreferencesManager.getBooleanPreference(this, PreferencesManager.LIGHT_ON_OPEN, false);
        mOpenLightBox.setChecked(lightOnStart);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_back:
                finish();
                break;
            case R.id.setting_light_on_open_container:
                mOpenLightBox.setChecked(!mOpenLightBox.isChecked());
                PreferencesManager.setBooleanPreference(this, PreferencesManager.LIGHT_ON_OPEN, mOpenLightBox.isChecked());
                break;
            case R.id.setting_rate:{
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("market://details?id=com.youba.flashlight");
                intent.setData(uri);
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    uri = Uri.parse("https://play.google.com/store/apps/details?id=com.youba.flashlight");
                    intent.setData(uri);
                    startActivity(intent);
                }
                break;
            }
            case R.id.setting_share:{
                String str = getResources().getString(R.string.setting_share_content) + "http://a.3533.com/flashlight/";
                Intent localIntent = new Intent("android.intent.action.SEND");
                localIntent.setType("text/plain");
                localIntent.putExtra("android.intent.extra.SUBJECT", str);
                localIntent.putExtra("android.intent.extra.TEXT", str);
                try {
                    startActivity(Intent.createChooser(localIntent, getString(R.string.more_share)));
                } catch (Exception localException) {
                    Toast.makeText(this, getString(R.string.more_share_soft), Toast.LENGTH_LONG).show();
                }
                break;
            }
            case R.id.setting_feedback:{
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/html");
                String[] strings = new String[1];
                strings[0] = "lhwhiteteeth@gmail.com";
                intent.putExtra(Intent.EXTRA_EMAIL, strings);
                String subject = getString(R.string.feedback_title);
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                startActivity(intent);
                break;
            }
            case R.id.setting_about_us:
                AboutActivity.startActivity(this);
                break;
            case R.id.setting_update:
                //TODO 版本更新
                Toast.makeText(this, R.string.update_result, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
