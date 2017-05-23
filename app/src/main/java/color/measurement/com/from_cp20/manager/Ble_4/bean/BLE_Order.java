package color.measurement.com.from_cp20.manager.Ble_4.bean;

/**
 * Created by wpc on 2016/12/15.
 */

public class BLE_Order {

    public static class LC {
        public static final  byte[] TEST_DATA={(byte) 0xbb, 0x01, (byte) 0xff, (byte) 0xbb};

//        public static byte[] getTestData() {
//            return new byte[]{(byte) 0xbb, 0x01, (byte) 0xff, (byte) 0xbb};
//        }

        public static byte[] getBlackData() {
            return new byte[]{(byte) 0xbb, 0x02, (byte) 0xff, (byte) 0xbc};
        }


        public static byte[] getWriteData() {
            return new byte[]{(byte) 0xbb, 0x03, (byte) 0xff, (byte) 0xbd};
        }

        public static byte[] getPowerData() {
            return new byte[]{(byte) 0xbb, 0x04, (byte) 0xff, (byte) 0xbe};
        }

        public static byte[] getXLHData() {
            return new byte[]{(byte) 0xbb, 0x05, (byte) 0xff, (byte) 0xbf};
        }

        public static byte[] setTurnDownData() {
            return new byte[]{(byte) 0xbb, 0x06, (byte) 0xff, (byte) 0xbb};
        }
    }

    public static class L{
        public static final  byte[] TEST_DATA={(byte) 0xbc, 0x01, (byte) 0xff, (byte) 0xbc};
    }

}