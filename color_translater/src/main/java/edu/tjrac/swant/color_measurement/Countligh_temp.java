package edu.tjrac.swant.color_measurement;

import edu.tjrac.swant.color_measurement.constast.Contast1;
import edu.tjrac.swant.color_measurement.constast.Contast2;

/**
 * Created by cimcenter on 2016/12/9.
 */

public class Countligh_temp {
    public static  int count = 31;
    static double[]  ligh_temp = new double[count];

    public static  double[] countligh_temp(int m_nGyType){
        int i;
        switch (m_nGyType) {
            case Contast1.ILLUMINANT_A:
                for (i = 0; i <count; i++) {
                    ligh_temp[i] = Contast2.L_A[i];
                }
                break;
            case Contast1.ILLUMINANT_C:
                for (i = 0; i <count; i++) {
                    ligh_temp[i] = Contast2.L_C[i];
                }
                break;

            case Contast1.ILLUMINANT_D50:
                for (i = 0; i <count; i++) {
                    ligh_temp[i] = Contast2.L_D50[i];
                }
                break;

            case Contast1.ILLUMINANT_D55:
                for (i = 0; i <count; i++) {
                    ligh_temp[i] = Contast2.L_D55[i];
                }
                break;

            case Contast1.ILLUMINANT_D65:
                for (i = 0; i <count; i++) {
                    ligh_temp[i] = Contast2.L_D65[i];
                }
                break;

            case Contast1.ILLUMINANT_D75:
                for (i = 0; i <count; i++) {
                    ligh_temp[i] = Contast2.L_D75[i];
                }
                break;

            case Contast1.ILLUMINANT_F1:
                for (i = 0; i <count; i++) {
                    ligh_temp[i] = Contast2.L_F1[i];
                }
                break;
            case Contast1.ILLUMINANT_CWF:

            case Contast1.ILLUMINANT_F2:
                for (i = 0; i <count; i++) {
                    ligh_temp[i] = Contast2.L_F2[i];
                }
                break;

            case Contast1.ILLUMINANT_F3:

            case Contast1.ILLUMINANT_U35:
                for (i = 0; i <count; i++) {
                    ligh_temp[i] = Contast2.L_F3[i];
                }
                break;

            case Contast1.ILLUMINANT_F4:
                for (i = 0; i <count; i++) {
                    ligh_temp[i] = Contast2.L_F4[i];
                }
                break;

            case Contast1.ILLUMINANT_F5:
                for (i = 0; i <count; i++) {
                    ligh_temp[i] = Contast2.L_F5[i];
                }
                break;

            case Contast1.ILLUMINANT_F6:
                for (i = 0; i <count; i++) {
                    ligh_temp[i] = Contast2.L_F6[i];
                }
                break;

            case Contast1.ILLUMINANT_DLF:

            case Contast1.ILLUMINANT_F7:
                for (i = 0; i <count; i++) {
                    ligh_temp[i] = Contast2.L_F7[i];
                }
                break;

            case Contast1.ILLUMINANT_F8:
                for (i = 0; i <count; i++) {
                    ligh_temp[i] = Contast2.L_F8[i];
                }
                break;

            case Contast1.ILLUMINANT_F9:
                for (i = 0; i <count; i++) {
                    ligh_temp[i] = Contast2.L_F9[i];
                }
                break;

            case Contast1.ILLUMINANT_F10:
                for (i = 0; i <count; i++) {
                    ligh_temp[i] = Contast2.L_F10[i];
                }
                break;

            case Contast1.ILLUMINANT_TL84:
            case Contast1.ILLUMINANT_NBF:
            case Contast1.ILLUMINANT_F11:
                for (i = 0; i <count; i++) {
                    ligh_temp[i] = Contast2.L_F11[i];
                }
                break;

            case Contast1.ILLUMINANT_U30:
            case Contast1.ILLUMINANT_TL83:
            case Contast1.ILLUMINANT_F12:
                for (i = 0; i <count; i++) {
                    ligh_temp[i] = Contast2.L_F12[i];
                }
                break;

            default:
                break;

        }
        return ligh_temp;
    }
}
