package color.measurement.com.from_cp20.manager.Ble_4.bean;

import color.measurement.com.from_cp20.module.been.SettingBean;

/**
 * Created by wpc on 2017/5/26.
 */

public interface Setting {
    SettingBean getStand();

    SettingBean getTest();

    void init(String str);

}
