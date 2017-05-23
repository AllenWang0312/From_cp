package edu.tjrac.swant.color_measurement;


import edu.tjrac.swant.color_measurement.constast.Contast1;

/**
 * Created by cimcenter on 2016/12/9.
 */

public class CountK {
    static double k;
    public static double countK(int m_nGyType,int Degree_mode){
        switch (Degree_mode) {
            case 1:           // 2°角标准观察者
                switch (m_nGyType) {
                    case Contast1.ILLUMINANT_A:
                        k = 0.092684062;
                        break;

                    case Contast1.ILLUMINANT_C:
                        k = 0.09392343;
                        break;

                    case Contast1.ILLUMINANT_D50:
                        k = 0.095187626;
                        break;

                    case Contast1.ILLUMINANT_D55:
                        k = 0.09507290;
                        break;

                    case Contast1.ILLUMINANT_D65:
                        k = 0.094625798;
                        break;

                    case Contast1.ILLUMINANT_D75:
                        k = 0.09394901;
                        break;

                    case Contast1.ILLUMINANT_F1:
                        k = 0.70852368;
                        break;

                    case Contast1.ILLUMINANT_CWF:
                    case Contast1.ILLUMINANT_F2:
                        k = 0.704856919;
                        break;

                    case Contast1.ILLUMINANT_F3:
                    case Contast1.ILLUMINANT_U35:
                        k = 0.70458397;
                        break;

                    case Contast1.ILLUMINANT_F4:
                        k = 0.70455217;
                        break;

                    case Contast1.ILLUMINANT_F5:
                        k = 0.70718545;
                        break;

                    case Contast1.ILLUMINANT_F6:
                        k = 0.704160961;
                        break;

                    case Contast1.ILLUMINANT_DLF:
                    case Contast1.ILLUMINANT_F7:
                        k = 0.711320291;
                        break;

                    case Contast1.ILLUMINANT_F8:
                        k = 0.710406614;
                        break;

                    case Contast1.ILLUMINANT_F9:
                        k = 0.70955055;
                        break;

                    case Contast1.ILLUMINANT_F10:
                        k = 0.693010279;
                        break;

                    case Contast1.ILLUMINANT_TL84:
                    case Contast1.ILLUMINANT_NBF:
                    case Contast1.ILLUMINANT_F11:
                        k = 0.689129273;
                        break;

                    case Contast1.ILLUMINANT_U30:
                    case Contast1.ILLUMINANT_TL83:
                    case Contast1.ILLUMINANT_F12:
                        k = 0.684598361;
                        break;

                    default:
                        break;

                }
                break;

            case 0:           // 10°角标准观察者
                switch (m_nGyType) {
                    case Contast1.ILLUMINANT_A:
                        k = 0.087889629;
                        break;

                    case Contast1.ILLUMINANT_C:
                        k = 0.085134669;
                        break;

                    case Contast1.ILLUMINANT_D50:
                        k = 0.087712838;
                        break;

                    case Contast1.ILLUMINANT_D55:
                        k = 0.08718461;
                        break;

                    case Contast1.ILLUMINANT_D65:
                        k = 0.086082;
                        break;

                    case Contast1.ILLUMINANT_D75:
                        k = 0.08488347;
                        break;

                    case Contast1.ILLUMINANT_F1:
                        k = 0.65303785;
                        break;

                    case Contast1.ILLUMINANT_CWF:
                    case Contast1.ILLUMINANT_F2:
                        k = 0.667289864;
                        break;

                    case Contast1.ILLUMINANT_F3:
                    case Contast1.ILLUMINANT_U35:
                        k = 0.67589889;
                        break;

                    case Contast1.ILLUMINANT_F4:
                        k = 0.68135389;
                        break;

                    case Contast1.ILLUMINANT_F5:
                        k = 0.65414866;
                        break;

                    case Contast1.ILLUMINANT_F6:
                        k = 0.670462607;
                        break;

                    case Contast1.ILLUMINANT_DLF:
                    case Contast1.ILLUMINANT_F7:
                        k = 0.652037151;

                        break;

                    case Contast1.ILLUMINANT_F8:
                        k = 0.657517923;
                        break;

                    case Contast1.ILLUMINANT_F9:
                        k = 0.66510438;
                        break;

                    case Contast1.ILLUMINANT_F10:
                        k = 0.651071267;
                        break;

                    case Contast1.ILLUMINANT_TL84:
                    case Contast1.ILLUMINANT_NBF:
                    case Contast1.ILLUMINANT_F11:
                        k = 0.653252153;
                        break;

                    case Contast1.ILLUMINANT_U30:
                    case Contast1.ILLUMINANT_TL83:
                    case Contast1.ILLUMINANT_F12:
                        k = 0.655963561;
                        break;

                    default:
                        break;

                }
                break;
        }
        return k;
    }

}
