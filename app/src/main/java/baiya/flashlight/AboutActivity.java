package baiya.flashlight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import baiya.flashlight.tools.ApkTools;

public class AboutActivity extends Activity implements View.OnClickListener{

    private LinearLayout mBackBtn;
    private TextView mVersionView;

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, AboutActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        mBackBtn = (LinearLayout) findViewById(R.id.about_back);
        mBackBtn.setOnClickListener(this);

        mVersionView = (TextView) findViewById(R.id.app_version);
        String version = ApkTools.getVersion(this);
        if (version != null) {
            mVersionView.setText(version);
        } else {
            mVersionView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about_back:
                finish();
                break;
        }
    }
}
