package color.measurement.com.from_cp20.module.measure.lightcolor.bean;

import java.util.HashMap;

/**
 * Created by wpc on 2017/2/16.
 */

public class ResultAndDValues {

    HashMap<String, Double> data;
    HashMap<String, Boolean> result;

    public ResultAndDValues() {
        data = new HashMap<>();
        result = new HashMap<>();
    }

    public HashMap<String, Double> getData() {
        return data;
    }

    public HashMap<String, Boolean> getResult() {
        return result;
    }

    public void setData(HashMap<String, Double> data) {
        this.data = data;
    }

    public void setResult(HashMap<String, Boolean> result) {
        this.result = result;
    }
}
