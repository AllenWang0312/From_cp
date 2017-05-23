package color.measurement.com.from_cp20.manager.Ble_4.bean;

import android.content.Context;
import android.content.Intent;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import color.measurement.com.from_cp20.module.measure.lightcolor.bean.MeasureType;
import color.measurement.com.from_cp20.util.java.clsPublic;
import color.measurement.com.from_cp20.util.utils.L;

import static color.measurement.com.from_cp20.manager.Ble_4.Ble_Consts.*;


/**
 * Created by wpc on 2017/5/7.
 */

public class LightColorDataPackager implements DataPackager {

    Context mContext;
    LightColorResultData mLightColorResultData;

    int index;
    byte[] result;

    int power;
    byte[] instrumentId = new byte[30];

    public LightColorDataPackager(Context context) {
        mContext = context;
    }

    @Override
    public void receiverData(byte[] item) {
        if (item.length == 1) {
            mContext.sendBroadcast(new Intent(RECEIVE_WRONG_DATA));
        } else if (item.length == 5) {
            if (item[1] == 0x02) {
                if (item[2] == 0x00) {
                    //黑校准成功
                    mContext.sendBroadcast(new Intent(BLACK_TEXT_SUCCESS));
                } else if (item[2] == 0x01) {
                    //黑校准失败
                    mContext.sendBroadcast(new Intent(BLACK_TEXT_FAILD));
                }
            } else if (item[1] == 0x03) {
                if (item[2] == 0x00) {
                    //白校准成功
                    mContext.sendBroadcast(new Intent(WHITE_TEXT_SUCCESS));
                } else if (item[2] == 0x01) {
                    //白校准失败
                    mContext.sendBroadcast(new Intent(WHITE_TEXT_FAILD));
                }
            } else if (item[1] == 0x10) {
                //按键按下
//                    mContext.sendBroadcast(new Intent(INSTRUMENT_BUTTON_PREASED));
            }
        } else if (item.length == 12
//                    &&item[10]==0xff
                ) {
            power = item[9];
            System.arraycopy(result, 3, instrumentId, 0, 30);
            L.i(power + ""
                    + clsPublic.bytesToHexString(result)
                    + new String(instrumentId)
            );
            mContext.sendBroadcast(new Intent(GET_POWER));
        } else {
            if (item[0] == (byte) 0xbb && item[1] == 0x01 && item[2] == 0x00) {
                index = 0;
                result = new byte[327];
            } else if (item[0] == (byte) 0xbb && item[1] == 0x04 && item[2] == 0x00) {
                index = 0;
                result = new byte[36];
            }
            System.arraycopy(item, 0, result, index, item.length);
            index += item.length;
//            if(item[item.length-4]==0x00&&item[item.length-3]==0x00&&item[item.length-2]==0xff){//                L.i("result",clsPublic.bytesToHexString(mStringBuffer.toString().getBytes())) ;
//            }
            if (item.length == 6) {
                mLightColorResultData = new LightColorResultData(result);
                Logger.i(mLightColorResultData.toString());
//                L.i("result", clsPublic.bytesToHexString(result));
                mContext.sendBroadcast(new Intent(RECEIVE_TEST_DATA));
            }
//            if (characteristic.equals(mBTValueCharacteristic)) {
////                getAndDisplayHrValue();
//            }
        }
    }

    @Override
    public ResultData getResultData() {
        return mLightColorResultData;
    }


    public static class LightColorResultData  extends ResultData{
        MeasureType mResultType;
        ArrayList<Double> SCI;
        ArrayList<Double> SCE;
        public LightColorResultData(){
        }

        public LightColorResultData(byte[] ble_data) {
            native_data=ble_data;
            if (ble_data.length == 327) {
                mResultType = MeasureType.base;
                byte[] sci_bytes = new byte[124];
                byte[] sce_bytes = new byte[124];
                System.arraycopy(ble_data, 33, sci_bytes, 0, 124);
                System.arraycopy(ble_data, 177, sce_bytes, 0, 124);
                SCI = clsPublic.getDouble(sci_bytes,31);
                SCE = clsPublic.getDouble(sce_bytes,31);
            } else if (ble_data.length == 5) {
                if (ble_data[1] == 2) {
                    mResultType = MeasureType.white;
                }else if(ble_data[1]==3){
                    mResultType = MeasureType.black;
                }else if(ble_data[1]==6){
                    mResultType= MeasureType.offTime;
                }
            }
        }

        public ArrayList<Double> getSCI() {
            return SCI;
        }

        public void setSCI( ArrayList<Double> SCI) {
            this.SCI = SCI;
        }

        public ArrayList<Double> getSCE() {
            return SCE;
        }

        public void setSCE( ArrayList<Double> SCE) {
            this.SCE = SCE;
        }

        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append("SCI:");
            for (int i = 0; i < SCI.size(); i++) {
                sb.append(String.valueOf(SCI.get(i)) + "  ");
            }
            sb.append("\n");
            sb.append("SCE:");
            for (int i = 0; i < SCE.size(); i++) {
                sb.append(String.valueOf(SCE.get(i)) + "  ");
            }
            return sb.toString();
        }
    }
}
