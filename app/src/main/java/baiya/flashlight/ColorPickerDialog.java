package baiya.flashlight;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import baiya.flashlight.bean.ColorGroups;

/**
 * Created by BaiYa on 2014/6/30.
 */
public final class ColorPickerDialog extends DialogFragment
        implements View.OnClickListener, AdapterView.OnItemClickListener{

    int mSelectedColorGroupIndex;
    private OnSelectedListener mSelectedListener;
    private static ColorPickerDialog mColorPickerDialog;

    public static ColorPickerDialog getInstance() {
        if (mColorPickerDialog == null) {
            mColorPickerDialog = new ColorPickerDialog();
        }
        return mColorPickerDialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mSelectedListener = (OnSelectedListener) getActivity();
        } catch (Exception e) {
            mSelectedListener = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.ColorSelectedDialog);
//        dialog.setContentView(R.layout.dialog_color_select);
//        ListView listView = (ListView) dialog.findViewById(R.id.color_listview);
//        listView.setAdapter(new ColorListAdapter());
        dialog.setContentView(getContentView());
        return dialog;
    }

    private View getContentView() {
        mSelectedColorGroupIndex = ((FlashLightActivity) getActivity()).getSelectedColorArrayIndex();
        View container = getActivity().getLayoutInflater().inflate(R.layout.dialog_color_select, null);
        ListView listView = (ListView) container.findViewById(R.id.color_listview);
        listView.setAdapter(new ColorListAdapter());
        listView.setOnItemClickListener(this);
        Button cancelBtn = (Button) container.findViewById(R.id.color_cancel);
        cancelBtn.setOnClickListener(this);
        return container;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.color_cancel) {
            dismiss();
        }
    }

    public interface OnSelectedListener {
        void onSelected(int position);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mSelectedListener != null) {
            mSelectedListener.onSelected(position);
        }
    }

    class ColorListAdapter extends BaseAdapter{
        int[] colorItemIds = {
                R.id.color_item_0,
                R.id.color_item_1,
                R.id.color_item_2,
                R.id.color_item_3,
                R.id.color_item_4,
                R.id.color_item_5,
                R.id.color_item_6};
        List<ColorGroups.ColorGroup> colorGroupList = ColorGroups.colorGroups;

        @Override
        public int getCount() {
            return colorGroupList.size();
        }

        @Override
        public ColorGroups.ColorGroup getItem(int position) {
            return colorGroupList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ColorGroups.ColorGroup colorGroup = colorGroupList.get(position);
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = getActivity().getLayoutInflater().inflate(R.layout.dialog_color_selected_item, null);
                viewHolder.textViews = new TextView[colorItemIds.length];
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.color_item_check);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            for (int i = 0; i < colorItemIds.length; i++) {
                viewHolder.textViews[i] = (TextView) convertView.findViewById(colorItemIds[i]);
                viewHolder.textViews[i].setBackgroundColor(colorGroup.colorArray[i]);
            }
            if (mSelectedColorGroupIndex == position) {
                viewHolder.imageView.setImageResource(R.drawable.radio_select);
            } else {
                viewHolder.imageView.setImageResource(R.drawable.radio_unselected);
            }
            return convertView;
        }

        class ViewHolder {
            TextView[] textViews;
            ImageView imageView;
        }
    }
}
