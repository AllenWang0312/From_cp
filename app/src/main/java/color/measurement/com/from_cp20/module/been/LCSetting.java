package color.measurement.com.from_cp20.module.been;

import java.util.List;

import color.measurement.com.from_cp20.manager.Ble_4.bean.Setting;

/**
 * Created by wpc on 2017/5/26.
 */

public class LCSetting implements Setting {

    /**
     * stand : {"light":0,"angle":0,"test_mod":0,"average_times":1,"stand_data_display_type":0,"stand_view_mod":0,"stand_auto_name":true}
     * test : {"test_data_display_type":["名称","备注","日期","时间"],"test_view_mod":0,"test_auto_name":true}
     */

    private StandBean stand;
    private TestBean test;

    @Override
    public StandBean getStand() {
        return stand;
    }

    public void setStand(StandBean stand) {
        this.stand = stand;
    }

    @Override
    public TestBean getTest() {
        return test;
    }

    public void setTest(TestBean test) {
        this.test = test;
    }

    @Override
    public void init(String str) {

    }

    public static class StandBean implements SettingBean {
        /**
         * light : 0
         * angle : 0
         * test_mod : 0
         * average_times : 1
         * stand_data_display_type : 0
         * stand_view_mod : 0
         * stand_auto_name : true
         */

        private int light;
        private int angle;
        private int test_mod;
        private int average_times;
        private int stand_data_display_type;
        private int stand_view_mod;
        private boolean stand_auto_name;

        public int getLight() {
            return light;
        }

        public void setLight(int light) {
            this.light = light;
        }

        public int getAngle() {
            return angle;
        }

        public void setAngle(int angle) {
            this.angle = angle;
        }

        public int getTest_mod() {
            return test_mod;
        }

        public void setTest_mod(int test_mod) {
            this.test_mod = test_mod;
        }

        public int getAverage_times() {
            return average_times;
        }

        public void setAverage_times(int average_times) {
            this.average_times = average_times;
        }

        public int getStand_data_display_type() {
            return stand_data_display_type;
        }

        public void setStand_data_display_type(int stand_data_display_type) {
            this.stand_data_display_type = stand_data_display_type;
        }

        public int getStand_view_mod() {
            return stand_view_mod;
        }

        public void setStand_view_mod(int stand_view_mod) {
            this.stand_view_mod = stand_view_mod;
        }

        public boolean isStand_auto_name() {
            return stand_auto_name;
        }

        public void setStand_auto_name(boolean stand_auto_name) {
            this.stand_auto_name = stand_auto_name;
        }
    }

    public static class TestBean implements SettingBean {
        /**
         * test_data_display_type : ["名称","备注","日期","时间"]
         * test_view_mod : 0
         * test_auto_name : true
         */

        private int test_view_mod;
        private boolean test_auto_name;
        private List<String> test_data_display_type;

        public int getTest_view_mod() {
            return test_view_mod;
        }

        public void setTest_view_mod(int test_view_mod) {
            this.test_view_mod = test_view_mod;
        }

        public boolean isTest_auto_name() {
            return test_auto_name;
        }

        public void setTest_auto_name(boolean test_auto_name) {
            this.test_auto_name = test_auto_name;
        }

        public List<String> getTest_data_display_type() {
            return test_data_display_type;
        }

        public void setTest_data_display_type(List<String> test_data_display_type) {
            this.test_data_display_type = test_data_display_type;
        }
    }
}
