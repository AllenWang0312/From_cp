package edu.tjrac.swant.color_measurement;

import java.util.ArrayList;
import java.util.List;

import edu.tjrac.swant.color_measurement.constast.Contast1;


/**
 * Created by cimcenter on 2016/12/5.
 */

public class OrDer {
    public static List set_Illuminant_Observer(int m_nGyType1, int Degree_mode1){
        switch (Degree_mode1) {
            case 1:   //2°角标准观察者
                switch (m_nGyType1) {
                    case Contast1.ILLUMINANT_A:
                        Contast1.Illuminant_Observer_X = Contast1.XYZ2LABCH_2_A[0];
                        Contast1.Illuminant_Observer_Y = Contast1.XYZ2LABCH_2_A[1];
                        Contast1.Illuminant_Observer_Z = Contast1.XYZ2LABCH_2_A[2];
                        break;
                    case Contast1.ILLUMINANT_C:
                        Contast1.Illuminant_Observer_X = Contast1.XYZ2LABCH_2_C[0];
                        Contast1.Illuminant_Observer_Y = Contast1.XYZ2LABCH_2_C[1];
                        Contast1.Illuminant_Observer_Z = Contast1.XYZ2LABCH_2_C[2];
                        break;
                    case Contast1.ILLUMINANT_D50:
                        Contast1.Illuminant_Observer_X = Contast1.XYZ2LABCH_2_D50[0];
                        Contast1.Illuminant_Observer_Y = Contast1.XYZ2LABCH_2_D50[1];
                        Contast1.Illuminant_Observer_Z = Contast1.XYZ2LABCH_2_D50[2];
                        break;
                    case Contast1.ILLUMINANT_D55:
                        Contast1.Illuminant_Observer_X = Contast1.XYZ2LABCH_2_D55[0];
                        Contast1.Illuminant_Observer_Y = Contast1.XYZ2LABCH_2_D55[1];
                        Contast1.Illuminant_Observer_Z = Contast1.XYZ2LABCH_2_D55[2];
                        break;
                    case Contast1.ILLUMINANT_D65:
                        Contast1.Illuminant_Observer_X = Contast1.XYZ2LABCH_2_D65[0];
                        Contast1.Illuminant_Observer_Y = Contast1.XYZ2LABCH_2_D65[1];
                        Contast1.Illuminant_Observer_Z = Contast1.XYZ2LABCH_2_D65[2];
                        break;
                    case Contast1.ILLUMINANT_D75:
                        Contast1.Illuminant_Observer_X = Contast1.XYZ2LABCH_2_D75[0];
                        Contast1.Illuminant_Observer_Y = Contast1.XYZ2LABCH_2_D75[1];
                        Contast1.Illuminant_Observer_Z = Contast1.XYZ2LABCH_2_D75[2];
                        break;
                    case Contast1.ILLUMINANT_F1:
                        Contast1.Illuminant_Observer_X = Contast1.XYZ2LABCH_2_F1[0];
                        Contast1.Illuminant_Observer_Y = Contast1.XYZ2LABCH_2_F1[1];
                        Contast1.Illuminant_Observer_Z = Contast1.XYZ2LABCH_2_F1[2];
                        break;

                    case Contast1.ILLUMINANT_CWF:
                    case Contast1.ILLUMINANT_F2:
                        Contast1.Illuminant_Observer_X = Contast1.XYZ2LABCH_2_F2[0];
                        Contast1.Illuminant_Observer_Y = Contast1.XYZ2LABCH_2_F2[1];
                        Contast1.Illuminant_Observer_Z = Contast1.XYZ2LABCH_2_F2[2];
                        break;
                    case Contast1.ILLUMINANT_F3:
                    case Contast1.ILLUMINANT_U35:
                        Contast1.Illuminant_Observer_X = Contast1.XYZ2LABCH_2_F3[0];
                        Contast1.Illuminant_Observer_Y = Contast1.XYZ2LABCH_2_F3[1];
                        Contast1.Illuminant_Observer_Z = Contast1.XYZ2LABCH_2_F3[2];
                        break;
                    case Contast1.ILLUMINANT_F4:
                        Contast1.Illuminant_Observer_X = Contast1.XYZ2LABCH_2_F4[0];
                        Contast1.Illuminant_Observer_Y = Contast1.XYZ2LABCH_2_F4[1];
                        Contast1.Illuminant_Observer_Z = Contast1.XYZ2LABCH_2_F4[2];
                        break;
                    case Contast1.ILLUMINANT_F5:
                        Contast1.Illuminant_Observer_X = Contast1.XYZ2LABCH_2_F5[0];
                        Contast1.Illuminant_Observer_Y = Contast1.XYZ2LABCH_2_F5[1];
                        Contast1.Illuminant_Observer_Z = Contast1.XYZ2LABCH_2_F5[2];
                        break;
                    case Contast1.ILLUMINANT_F6:
                        Contast1.Illuminant_Observer_X = Contast1.XYZ2LABCH_2_F6[0];
                        Contast1.Illuminant_Observer_Y = Contast1.XYZ2LABCH_2_F6[1];
                        Contast1.Illuminant_Observer_Z = Contast1.XYZ2LABCH_2_F6[2];
                        break;

                    case Contast1.ILLUMINANT_DLF:
                    case Contast1.ILLUMINANT_F7:
                        Contast1.Illuminant_Observer_X = Contast1.XYZ2LABCH_2_F7[0];
                        Contast1.Illuminant_Observer_Y = Contast1.XYZ2LABCH_2_F7[1];
                        Contast1.Illuminant_Observer_Z = Contast1.XYZ2LABCH_2_F7[2];
                        break;
                    case Contast1.ILLUMINANT_F8:
                        Contast1.Illuminant_Observer_X = Contast1.XYZ2LABCH_2_F8[0];
                        Contast1.Illuminant_Observer_Y = Contast1.XYZ2LABCH_2_F8[1];
                        Contast1.Illuminant_Observer_Z = Contast1.XYZ2LABCH_2_F8[2];
                        break;
                    case Contast1.ILLUMINANT_F9:
                        Contast1.Illuminant_Observer_X = Contast1.XYZ2LABCH_2_F9[0];
                        Contast1.Illuminant_Observer_Y = Contast1.XYZ2LABCH_2_F9[1];
                        Contast1.Illuminant_Observer_Z = Contast1.XYZ2LABCH_2_F9[2];
                        break;
                    case Contast1.ILLUMINANT_F10:
                        Contast1.Illuminant_Observer_X = Contast1.XYZ2LABCH_2_F10[0];
                        Contast1.Illuminant_Observer_Y = Contast1.XYZ2LABCH_2_F10[1];
                        Contast1.Illuminant_Observer_Z = Contast1.XYZ2LABCH_2_F10[2];
                        break;
                    case Contast1.ILLUMINANT_TL84:
                    case Contast1.ILLUMINANT_NBF:
                    case Contast1.ILLUMINANT_F11:
                        Contast1.Illuminant_Observer_X = Contast1.XYZ2LABCH_2_F11[0];
                        Contast1.Illuminant_Observer_Y = Contast1.XYZ2LABCH_2_F11[1];
                        Contast1.Illuminant_Observer_Z = Contast1.XYZ2LABCH_2_F11[2];
                        break;
                    case Contast1.ILLUMINANT_U30:
                    case Contast1.ILLUMINANT_TL83:
                    case Contast1.ILLUMINANT_F12:
                        Contast1.Illuminant_Observer_X = Contast1.XYZ2LABCH_2_F12[0];
                        Contast1.Illuminant_Observer_Y = Contast1.XYZ2LABCH_2_F12[1];
                        Contast1.Illuminant_Observer_Z = Contast1.XYZ2LABCH_2_F12[2];
                        break;

                    default:
                        Contast1.Illuminant_Observer_X = Contast1.XYZ2LABCH_2_D65[0];
                        Contast1.Illuminant_Observer_Y = Contast1.XYZ2LABCH_2_D65[1];
                        Contast1.Illuminant_Observer_Z = Contast1.XYZ2LABCH_2_D65[2];
                        break;
                }
                break;

            case 0: //10°角标准观察者
                switch (m_nGyType1) {
                    case Contast1.ILLUMINANT_A:
                        Contast1.Illuminant_Observer_X = Contast1.XYZ2LABCH_10_A[0];
                        Contast1.Illuminant_Observer_Y = Contast1.XYZ2LABCH_10_A[1];
                        Contast1.Illuminant_Observer_Z = Contast1.XYZ2LABCH_10_A[2];
                        break;
                    case Contast1.ILLUMINANT_C:
                        Contast1.Illuminant_Observer_X = Contast1.XYZ2LABCH_10_C[0];
                        Contast1.Illuminant_Observer_Y = Contast1.XYZ2LABCH_10_C[1];
                        Contast1.Illuminant_Observer_Z = Contast1.XYZ2LABCH_10_C[2];
                        break;
                    case Contast1.ILLUMINANT_D50:
                        Contast1.Illuminant_Observer_X = Contast1.XYZ2LABCH_10_D50[0];
                        Contast1.Illuminant_Observer_Y = Contast1.XYZ2LABCH_10_D50[1];
                        Contast1.Illuminant_Observer_Z = Contast1.XYZ2LABCH_10_D50[2];
                        break;
                    case Contast1.ILLUMINANT_D55:
                        Contast1.Illuminant_Observer_X = Contast1.XYZ2LABCH_10_D55[0];
                        Contast1.Illuminant_Observer_Y = Contast1.XYZ2LABCH_10_D55[1];
                        Contast1.Illuminant_Observer_Z = Contast1.XYZ2LABCH_10_D55[2];
                        break;
                    case Contast1.ILLUMINANT_D65:
                        Contast1.Illuminant_Observer_X = Contast1.XYZ2LABCH_10_D65[0];
                        Contast1.Illuminant_Observer_Y = Contast1.XYZ2LABCH_10_D65[1];
                        Contast1.Illuminant_Observer_Z = Contast1.XYZ2LABCH_10_D65[2];
                        break;
                    case Contast1.ILLUMINANT_D75:
                        Contast1.Illuminant_Observer_X = Contast1.XYZ2LABCH_10_D75[0];
                        Contast1.Illuminant_Observer_Y = Contast1.XYZ2LABCH_10_D75[1];
                        Contast1.Illuminant_Observer_Z = Contast1.XYZ2LABCH_10_D75[2];
                        break;
                    case Contast1.ILLUMINANT_F1:
                        Contast1.Illuminant_Observer_X = Contast1.XYZ2LABCH_10_F1[0];
                        Contast1.Illuminant_Observer_Y = Contast1.XYZ2LABCH_10_F1[1];
                        Contast1.Illuminant_Observer_Z = Contast1.XYZ2LABCH_10_F1[2];
                        break;

                    case Contast1.ILLUMINANT_CWF:
                    case Contast1.ILLUMINANT_F2:
                        Contast1.Illuminant_Observer_X = Contast1.XYZ2LABCH_10_F2[0];
                        Contast1.Illuminant_Observer_Y = Contast1.XYZ2LABCH_10_F2[1];
                        Contast1.Illuminant_Observer_Z = Contast1.XYZ2LABCH_10_F2[2];
                        break;
                    case Contast1.ILLUMINANT_F3:
                    case Contast1.ILLUMINANT_U35:
                        Contast1.Illuminant_Observer_X = Contast1.XYZ2LABCH_10_F3[0];
                        Contast1.Illuminant_Observer_Y = Contast1.XYZ2LABCH_10_F3[1];
                        Contast1.Illuminant_Observer_Z = Contast1.XYZ2LABCH_10_F3[2];
                        break;
                    case Contast1.ILLUMINANT_F4:
                        Contast1.Illuminant_Observer_X = Contast1.XYZ2LABCH_10_F4[0];
                        Contast1.Illuminant_Observer_Y = Contast1.XYZ2LABCH_10_F4[1];
                        Contast1.Illuminant_Observer_Z = Contast1.XYZ2LABCH_10_F4[2];
                        break;
                    case Contast1.ILLUMINANT_F5:
                        Contast1.Illuminant_Observer_X = Contast1.XYZ2LABCH_10_F5[0];
                        Contast1.Illuminant_Observer_Y = Contast1.XYZ2LABCH_10_F5[1];
                        Contast1.Illuminant_Observer_Z = Contast1.XYZ2LABCH_10_F5[2];
                        break;
                    case Contast1.ILLUMINANT_F6:
                        Contast1.Illuminant_Observer_X = Contast1.XYZ2LABCH_10_F6[0];
                        Contast1.Illuminant_Observer_Y = Contast1.XYZ2LABCH_10_F6[1];
                        Contast1.Illuminant_Observer_Z = Contast1.XYZ2LABCH_10_F6[2];
                        break;
                    case Contast1.ILLUMINANT_DLF:
                    case Contast1.ILLUMINANT_F7:
                        Contast1.Illuminant_Observer_X = Contast1.XYZ2LABCH_10_F7[0];
                        Contast1.Illuminant_Observer_Y = Contast1.XYZ2LABCH_10_F7[1];
                        Contast1.Illuminant_Observer_Z = Contast1.XYZ2LABCH_10_F7[2];
                        break;
                    case Contast1.ILLUMINANT_F8:
                        Contast1.Illuminant_Observer_X = Contast1.XYZ2LABCH_10_F8[0];
                        Contast1.Illuminant_Observer_Y = Contast1.XYZ2LABCH_10_F8[1];
                        Contast1.Illuminant_Observer_Z = Contast1.XYZ2LABCH_10_F8[2];
                        break;
                    case Contast1.ILLUMINANT_F9:
                        Contast1.Illuminant_Observer_X = Contast1.XYZ2LABCH_10_F9[0];
                        Contast1.Illuminant_Observer_Y = Contast1.XYZ2LABCH_10_F9[1];
                        Contast1.Illuminant_Observer_Z = Contast1.XYZ2LABCH_10_F9[2];
                        break;
                    case Contast1.ILLUMINANT_F10:
                        Contast1.Illuminant_Observer_X = Contast1.XYZ2LABCH_10_F10[0];
                        Contast1.Illuminant_Observer_Y = Contast1.XYZ2LABCH_10_F10[1];
                        Contast1.Illuminant_Observer_Z = Contast1.XYZ2LABCH_10_F10[2];
                        break;
                    case Contast1.ILLUMINANT_TL84:
                    case Contast1.ILLUMINANT_NBF:
                    case Contast1.ILLUMINANT_F11:
                        Contast1.Illuminant_Observer_X = Contast1.XYZ2LABCH_10_F11[0];
                        Contast1.Illuminant_Observer_Y = Contast1.XYZ2LABCH_10_F11[1];
                        Contast1.Illuminant_Observer_Z = Contast1.XYZ2LABCH_10_F11[2];
                        break;
                    case Contast1.ILLUMINANT_U30:
                    case Contast1.ILLUMINANT_TL83:
                    case Contast1.ILLUMINANT_F12:
                        Contast1.Illuminant_Observer_X = Contast1.XYZ2LABCH_10_F12[0];
                        Contast1.Illuminant_Observer_Y = Contast1.XYZ2LABCH_10_F12[1];
                        Contast1.Illuminant_Observer_Z = Contast1.XYZ2LABCH_10_F12[2];
                        break;

                    default:
                        Contast1.Illuminant_Observer_X = Contast1.XYZ2LABCH_10_D65[0];
                        Contast1.Illuminant_Observer_Y = Contast1.XYZ2LABCH_10_D65[1];
                        Contast1.Illuminant_Observer_Z = Contast1.XYZ2LABCH_10_D65[2];
                        break;
                }
                break;
        }
        List list = new ArrayList();
        list.add(Contast1.Illuminant_Observer_X);
        list.add(Contast1.Illuminant_Observer_Y);
        list.add(Contast1.Illuminant_Observer_Z);
        return list;
    }
}
