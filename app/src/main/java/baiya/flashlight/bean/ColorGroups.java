package baiya.flashlight.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BaiYa on 2014/6/29.
 */
public class ColorGroups {

    public static final List<ColorGroup> colorGroups;
    static {
        colorGroups = new ArrayList<ColorGroup>();
        int[] colorGroup0 = {0xfff5f5f5, 0xfff46543, 0xfffebb22, 0xffecee21, 0xffbae534, 0xff65ccdb, 0xff75ddba};
        int[] colorGroup1 = {0xfff5f5f5, 0xff555555, 0xffa967cb, 0xffffbb32, 0xfffe4543, 0xff98cc00, 0xff32b5e3};
        int[] colorGroup2 = {0xffffffff, 0xff333333, 0xff9834cc, 0xfffe8800, 0xffcc0000, 0xff669900, 0xff0098ca};
        int[] colorGroup3 = {0xffffffff, 0xff00fffc, 0xfffe00fe, 0xff0000fd, 0xfffeff00, 0xff00ff00, 0xfffe0000};
        colorGroups.add(new ColorGroup(colorGroup0));
        colorGroups.add(new ColorGroup(colorGroup1));
        colorGroups.add(new ColorGroup(colorGroup2));
        colorGroups.add(new ColorGroup(colorGroup3));
    }

    public static class ColorGroup {
        public final int[] colorArray;

        public ColorGroup(int[] colorGroup) {
            colorArray = colorGroup;
        }

    }

}
