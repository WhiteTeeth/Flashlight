package baiya.flashlight;

import android.app.Activity;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import baiya.flashlight.bean.ColorGroups;
import baiya.flashlight.controler.PreferencesManager;

public class FlashLightActivity extends Activity
        implements View.OnClickListener, ColorPickerDialog.OnSelectedListener{

    private static final String TAG = FlashLightActivity.class.getSimpleName();

    private ImageButton mCenterBtn;
    private ImageButton mColorPickerBtn;
    private ImageButton mImgRightBottom;

    private ImageButton mSettingBtn;
    // 7个imageView对应7个id
    private final int[] mImgIds = {
            R.id.te_img_right,
            R.id.te_img_right_top,
            R.id.te_img_top,
            R.id.te_img_left_top,
            R.id.te_img_left,
            R.id.te_img_left_bottom,
            R.id.te_img_bottom};
    private final ImageView[] mImgViews = new ImageView[mImgIds.length];
    private int mSelectedId = -1;

    private ColorGroups.ColorGroup mCurrentColors;
    private SparseArray<Integer> mViewColorMap;


    private final View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ImageView before = (ImageView) findViewById(mSelectedId);
            ImageView selected = (ImageView) v;
            if (!selected.isSelected()) {
                if (before != null) {
                    before.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    before.setSelected(false);
                }
                selected.setScaleType(ImageView.ScaleType.CENTER);
                selected.setSelected(true);
                mSelectedId = selected.getId();
                saveSelectedColorIndex();
                saveScreenColor();
            }
        }
    };

    private void setImgColors() {
        mViewColorMap = new SparseArray<Integer>();
        for (int i = 0; i < mImgViews.length; i++) {
            // 记录view-color配对
            mViewColorMap.put(mImgViews[i].getId(), mCurrentColors.colorArray[i]);

            ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
            shapeDrawable.getPaint().setColor(mCurrentColors.colorArray[i]);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                mImgViews[i].setBackgroundDrawable(shapeDrawable);
            } else {
                mImgViews[i].setBackground(shapeDrawable);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_flashlight);

        int colorArrayPosition = getSelectedColorArrayIndex();
        mCurrentColors = ColorGroups.colorGroups.get(colorArrayPosition);


        mCenterBtn = (ImageButton) findViewById(R.id.te_screen_center);
        mCenterBtn.setOnClickListener(this);
        mColorPickerBtn = (ImageButton) findViewById(R.id.te_color_select);
        mColorPickerBtn.setOnClickListener(this);
        mImgRightBottom = (ImageButton) findViewById(R.id.te_img_right_bottom);
        mImgRightBottom.setOnClickListener(this);
        mSettingBtn = (ImageButton) findViewById(R.id.te_setting);
        mSettingBtn.setOnClickListener(this);
        for (int i = 0; i < mImgIds.length; i++) {
            final ImageView imageView = (ImageView) findViewById(mImgIds[i]);
            imageView.setOnClickListener(mClickListener);
            mImgViews[i] = imageView;
        }

        setImgColors();

        mSelectedId = getSelectedColorIndex();
        if (mSelectedId == 0) {
            mSelectedId = mImgIds[mImgIds.length - 1];

            saveSelectedColorIndex();
            saveScreenColor();
            saveSelectedColorArrayIndex(colorArrayPosition);
        }
        ImageView imageView = (ImageView) findViewById(mSelectedId);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setSelected(true);

        if (isLightOnOpen()) {
            LightActivity.startActivityForResult(this);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (R.id.te_color_select == id) {
            ColorPickerDialog colorPickerDialog = ColorPickerDialog.getInstance();
            colorPickerDialog.show(getFragmentManager(), "dialog");
        } else if (R.id.te_img_right_bottom == id) {
            LightActivity.startActivityForResult(this);
        } else if (R.id.te_screen_center == id) {
            ScreenLightActivity.startActivity(this);
        } else if (R.id.te_setting == id) {
            SettingActivity.startActivity(this);
        }
    }

    @Override
    public void onSelected(int position) {
        ColorPickerDialog.getInstance().dismiss();
        mCurrentColors = ColorGroups.colorGroups.get(position);
        saveSelectedColorArrayIndex(position);
        setImgColors();
        saveScreenColor();
    }

    public boolean isLightOnOpen() {
        return PreferencesManager.getBooleanPreference(this, PreferencesManager.LIGHT_ON_OPEN, false);
    }

    public int getSelectedColorIndex() {
        return PreferencesManager.getIntPreference(this, PreferencesManager.SELECTED_COLOR_INDEX, 0);
    }

    public void saveSelectedColorIndex() {
        PreferencesManager.setIntPreference(this, PreferencesManager.SELECTED_COLOR_INDEX, mSelectedId);
    }

    public void saveScreenColor() {
        int color = mViewColorMap.get(mSelectedId);
        PreferencesManager.setIntPreference(this, PreferencesManager.SCREEN_COLOR, color);
    }

    public int getSelectedColorArrayIndex() {
        return PreferencesManager.getIntPreference(this, PreferencesManager.SELECTED_COLOR_ARRAY_INDEX, 0);
    }

    public void saveSelectedColorArrayIndex(int position) {
        PreferencesManager.setIntPreference(this, PreferencesManager.SELECTED_COLOR_ARRAY_INDEX, position);
    }

}
