package color.measurement.com.from_cp20.manager.Ble_4.bean;

/**
 * Created by wpc on 2017/5/7.
 */

public interface DataPackager {
    public void receiverData(byte [] data);
    public ResultData getResultData();
}
