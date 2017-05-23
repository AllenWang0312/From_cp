package edu.tjrac.swant.color_measurement;

import java.util.ArrayList;
import java.util.List;

import edu.tjrac.swant.color_measurement.constast.Contast2;
import edu.tjrac.swant.color_measurement.constast.Contast4;
import edu.tjrac.swant.color_measurement.constast.Contast5;

import static edu.tjrac.swant.color_measurement.constast.Contast1.ILLUMINANT_C;
import static edu.tjrac.swant.color_measurement.constast.Contast1.ILLUMINANT_D50;
import static edu.tjrac.swant.color_measurement.constast.Contast1.ILLUMINANT_D65;
import static edu.tjrac.swant.color_measurement.constast.Contast6.MSE_H;
import static edu.tjrac.swant.color_measurement.constast.Contast7.MSE_C;
import static edu.tjrac.swant.color_measurement.constast.Contast8.DensityA_B_Coefficient;
import static edu.tjrac.swant.color_measurement.constast.Contast8.DensityA_G_Coefficient;
import static edu.tjrac.swant.color_measurement.constast.Contast8.DensityA_R_Coefficient;
import static edu.tjrac.swant.color_measurement.constast.Contast8.DensityE_B_Coefficient;
import static edu.tjrac.swant.color_measurement.constast.Contast8.DensityE_G_Coefficient;
import static edu.tjrac.swant.color_measurement.constast.Contast8.DensityE_R_Coefficient;
import static edu.tjrac.swant.color_measurement.constast.Contast8.DensityM_B_Coefficient;
import static edu.tjrac.swant.color_measurement.constast.Contast8.DensityM_G_Coefficient;
import static edu.tjrac.swant.color_measurement.constast.Contast8.DensityM_R_Coefficient;
import static edu.tjrac.swant.color_measurement.constast.Contast8.DensityT_B_Coefficient;
import static edu.tjrac.swant.color_measurement.constast.Contast8.DensityT_G_Coefficient;
import static edu.tjrac.swant.color_measurement.constast.Contast8.DensityT_R_Coefficient;
import static edu.tjrac.swant.color_measurement.constast.Contast8.Density_V_Coefficient;
import static edu.tjrac.swant.color_measurement.constast.Contast9.MSE_H_S;

/**
 * color工具类
 * Created by cimcenter on 2016/12/6.
 */

public class ColorUtil_Measure {

    public static int count = 31;


    public static List<Double> XYZtoCIE_LabCH(Double X, Double Y, Double Z, int m_nGyType, int Degree_mode) {
        double L = 0, a = 0, b = 0, C = 0, H = 0;
        List list = new ArrayList();
        list = OrDer.set_Illuminant_Observer(m_nGyType, Degree_mode);
        double temX = X / (double) list.get(0); // temX;//白板X值
        double temY = Y / (double) list.get(1); // temY;//白板Y值
        double temZ = Z / (double) list.get(2); // temZ;//白板Z值
        if (temX > 0.008856) {
            temX = Math.pow((double) temX, (double) 0.3333333);
        } else {
            temX = (7.787 * temX) + 0.138;
        }
        if (temY > 0.008856) {
            temY = Math.pow(temY, 0.3333333);
            L = 116 * temY - 16;
        } else {
            L = 903.3 * temY;
            temY = (7.787 * temY) + 0.138;
        }
        if (temZ > 0.008856) {
            temZ = Math.pow((double) temZ, (double) 0.3333333);
        } else {
            temZ = (7.787 * temZ) + 0.138;
        }
        a = 500.0 * (temX - temY);
        b = 200.0 * (temY - temZ);
        if (L < 0) L = 0.00;
        C = Math.sqrt(a * a + b * b);
        if ((a == 0) && (b > 0))
            H = 90;
        else if ((a == 0) && (b < 0))
            H = 270;
        else if ((a >= 0) && (b == 0))
            H = 0;
        else if ((a < 0) && (b == 0))
            H = 180;
        else {
            H = Math.atan(b / a);
            H = H * 57.3;
            if ((a > 0) && (b > 0))
                H = H;
            else if (a < 0)
                H = 180 + H;
            else
                H = 360 + H;
        }
        List list2 = new ArrayList();
        list2.add(L);
        list2.add(a);
        list2.add(b);
        list2.add(C);
        list2.add(H);
        return list2;
    }
    public static List<Double> XYZtoXYZ(Double X, Double Y, Double Z) {
        List list = new ArrayList();
        list.add(X);
        list.add(Y);
        list.add(Z);
        return list;
    }
    public static List<Double> comuteXYZ(List<Double> data_SCI, int m_nGyType, int Degree_mode) {
        int i;
        double x, y, z;
        double[] ligh_temp = new double[41];
        double xx[] = new double[41];
        double yy[] = new double[41];
        double zz[] = new double[41];
        double k = 0;
        double XYZ[] = new double[5];

        switch (Degree_mode) {
            case 1:           // 2°角标准观察者
                for (i = 0; i < count; i++) {
                    xx[i] = Contast2.xx_2[i];
                    yy[i] = Contast2.yy_2[i];
                    zz[i] = Contast2.zz_2[i];
                }
                break;

            case 0:           // 10°角标准观察者
                for (i = 0; i < count; i++) {
                    xx[i] = Contast2.xx_10[i];
                    yy[i] = Contast2.yy_10[i];
                    zz[i] = Contast2.zz_10[i];
                }
                break;
        }
        ligh_temp = Countligh_temp.countligh_temp(m_nGyType);
        //Contast1.ILLUMINANT_D65  0
        k = CountK.countK(m_nGyType, Degree_mode);
        XYZ[0] = XYZ[1] = XYZ[2] = 0;
        for (i = 0; i < 31; i++) {
            XYZ[0] += xx[i] * (data_SCI.get(i)* 0.01) * ligh_temp[i];
            XYZ[1] += yy[i] * (data_SCI.get(i) * 0.01) * ligh_temp[i];
            XYZ[2] += zz[i] * (data_SCI.get(i) * 0.01) * ligh_temp[i];
        }
        x = XYZ[0] * k;
        y = XYZ[1] * k;
        z = XYZ[2] * k;
        List list = new ArrayList();
        list.add(x);
        list.add(y);
        list.add(z);
        return list;
    }
    public static List<Double> XYZtoLuv(Double X, Double Y, Double Z, int m_nGyType, int Degree_mode) {
        double temX = 0, temY = 0, temZ = 0;
        double u1, v1, un1, vn1, L, u, v;
        double l = 0;
        List list = OrDer.set_Illuminant_Observer(m_nGyType, Degree_mode);
        temX = (double) list.get(0);
        temY = (double) list.get(1);
        temZ = (double) list.get(2);

        if (Y / temY > 0.008856) {
            L = 116 * Math.pow(Y / temY, 1 / 3) - 16;
            l = 116 * Math.pow(Y / temY, 1.0 / 3) - 16;
        } else            // 带确定
            L = 903.3 * (Y / temY);

        u1 = 4 * X / (X + 15 * Y + 3 * Z);
        v1 = 9 * X / (X + 15 * Y + 3 * Z);

        un1 = 4 * temX / (temX + 15 * temY + 3 * temZ);
        vn1 = 9 * temX / (temX + 15 * temY + 3 * temZ);
        u = 13 * L * (u1 - un1);
        v = 13 * L * (v1 - vn1);
        List list1 = new ArrayList();
        list1.add(l);
        list1.add(u);
        list1.add(v);
        return list1;
    }

    public static List<Double> XYZtoHunterLab(Double X, Double Y, Double Z, int m_nGyType, int Degree_mode) {
        double L, a, b;
        double temX, temY, temZ;
        List list = new ArrayList();
        list = OrDer.set_Illuminant_Observer(m_nGyType, Degree_mode);
        temX = (double) list.get(0);
        temY = (double) list.get(1);
        temZ = (double) list.get(2);
        L = 100 * Math.sqrt(Y / temY);
        a = 175 * Math.sqrt(0.0102 * temX / (Y / temY)) * (X / temX - Y / temY);
        b = 70 * Math.sqrt(0.00847 * temZ / (Y / temY)) * (Y / temY - Z / temZ);
        List list1 = new ArrayList();
        list1.add(L);
        list1.add(a);
        list1.add(b);
        return list1;
    }

    public static List<Double> XYZtoYxy(Double X, Double Y, Double Z) {
        double x, y;
        x = X / (X + Y + Z);
        y = Y / (X + Y + Z);
        if (X + Y + Z > 0) {
            x = X / (X + Y + Z);
            y = Y / (X + Y + Z);
        } else {
            x = 0;
            y = 0;
        }
        List list = new ArrayList();
        list.add(Y);
        list.add(x);
        list.add(y);
        return list;
    }
    public static List<Double> getSecha(Double X, Double Y, Double Z){
        List<Double> secha=new ArrayList<>();
        for (int i = 0; i <11 ; i++) {
            secha.add(Math.random()*100);
        }
        return secha;
    }

    public static List<Double> XYZtoWI_TINT_CIE(Double X, Double Y, Double Z, int m_nGyType, int Degree_mode) {
        double x, y;
        double Tw = 0;
        double WI = 0; //白度0
        double Hunter_L, Hunter_a, Hunter_b;
        double Tint_CIE, Tint_E313, Tint_Ganz;
        double YI_E313_1973, YI_E313_1996_98_10_05_2010, YI_D1925;
        double YI_1925 = 0;  //YI ASTM D1925
        double WI_E313 = 0;  //WI ASTM E313
        double WI_E313_98 = 0;//WI ASTM E313-98
        double YI_E313 = 0;
        double WC10 = 0;  //干次百度
        double TWG10 = 0; //
        double WI_CIE, WI_Ganz, WI_Hunter, WI_Taube, WI_Stensby, WI_Berger, WI_AATCC, WI_E313_1973, WI_E313_1996_98_10_05_2010;
        double temX, temY, temZ;
        List list1 = new ArrayList();
        list1 = OrDer.set_Illuminant_Observer(m_nGyType, Degree_mode);
        temX = (double) list1.get(0);
        temY = (double) list1.get(1);
        temZ = (double) list1.get(2);
        Hunter_L = 100 * Math.sqrt(Y / temY);
        Hunter_a = 175 * Math.sqrt(0.0102 * temX / (Y / temY)) * (X / temX - Y / temY);
        Hunter_b = 70 * Math.sqrt(0.00847 * temZ / (Y / temY)) * (Y / temY - Z / temZ);
        List list = new ArrayList();
        list = OrDer.set_Illuminant_Observer(m_nGyType, Degree_mode);
        double Illuminant_Observer_Z = (double) list.get(2);
        if (X + Y + Z > 0) {
            x = X / (X + Y + Z);
            y = Y / (X + Y + Z);
        } else {
            x = 0;
            y = 0;
        }
        if (Degree_mode == 1) {
            //I模式
            WI = Y + 800 * (0.3127 - x) + 1700 * (0.3290 - y); //2度
            Tw = 1000 * (0.3127 - x) - 650 * (0.3290 - y);//2度

            YI_1925 = 100 * (1.28 * X - 1.06 * Z) / Y;
            WI_E313 = 3.37472 * Z - 2.99 * Y;
            WI_E313_98 = Y + 800 * (0.3101 - x) + 1700 * (0.3161 - y);
            ///////////////YI ASTM E313//////////////////
            YI_E313 = 100 * (1 - 0.847 * Z / Y);

            WC10 = 0; //2度
            TWG10 = 0; //2度
        } else if (Degree_mode == 0) {
            //I模式
            WI = Y + 800 * (0.3138 - x) + 1700 * (0.3309 - y);//10度
            Tw = 900 * (0.3138 - x) - 650 * (0.3309 - y);//10度
            YI_1925 = 100 * (1.28 * X - 1.06 * Z) / Y;
            WI_E313 = 3.37472 * Z - 2.99 * Y;
            WI_E313_98 = Y + 800 * (0.3101 - x) + 1700 * (0.3161 - y);
            WC10 = Y - 800 * x - 1700 * y + 813.7;  //10度
            TWG10 = 650 * y - 900 * x + 67.3;    //10度
        }
        double xn, yn;
        double Cx = 0, Cz = 0;
        double Tx, Ty;
        /********************************************************************************************************/
        /////////////////////////WI CIE////// only D65
        if (Degree_mode == 1) {
            xn = 0.3127;
            yn = 0.3290;
        } else {
            xn = 0.3138;
            yn = 0.3310;
        }
        if (ILLUMINANT_D65 == m_nGyType) {
            WI_CIE = Y + 800 * (xn - x) + 1700 * (yn - y);
        } else {
            WI_CIE = 0;
        }
        /********************************************************************************************************/
        ////////WI ASTM E313-1996 1998 2000 2005 2010//////
        if (Degree_mode == 1) {
            if (ILLUMINANT_C == m_nGyType) {
                xn = 0.3101;
                yn = 0.3161;
            } else if (ILLUMINANT_D50 == m_nGyType) {
                xn = 0.3457;
                yn = 0.3585;
            } else if (ILLUMINANT_D65 == m_nGyType) {
                xn = 0.3127;
                yn = 0.3290;
            }
        } else {
            if (ILLUMINANT_C == m_nGyType) {
                xn = 0.3104;
                yn = 0.3191;
            } else if (ILLUMINANT_D50 == m_nGyType) {
                xn = 0.3477;
                yn = 0.3595;
            } else if (ILLUMINANT_D65 == m_nGyType) {
                xn = 0.3138;
                yn = 0.3310;
            }
        }
        if (ILLUMINANT_C == m_nGyType || ILLUMINANT_D50 == m_nGyType || ILLUMINANT_D65 == m_nGyType) {
            WI_E313_1996_98_10_05_2010 = Y + 800 * (xn - x) + 1700 * (yn - y);
        } else {
            WI_E313_1996_98_10_05_2010 = 0;
        }
        /********************************************************************************************************/
        ////////WI AATCC//////	 = （ASTM E313 D65光源和C光源）
        if (ILLUMINANT_C == m_nGyType || ILLUMINANT_D65 == m_nGyType) {
            WI_AATCC = WI_E313_1996_98_10_05_2010;
        } else {
            WI_AATCC = 0;
        }
        /********************************************************************************************************/
        ////////Tint CIE//////	only D65
        if (ILLUMINANT_D65 == m_nGyType) {
            if (Degree_mode == 1) {
                Tint_CIE = 1000 * (0.3127 - x) - 650 * (0.3290 - y);
            } else {
                Tint_CIE = 900 * (0.3138 - x) - 650 * (0.3310 - y);
            }
        } else {
            Tint_CIE = 0;
        }

        /********************************************************************************************************/
        ////////Tint ASTM E313-1996 1998 2000 2005 2010//////
        if (Degree_mode == 1) {
            if (ILLUMINANT_C == m_nGyType) {
                xn = 0.3101;
                yn = 0.3161;
            } else if (ILLUMINANT_D50 == m_nGyType) {
                xn = 0.3457;
                yn = 0.3585;
            } else if (ILLUMINANT_D65 == m_nGyType) {
                xn = 0.3127;
                yn = 0.3290;
            }
            Tx = 1000;
            Ty = 650;
        } else {
            if (ILLUMINANT_C == m_nGyType) {
                xn = 0.3104;
                yn = 0.3191;
            } else if (ILLUMINANT_D50 == m_nGyType) {
                xn = 0.3477;
                yn = 0.3595;
            } else if (ILLUMINANT_D65 == m_nGyType) {
                xn = 0.3138;
                yn = 0.3310;
            }
            Tx = 900;
            Ty = 650;
        }
        if (ILLUMINANT_C == m_nGyType || ILLUMINANT_D50 == m_nGyType || ILLUMINANT_D65 == m_nGyType) {
            Tint_E313 = Tx * (xn - x) - Ty * (yn - y);
        } else {
            Tint_E313 = 0;
        }

        /********************************************************************************************************/
        ////////YI ASTM E313-1996 1998 2000 2005 2010//////
        if (Degree_mode == 1) {
            if (ILLUMINANT_C == m_nGyType) {
                Cx = 1.2769;
                Cz = 1.0592;
            } else if (ILLUMINANT_D65 == m_nGyType) {
                Cx = 1.2985;
                Cz = 1.1335;
            }
        } else {
            if (ILLUMINANT_C == m_nGyType) {
                Cx = 1.2871;
                Cz = 1.0781;
            } else if (ILLUMINANT_D65 == m_nGyType) {
                Cx = 1.3013;
                Cz = 1.1498;
            }
        }
        if (ILLUMINANT_C == m_nGyType || ILLUMINANT_D65 == m_nGyType) {
            YI_E313_1996_98_10_05_2010 = 100 * (Cx * X - Cz * Z) / Y;
        } else {
            YI_E313_1996_98_10_05_2010 = 0;
        }
        /********************************************************************************************************/
        ///////////////WI ASTM E313-1973//////////////////
        WI_E313_1973 = 3.388 * Z - 3.00 * Y;
        /********************************************************************************************************/
        ///////////////YI ASTM E313-1973//////////////////
        YI_E313_1973 = 100 * (1 - 0.847 * Z / Y);
        /********************************************************************************************************/
        ///////////////YI ASTM 1925//////////////////
        YI_D1925 = 100 * (1.28 * X - 1.06 * Z) / Y;
        /********************************************************************************************************/
        ///////////////WI Ganz//////////////////
        WI_Ganz = Y - 1868.322 * x - 3695.69 * y + 1809.441;
        /********************************************************************************************************/
        ///////////////TI Ganz//////////////////
        Tint_Ganz = -1001.223 * x + 748.366 * y + 68.261;
        /********************************************************************************************************/
        ///////////////WI Hunter//////////////////
        WI_Hunter = Hunter_L - 3 * Hunter_b;
        /********************************************************************************************************/
        ///////////////WI Stensby//////////////////
        WI_Stensby = Hunter_L - 3 * Hunter_b + 3 * Hunter_a;
        /********************************************************************************************************/
        ///////////////WI Taube//////////////////
        WI_Taube = 400.0 * Z / Illuminant_Observer_Z - 3 * Y;
        /********************************************************************************************************/
        ///////////////WI Berger//////////////////
        //WI_Berger = 0.333*Y+125*Z/Illuminant_Observer_Z-125*X/Illuminant_Observer_X;
        if (Degree_mode == 1) {
            WI_Berger = Y + 3.440 * Z - 3.895 * X;
        } else {
            WI_Berger = Y + 3.448 * Z - 3.904 * X;
        }
        /********************************************************************************************************/
        ///////////////////SCE///////////////////////
        /********************************************************************************************************/
        /////////////////////////WI CIE////// only D65
        if (Degree_mode == 1) {
            xn = 0.3127;
            yn = 0.3290;
        } else {
            xn = 0.3138;
            yn = 0.3310;
        }
        /********************************************************************************************************/
        ////////WI ASTM E313-1996 1998 2000 2005 2010//////
        if (Degree_mode == 1) {
            if (ILLUMINANT_C == m_nGyType) {
                xn = 0.3101;
                yn = 0.3161;
            } else if (ILLUMINANT_D50 == m_nGyType) {
                xn = 0.3457;
                yn = 0.3585;
            } else if (ILLUMINANT_D65 == m_nGyType) {
                xn = 0.3127;
                yn = 0.3290;
            }
        } else {
            if (ILLUMINANT_C == m_nGyType) {
                xn = 0.3104;
                yn = 0.3191;
            } else if (ILLUMINANT_D50 == m_nGyType) {
                xn = 0.3477;
                yn = 0.3595;
            } else if (ILLUMINANT_D65 == m_nGyType) {
                xn = 0.3138;
                yn = 0.3310;
            }
        }

        /********************************************************************************************************/
        ////////Tint ASTM E313-1996 1998 2000 2005 2010//////
        if (Degree_mode == 1) {
            if (ILLUMINANT_C == m_nGyType) {
                xn = 0.3101;
                yn = 0.3161;
            } else if (ILLUMINANT_D50 == m_nGyType) {
                xn = 0.3457;
                yn = 0.3585;
            } else if (ILLUMINANT_D65 == m_nGyType) {
                xn = 0.3127;
                yn = 0.3290;
            }
            Tx = 1000;
            Ty = 650;
        } else {
            if (ILLUMINANT_C == m_nGyType) {
                xn = 0.3104;
                yn = 0.3191;
            } else if (ILLUMINANT_D50 == m_nGyType) {
                xn = 0.3477;
                yn = 0.3595;
            } else if (ILLUMINANT_D65 == m_nGyType) {
                xn = 0.3138;
                yn = 0.3310;
            }
            Tx = 900;
            Ty = 650;
        }
        /********************************************************************************************************/
        ////////YI ASTM E313-1996 1998 2000 2005 2010//////
        if (Degree_mode == 1) {
            if (ILLUMINANT_C == m_nGyType) {
                Cx = 1.2769;
                Cz = 1.0592;
            } else if (ILLUMINANT_D65 == m_nGyType) {
                Cx = 1.2985;
                Cz = 1.1335;
            }
        } else {
            if (ILLUMINANT_C == m_nGyType) {
                Cx = 1.2871;
                Cz = 1.0781;
            } else if (ILLUMINANT_D65 == m_nGyType) {
                Cx = 1.3013;
                Cz = 1.1498;
            }
        }
        List list2 = new ArrayList();
        list2.add(Tint_CIE);
        list2.add(Tint_E313);
        list2.add(Tint_Ganz);//0-2
        list2.add(YI_E313_1973);
        list2.add(YI_E313_1996_98_10_05_2010);
        list2.add(YI_D1925);//3-5
        list2.add(WI_CIE);
        list2.add(WI_Ganz);
        list2.add(WI_Hunter);
        list2.add(WI_Taube);
        list2.add(WI_Berger);
        list2.add(WI_AATCC);
        list2.add(WI_E313_1973);
        list2.add(WI_E313_1996_98_10_05_2010);//6-13
        return list2;
    }

    public static List<Double> XYZtoMSE_HVC(Double X, Double Y, Double Z) {
        List list = XYZtoYxy(X, Y, Z);
        double X1 = (double) list.get(0);
        double Y1 = (double) list.get(1);
        double Z1 = (double) list.get(2);
        List list1 = YxyToMSE_HVC(X1, Y1, Z1);
        return list1;
    }

    public static List<Double> XYZtoRGB(double X, double Y, double Z, int m_nGyType,int Degree_mode){
        List list = XYZtoCIE_LabCH(X, Y, Z, m_nGyType, Degree_mode);
        double L = (double) list.get(0);
        double a = (double) list.get(1);
        double b = (double) list.get(2);
        List list1 = LabtoRGB(L,a,b);
        return  list1;
    }

    public static List<Double> LabtoRGB(Double L, Double a, Double b) {
        double R, G, B;
        double y = (L + 16.0f) / 116.0f;
        double x = a / 500.0f + y;
        double z = y - b / 200.0f;

        y = y > 6.0f / 29 ? Math.pow(y, 3.0f) : (y - 16.0f / 116) / 7.787f;
        x = x > 6.0f / 29 ? Math.pow(x, 3.0f) : (x - 16.0f / 116) / 7.787f;
        z = z > 6.0f / 29 ? Math.pow(z, 3.0f) : (z - 16.0f / 116) / 7.787f;

        x *= 0.95047f;
        z *= 1.08883f;

        double rgb_r = 3.2406f * x - 1.5372f * y - 0.4986f * z;
        double rgb_g = -0.9689f * x + 1.8758f * y + 0.0415f * z;
        double rgb_b = 0.0557f * x - 0.2040f * y + 1.0570f * z;

        rgb_r = rgb_r > 0.0031308f ? 1.055f * Math.pow(rgb_r, 1 / 2.4f) - 0.055f : 12.92f * rgb_r;
        rgb_g = rgb_g > 0.0031308f ? 1.055f * Math.pow(rgb_g, 1 / 2.4f) - 0.055f : 12.92f * rgb_g;
        rgb_b = rgb_b > 0.0031308f ? 1.055f * Math.pow(rgb_b, 1 / 2.4f) - 0.055f : 12.92f * rgb_b;

        R = Math.max(0, Math.min(255.0f, rgb_r * 255.0f));
        G = Math.max(0, Math.min(255.0f, rgb_g * 255.0f));
        B = Math.max(0, Math.min(255.0f, rgb_b * 255.0f));

        List list = new ArrayList();
        list.add(R);
        list.add(G);
        list.add(B);
        return list;
    }




    public static List<Double> YxytoRGB(Double Y1, Double x, Double y, int m_nGyType, int Degree_mode) {
        List list = YxytoXYZ(Y1, x, y);
        double X = (double) list.get(0);
        double Y = (double) list.get(1);
        double Z = (double) list.get(2);
        List list1 = XYZtoCIE_LabCH(X, Y, Z, m_nGyType, Degree_mode);
        double L = (double) list1.get(0);
        double a = (double) list1.get(1);
        double b = (double) list1.get(2);
        List list2 = LabtoRGB(L, a, b);
        return list2;
    }

    public static List<Double> YxytoCIE_LabCH(Double X, Double Y, Double Z, int m_nGyType, int Degree_mode) {
        List list = YxytoXYZ(X, Y, Z);
        double X1 = (double) list.get(0);
        double Y1 = (double) list.get(1);
        double Z1 = (double) list.get(2);
        List list1 = XYZtoCIE_LabCH(X1, Y1, Z1, m_nGyType, Degree_mode);
        return list1;
    }

    public static List<Double> YxytoXYZ(Double Y1, Double x, Double y) {
        double X, Y, Z;
        Z = Y1 / y * (1 - x - y);
        X = Y1 / y * x;
        Y = Y1;
        List list = new ArrayList();
        list.add(X);
        list.add(Y);
        list.add(Z);
        return list;
    }

    public static List<Double> YxyToMSE_HVC(Double Yxy_Y, Double Yxy_x, Double Yxy_y) {
        double Munsell_H, Munsell_V, Munsell_C;
        double Munsell_HS = 0;
        int i;
        short Starti, Endi, Min_i = 0;
        short Startj, Endj, Min_j = 0;
        double min, temp;
        double Munsell_Htemp = 0, Munsell_Htemp1 = 0;
        double Munsell_Ctemp = 0, Munsell_Ctemp1 = 0;
        double Munsell_HS1 = 0;

        double Tempx, Tempy;
        double Tempx1 = 0, Tempy1;
        double PN;
        if (Yxy_Y <= 0.9) {
            Munsell_V = 0.87445 * Math.pow(Yxy_Y,  0.9967);
        } else {
            Munsell_V = 2.49268 * Math.pow(Yxy_Y,  0.333333) - 1.5614 -
                    0.985 / ((0.1073 * Yxy_Y - 3.084) * (0.1073 * Yxy_Y - 3.084) + 7.54) +
                    0.0133 / Math.pow(Yxy_Y,  2.3) + 0.0084 * Math.sin(4.1 * Math.pow(Yxy_Y,  0.333333) + 1) +
                    (0.0221 / Yxy_Y) * Math.sin(0.39 * (Yxy_Y - 2)) -
                    (0.0037 / (0.44 * Yxy_Y)) * Math.sin(1.28 * (Yxy_Y - 0.53));
        }
        if (Munsell_V < 0.4) {
            Starti = 0;
            Endi = 103 - 1;

            Startj = 103;
            Endj = 271 - 1;
        } else if (Munsell_V < 0.6) {
            Starti = 103;
            Endi = 271 - 1;

            Startj = 271;
            Endj = 473 - 1;
        } else if (Munsell_V < 0.8) {
            Starti = 271;
            Endi = 473 - 1;

            Startj = 473;
            Endj = 719 - 1;
        } else if (Munsell_V < 1) {
            Starti = 473;
            Endi = 719 - 1;

            Startj = 719;
            Endj = 1009 - 1;
        } else if (Munsell_V < 2) {
            Starti = 719;
            Endi = 1009 - 1;

            Startj = 1009;
            Endj = 1381 - 1;
        } else if (Munsell_V < 3) {
            Starti = 1009;
            Endi = 1381 - 1;

            Startj = 1381;
            Endj = 1828 - 1;
        } else if (Munsell_V < 4) {
            Starti = 1381;
            Endi = 1828 - 1;

            Startj = 1828;
            Endj = 2317 - 1;
        } else if (Munsell_V < 5) {
            Starti = 1828;
            Endi = 2317 - 1;

            Startj = 2317;
            Endj = 2840 - 1;
        } else if (Munsell_V < 6) {
            Starti = 2317;
            Endi = 2840 - 1;

            Startj = 2840;
            Endj = 3369 - 1;
        } else if (Munsell_V < 7) {
            Starti = 2840;
            Endi = 3369 - 1;


            Startj = 3369;
            Endj = 3861 - 1;
        } else if (Munsell_V < 8) {
            Starti = 3369;
            Endi = 3861 - 1;

            Startj = 3861;
            Endj = 4311 - 1;
        } else if (Munsell_V < 9) {
            Starti = 3861;
            Endi = 4311 - 1;

            Startj = 4311;
            Endj = 4678 - 1;
        } else if (Munsell_V < 10) {
            Starti = 4311;
            Endi = 4678 - 1;

            Startj = 4678;
            Endj = 4927 - 1;
        } else {
            Starti = 4678;
            Endi = 4927 - 1;

            Startj = 0;
            Endj = 0;
        }

        min = 100;
        for (i = Starti; i < Endi; i++) {
            temp = Math.sqrt((Contast4.MSE_x[i] - Yxy_x) * (Contast4.MSE_x[i] - Yxy_x) + (Contast5.MSE_y[i] - Yxy_y) * (Contast5.MSE_y[i] - Yxy_y));
            if (temp < min) {
                min = temp;    //找到与输入x、y最接近的
                Min_i = (short) i;
                Munsell_Htemp = MSE_H[i] / 10;    // 相对应H值
                Munsell_HS = MSE_H_S[i];        // 相对应H值R-1 YR-2 Y-3 GY-4 G-5 BG-6 B-7 PB-8 P-9 RP-10
                Munsell_Ctemp = MSE_C[i];        // 相对应C值
            }
        }
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
        ////////////////////求C值/////////
        Tempx = Contast4.MSE_x[Min_i];
        Tempy = Contast5.MSE_y[Min_i];
        Tempx1 = 0.0;
        Tempy1 = 0.0;
        min = 100;
        PN = 1;
        if ((MSE_H_S[Min_i + 1] == Munsell_HS) && (MSE_H[Min_i + 1] == (Munsell_Htemp * 10)))    // 相邻下一个H值没有发生变化
        {
            temp = Math.sqrt((Contast4.MSE_x[Min_i + 1] - Yxy_x) * (Contast4.MSE_x[Min_i + 1] - Yxy_x) + (Contast5.MSE_y[Min_i + 1] - Yxy_y) * (Contast5.MSE_y[Min_i + 1] - Yxy_y));
            min = temp;
            Tempx1 = Contast4.MSE_x[Min_i + 1];
            Tempy1 = Contast5.MSE_y[Min_i + 1];
            PN = 1;
        }
        if ((MSE_H_S[Min_i - 1] == Munsell_HS) && (MSE_H[Min_i - 1] == (Munsell_Htemp * 10)))    // 相邻上一个H值没有发生变化
        {
            temp = Math.sqrt((Contast4.MSE_x[Min_i - 1] - Yxy_x) * (Contast4.MSE_x[Min_i - 1] - Yxy_x) + (Contast5.MSE_y[Min_i - 1] - Yxy_y) * (Contast5.MSE_y[Min_i - 1] - Yxy_y));
            if (min > temp) {
                Tempx1 = Contast4.MSE_x[Min_i - 1];
                Tempy1 = Contast5.MSE_y[Min_i - 1];
                PN = -1;
            }
        }
        if (Tempx1 == 0.0) {
            if (Munsell_Ctemp == 2) {
                Tempx1 = 0.33333;
                Tempy1 = 0.33333;
                PN = -1;
            }
        }
        temp = Math.sqrt((Tempx - Tempx1) * (Tempx - Tempx1) + (Tempy - Tempy1) * (Tempy - Tempy1));
        min = Math.sqrt((Tempx - Yxy_x) * (Tempx - Yxy_x) + (Tempy - Yxy_y) * (Tempy - Yxy_y));
        Munsell_Ctemp = Munsell_Ctemp + (min / temp) * 2 * PN;
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
        ////////////////////求H值/////////
        Tempx = Contast4.MSE_x[Min_i];
        Tempy = Contast5.MSE_y[Min_i];
        Tempx1 = 0.0;
        Tempy1 = 0.0;
        min = 100;
        PN = 1;
        for (i = Starti; i < Endi; i++) {
            if ((Munsell_Ctemp == MSE_C[i]) &&        // C值相等
                    (Munsell_HS == MSE_H_S[i]))        // H值尾缀相等 R,YR,Y.....
            {
                if (MSE_H[i] == (Munsell_Htemp * 10 - 25))            // H值为上一个H值
                {
                    temp = Math.sqrt((Contast4.MSE_x[i] - Yxy_x) * (Contast4.MSE_x[i] - Yxy_x) + (Contast5.MSE_y[i] - Yxy_y) * (Contast5.MSE_y[i] - Yxy_y));
                    if (min > temp) {
                        min = temp;
                        Tempx1 = Contast4.MSE_x[i];
                        Tempy1 = Contast5.MSE_y[i];
                        PN = -1;
                    }
                } else if (MSE_H[i] == (Munsell_Htemp * 10 + 25))        // H值为下一个H值
                {
                    temp = Math.sqrt((Contast4.MSE_x[i] - Yxy_x) * (Contast4.MSE_x[i] - Yxy_x) + (Contast5.MSE_y[i] - Yxy_y) * (Contast5.MSE_y[i] - Yxy_y));
                    if (min > temp) {
                        min = temp;
                        Tempx1 = Contast4.MSE_x[i];
                        Tempy1 = Contast5.MSE_y[i];
                        PN = 1;
                    }
                }
            }
        }
        temp = Math.sqrt((Tempx - Tempx1) * (Tempx - Tempx1) + (Tempy - Tempy1) * (Tempy - Tempy1));
        min = Math.sqrt((Tempx - Yxy_x) * (Tempx - Yxy_x) + (Tempy - Yxy_y) * (Tempy - Yxy_y));
        Munsell_Htemp = Munsell_Htemp + (min / temp) * 2.5 * PN;

        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

        if (Munsell_V < 10) {
            min = 100;
            for (i = Startj; i < Endj; i++) {
                temp = Math.sqrt((Contast4.MSE_x[i] - Yxy_x) * (Contast4.MSE_x[i] - Yxy_x) + (Contast5.MSE_y[i] - Yxy_y) * (Contast5.MSE_y[i] - Yxy_y));
                if (temp < min && Munsell_HS == MSE_H_S[i]) {
                    min = temp;    //找到与输入x、y最接近的
                    Min_j = (short) i;
                    Munsell_Htemp1 = MSE_H[i] / 10;    // 相对应H值
                    Munsell_HS1 = MSE_H_S[i];        // 相对应H值R-1 YR-2 Y-3 GY-4 G-5 BG-6 B-7 PB-8 P-9 RP-10
                    Munsell_Ctemp1 = MSE_C[i];        // 相对应C值
                }
            }
            //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
            Min_i = Min_j;
            ////////////////////求C值/////////
            Tempx = Contast4.MSE_x[Min_i];
            Tempy = Contast5.MSE_y[Min_i];
            Tempx1 = 0.0;
            Tempy1 = 0.0;
            min = 100;
            PN = 1;
            if ((MSE_H_S[Min_i + 1] == Munsell_HS1) && (MSE_H[Min_i + 1] == (Munsell_Htemp1 * 10)))    // 相邻下一个H值没有发生变化
            {
                temp = Math.sqrt((Contast4.MSE_x[Min_i + 1] - Yxy_x) * (Contast4.MSE_x[Min_i + 1] - Yxy_x) + (Contast5.MSE_y[Min_i + 1] - Yxy_y) * (Contast5.MSE_y[Min_i + 1] - Yxy_y));
                min = temp;
                Tempx1 = Contast4.MSE_x[Min_i + 1];
                Tempy1 = Contast5.MSE_y[Min_i + 1];
                PN = 1;
            }
            if ((MSE_H_S[Min_i - 1] == Munsell_HS1) && (MSE_H[Min_i - 1] == (Munsell_Htemp1 * 10)))    // 相邻上一个H值没有发生变化
            {
                temp = Math.sqrt((Contast4.MSE_x[Min_i - 1] - Yxy_x) * (Contast4.MSE_x[Min_i - 1] - Yxy_x) + (Contast5.MSE_y[Min_i - 1] - Yxy_y) * (Contast5.MSE_y[Min_i - 1] - Yxy_y));
                if (min > temp) {
                    Tempx1 = Contast4.MSE_x[Min_i - 1];
                    Tempy1 = Contast5.MSE_y[Min_i - 1];
                    PN = -1;
                }
            }
            if (Tempx1 == 0.0) {
                if (Munsell_Ctemp1 == 2) {
                    Tempx1 = 0.33333;
                    Tempy1 = 0.33333;
                    PN = -1;
                }
            }
            temp = Math.sqrt((Tempx - Tempx1) * (Tempx - Tempx1) + (Tempy - Tempy1) * (Tempy - Tempy1));
            min = Math.sqrt((Tempx - Yxy_x) * (Tempx - Yxy_x) + (Tempy - Yxy_y) * (Tempy - Yxy_y));
            Munsell_Ctemp1 = Munsell_Ctemp1 + (min / temp) * 2 * PN;
            //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
            ////////////////////求H值/////////
            Tempx = Contast4.MSE_x[Min_i];
            Tempy = Contast5.MSE_y[Min_i];
            Tempx1 = 0.0;
            Tempy1 = 0.0;
            min = 100;
            PN = 1;
            for (i = Startj; i < Endj; i++) {
                if ((Munsell_Ctemp1 == MSE_C[i]) &&        // C值相等
                        (Munsell_HS1 == MSE_H_S[i]))        // H值尾缀相等 R,YR,Y.....
                {
                    if (MSE_H[i] == (Munsell_Htemp1 * 10 - 25))            // H值为上一个H值
                    {
                        temp = Math.sqrt((Contast4.MSE_x[i] - Yxy_x) * (Contast4.MSE_x[i] - Yxy_x) + (Contast5.MSE_y[i] - Yxy_y) * (Contast5.MSE_y[i] - Yxy_y));
                        if (min > temp) {
                            min = temp;
                            Tempx1 = Contast4.MSE_x[i];
                            Tempy1 = Contast5.MSE_y[i];
                            PN = -1;
                        }
                    } else if (MSE_H[i] == (Munsell_Htemp1 * 10 + 25))        // H值为下一个H值
                    {
                        temp = Math.sqrt((Contast4.MSE_x[i] - Yxy_x) * (Contast4.MSE_x[i] - Yxy_x) + (Contast5.MSE_y[i] - Yxy_y) * (Contast5.MSE_y[i] - Yxy_y));
                        if (min > temp) {
                            min = temp;
                            Tempx1 = Contast4.MSE_x[i];
                            Tempy1 = Contast5.MSE_y[i];
                            PN = 1;
                        }
                    }
                }
            }
            temp = Math.sqrt((Tempx - Tempx1) * (Tempx - Tempx1) + (Tempy - Tempy1) * (Tempy - Tempy1));
            min = Math.sqrt((Tempx - Yxy_x) * (Tempx - Yxy_x) + (Tempy - Yxy_y) * (Tempy - Yxy_y));
            Munsell_Htemp1 = Munsell_Htemp1 + (min / temp) * 2.5 * PN;

        }
        min = (int) Munsell_V;
        temp = Munsell_V - min;
        if (Munsell_V < 10) {
            Munsell_C = Munsell_Ctemp + (Munsell_Ctemp1 - Munsell_Ctemp) * temp;
            Munsell_H = Munsell_Htemp + (Munsell_Htemp1 - Munsell_Htemp) * temp;
        } else {
            Munsell_C = Munsell_Ctemp;
            Munsell_H = Munsell_Htemp;
        }
        List list = new ArrayList();
        list.add(Munsell_H);
        list.add(Munsell_V);
        list.add(Munsell_C);
        return list;
    }

    public static List<Double> CIELabtoXYZ(Double L, Double a, Double b, int m_nGyType, int Degree_mode) {
        double X, Y, Z;
        double var_X, var_Y, var_Z;
        double temX = 0, temY = 0, temZ = 0;
        List list1 = OrDer.set_Illuminant_Observer(m_nGyType, Degree_mode);
        temX = (double) list1.get(0);
        temY = (double) list1.get(1);
        temZ = (double) list1.get(2);
        var_Y = (L + 16) / 116;
        var_X = a / 500 + var_Y;
        var_Z = var_Y - b / 200;
        if (var_Y > 6.0 / 29.0)
            var_Y = Math.pow((float) var_Y, 3);
        else
            var_Y = (var_Y - 16.0 / 116.0) * 3.0 * Math.pow((float) (6.0 / 29.0), 2);
        if (var_X > 6.0 / 29.0)
            var_X = Math.pow((float) var_X, 3);
        else
            var_X = (var_X - 16.0 / 116.0) * 3.0 * Math.pow((float) (6.0 / 29.0), 2);
        if (var_Z > 6.0 / 29.0)
            var_Z = Math.pow((float) var_Z, 3);
        else
            var_Z = (var_Z - 16.0 / 116.0) * 3.0 * Math.pow((float) (6.0 / 29.0), 2);
        X = temX * var_X; // 94.811
        Y = temY * var_Y;    //100.000
        Z = temZ * var_Z; //107.304
        List list = new ArrayList();
        list.add(X);
        list.add(Y);
        list.add(Z);
        return list;
    }

    public static double getASTM_Color(Double X, Double Y, Double Z) {
        double DX, DY, DZ;
        double SunD;
        DX = -Math.log10(X / 98.072);
        DY = -Math.log10(Y / 100.00);
        DZ = -Math.log10(Z / 118.225);
        SunD = DX + DY + DZ;
        double ASTM_Color =  (0.25 + 0.8695 * SunD);        //ASTM D6025
        if (ASTM_Color < 0) {
            ASTM_Color = 0;
        }
        if (ASTM_Color > 8) {
            ASTM_Color = 8;
        }
        return ASTM_Color;
    }

    public static double getSaybolt(Double L, Double a, Double b) {
        double DL, Da, Db, DEab;
        // 	DL = 96.82-L;		// 十二烷标准 X=90 Y=92 Z=105 计算到L*a*b* L*=96.82，a*=-0.35 b*=2.2，标准数据是仪器测量的结果
        // 	Da = -0.35-a;
        // 	Db = 2.2-b;
        DL = 100.32 - L;        // 十二烷标准 X=98.85 Y=100.83 Z=118.98 计算          到L*a*b* L*=100.32，a*=-0.01 b*=0.07，标准数据是仪器测量的结果
        Da = -0.01 - a;
        Db = 0.07 - b;
        DEab = (float) Math.sqrt(DL * DL + Da * Da + Db * Db);
        double Saybolt = 51.1 + 44.5 / (Math.log10(DEab) - 2.55);    //ASTM D6025
        if (Saybolt > 30) {
            Saybolt = 30;
        }
        if (Saybolt < -16) {
            Saybolt = -16;
        }
        return Saybolt;
    }

    public static double ref2ISOLight(double[] data_SCI) {
        int i;
        double data;
        double Fnm[] = {1, 6.7, 18.2, 34.5, 57.6, 82.5, 100.0, 88.7, 53.1, 20.3, 5.6, 0.3};
        double weights[] = {0.213, 1.430, 3.885, 7.364, 12.295, 17.609, 21.345, 18.933, 11.334, 4.333, 1.195, 0.064};
        data = 0;
        for (i = 0; i < 12; i++) {
            data += data_SCI[i] * weights[i];
        }
        return data * 0.01;
    }

    public static List<Double> comuteXYZ(double[] data_SCI, int m_nGyType, int Degree_mode) {
        int i;
        double x, y, z;
        double[] ligh_temp = new double[41];
        double xx[] = new double[41];
        double yy[] = new double[41];
        double zz[] = new double[41];
        double k = 0;
        double XYZ[] = new double[5];

        switch (Degree_mode) {
            case 1:           // 2°角标准观察者
                for (i = 0; i < count; i++) {
                    xx[i] = Contast2.xx_2[i];
                    yy[i] = Contast2.yy_2[i];
                    zz[i] = Contast2.zz_2[i];
                }
                break;

            case 0:           // 10°角标准观察者
                for (i = 0; i < count; i++) {
                    xx[i] = Contast2.xx_10[i];
                    yy[i] = Contast2.yy_10[i];
                    zz[i] = Contast2.zz_10[i];
                }
                break;
        }
        ligh_temp = Countligh_temp.countligh_temp(m_nGyType);
        //Contast1.ILLUMINANT_D65  0
        k = CountK.countK(m_nGyType, Degree_mode);
        XYZ[0] = XYZ[1] = XYZ[2] = 0;
        for (i = 0; i < 31; i++) {
            XYZ[0] += xx[i] * (data_SCI[i] * 0.01) * ligh_temp[i];
            XYZ[1] += yy[i] * (data_SCI[i] * 0.01) * ligh_temp[i];
            XYZ[2] += zz[i] * (data_SCI[i] * 0.01) * ligh_temp[i];
        }
        x = XYZ[0] * k;
        y = XYZ[1] * k;
        z = XYZ[2] * k;
        List list = new ArrayList();
        list.add(x);
        list.add(y);
        list.add(z);
        return list;
    }

    public static List<Double> comuteXYZ(float[] data_SCI, int m_nGyType, int Degree_mode) {
        int i;
        double x, y, z;
        double[] ligh_temp = new double[41];
        double xx[] = new double[41];
        double yy[] = new double[41];
        double zz[] = new double[41];
        double k = 0;
        double XYZ[] = new double[5];

        switch (Degree_mode) {
            case 1:           // 2°角标准观察者
                for (i = 0; i < count; i++) {
                    xx[i] = Contast2.xx_2[i];
                    yy[i] = Contast2.yy_2[i];
                    zz[i] = Contast2.zz_2[i];
                }
                break;

            case 0:           // 10°角标准观察者
                for (i = 0; i < count; i++) {
                    xx[i] = Contast2.xx_10[i];
                    yy[i] = Contast2.yy_10[i];
                    zz[i] = Contast2.zz_10[i];
                }
                break;
        }
        ligh_temp = Countligh_temp.countligh_temp(m_nGyType);
        //Contast1.ILLUMINANT_D65  0
        k = CountK.countK(m_nGyType, Degree_mode);
        XYZ[0] = XYZ[1] = XYZ[2] = 0;
        for (i = 0; i < 31; i++) {
            XYZ[0] += xx[i] * (data_SCI[i] * 0.01) * ligh_temp[i];
            XYZ[1] += yy[i] * (data_SCI[i] * 0.01) * ligh_temp[i];
            XYZ[2] += zz[i] * (data_SCI[i] * 0.01) * ligh_temp[i];
        }
        x = XYZ[0] * k;
        y = XYZ[1] * k;
        z = XYZ[2] * k;
        List list = new ArrayList();
        list.add(x);
        list.add(y);
        list.add(z);
        return list;
    }

    public static List<Double> Density(double[] data_SCI) {
        //色密度测试参数 CMYK////
        double DENSITY_V;    //K
        double DENSITY_A_B = 0;
        double DENSITY_A_G = 0;
        double DENSITY_A_R = 0;
        double DENSITY_T_B = 0;
        double DENSITY_T_G = 0;
        double DENSITY_T_R = 0;
        double DENSITY_M_B;
        double DENSITY_M_G;
        double DENSITY_M_R;
        double DENSITY_E_B;
        double DENSITY_E_G;
        double DENSITY_E_R;
        double A_SrVr_B = 0, A_SrVr_G = 0, A_SrVr_R = 0;
        double T_SrVr_B = 0, T_SrVr_G = 0, T_SrVr_R = 0;

        double A_SrVr_B_nm = 0, A_SrVr_G_nm = 0, A_SrVr_R_nm = 0;
        double T_SrVr_B_nm = 0, T_SrVr_G_nm = 0, T_SrVr_R_nm = 0;

        double SrVr_B = 0, SrVr_G = 0, SrVr_R = 0;
        double SrVr_B_nm = 0, SrVr_G_nm = 0, SrVr_R_nm = 0;

        int i;
        for (i = 0; i < 31; i++) {
            SrVr_B += Math.pow(10, Density_V_Coefficient[i]) * (400 + i * 10);
            SrVr_B_nm += Math.pow(10, Density_V_Coefficient[i]) * (400 + i * 10) * data_SCI[i] * 0.01;
        }
        DENSITY_V = Math.log10(SrVr_B / SrVr_B_nm);
        /////////////////A、T密度(CMY值)//////////////////////
        for (i = 0; i < 31; i++) {
            A_SrVr_B += Math.pow(10, DensityA_B_Coefficient[i]) * (400 + i * 10);
            A_SrVr_G += Math.pow(10, DensityA_G_Coefficient[i]) * (400 + i * 10);
            A_SrVr_R += Math.pow(10, DensityA_R_Coefficient[i]) * (400 + i * 10);
            T_SrVr_B += Math.pow(10, DensityT_B_Coefficient[i]) * (400 + i * 10);
            T_SrVr_G += Math.pow(10, DensityT_G_Coefficient[i]) * (400 + i * 10);
            T_SrVr_R += Math.pow(10, DensityT_R_Coefficient[i]) * (400 + i * 10);
            A_SrVr_B_nm += Math.pow(10, DensityA_B_Coefficient[i]) * (400 + i * 10) * data_SCI[i] * 0.01;
            A_SrVr_G_nm += Math.pow(10, DensityA_G_Coefficient[i]) * (400 + i * 10) * data_SCI[i] * 0.01;
            A_SrVr_R_nm += Math.pow(10, DensityA_R_Coefficient[i]) * (400 + i * 10) * data_SCI[i] * 0.01;
            T_SrVr_B_nm += Math.pow(10, DensityT_B_Coefficient[i]) * (400 + i * 10) * data_SCI[i] * 0.01;
            T_SrVr_G_nm += Math.pow(10, DensityT_G_Coefficient[i]) * (400 + i * 10) * data_SCI[i] * 0.01;
            T_SrVr_R_nm += Math.pow(10, DensityT_R_Coefficient[i]) * (400 + i * 10) * data_SCI[i] * 0.01;
        }
        DENSITY_A_B = Math.log10(A_SrVr_B / A_SrVr_B_nm);
        DENSITY_A_G = Math.log10(A_SrVr_G / A_SrVr_G_nm);
        DENSITY_A_R = Math.log10(A_SrVr_R / A_SrVr_R_nm);
        DENSITY_T_B = Math.log10(T_SrVr_B / T_SrVr_B_nm);
        DENSITY_T_G = Math.log10(T_SrVr_G / T_SrVr_G_nm);
        DENSITY_T_R = Math.log10(T_SrVr_R / T_SrVr_R_nm);
        /////////////////M密度(CMY值)//////////////////////
        SrVr_B = SrVr_G = SrVr_R = 0;
        SrVr_B_nm = SrVr_G_nm = SrVr_R_nm = 0;
        for (i = 0; i < 31; i++) {
            SrVr_B += Math.pow(10, DensityM_B_Coefficient[i]) * (400 + i * 10);
            SrVr_G += Math.pow(10, DensityM_G_Coefficient[i]) * (400 + i * 10);
            SrVr_R += Math.pow(10, DensityM_R_Coefficient[i]) * (400 + i * 10);
            SrVr_B_nm += Math.pow(10, DensityM_B_Coefficient[i]) * (400 + i * 10) * data_SCI[i] * 0.01;
            SrVr_G_nm += Math.pow(10, DensityM_G_Coefficient[i]) * (400 + i * 10) * data_SCI[i] * 0.01;
            SrVr_R_nm += Math.pow(10, DensityM_R_Coefficient[i]) * (400 + i * 10) * data_SCI[i] * 0.01;
        }
        DENSITY_M_B = Math.log10(SrVr_B / SrVr_B_nm);
        DENSITY_M_G = Math.log10(SrVr_G / SrVr_G_nm);
        DENSITY_M_R = Math.log10(SrVr_R / SrVr_R_nm);
        /////////////////E密度(CMY值)//////////////////////
        SrVr_B = SrVr_G = SrVr_R = 0;
        SrVr_B_nm = SrVr_G_nm = SrVr_R_nm = 0;
        for (i = 0; i < 31; i++) {
            SrVr_B += Math.pow(10, DensityE_B_Coefficient[i]) * (400 + i * 10);
            SrVr_G += Math.pow(10, DensityE_G_Coefficient[i]) * (400 + i * 10);
            SrVr_R += Math.pow(10, DensityE_R_Coefficient[i]) * (400 + i * 10);
            SrVr_B_nm += Math.pow(10, DensityE_B_Coefficient[i]) * (400 + i * 10) * data_SCI[i] * 0.01;
            SrVr_G_nm += Math.pow(10, DensityE_G_Coefficient[i]) * (400 + i * 10) * data_SCI[i] * 0.01;
            SrVr_R_nm += Math.pow(10, DensityE_R_Coefficient[i]) * (400 + i * 10) * data_SCI[i] * 0.01;
        }
        DENSITY_E_B = Math.log10(SrVr_B / SrVr_B_nm);
        DENSITY_E_G = Math.log10(SrVr_G / SrVr_G_nm);
        DENSITY_E_R = Math.log10(SrVr_R / SrVr_R_nm);
        List list = new ArrayList();
        list.add(DENSITY_A_B);//0
        list.add(DENSITY_A_G);//1
        list.add(DENSITY_A_R);//2
        list.add(DENSITY_T_B);//3
        list.add(DENSITY_T_G);//4
        list.add(DENSITY_T_R);//5
        list.add(DENSITY_M_B);//6
        list.add(DENSITY_M_G);//7
        list.add(DENSITY_M_R);//8
        list.add(DENSITY_E_B);//9
        list.add(DENSITY_E_G);//10
        list.add(DENSITY_E_R);//11
        return list;
    }





    public static List<Double> SCItoCIE_LabCH(double[] data_SCI, int m_nGyType, int Degree_mode) {
        List list = comuteXYZ(data_SCI, m_nGyType, Degree_mode);
        double X = (double) list.get(0);
        double Y = (double) list.get(1);
        double Z = (double) list.get(2);
        List list2 = XYZtoCIE_LabCH(X, Y, Z, m_nGyType, Degree_mode);
        return list2;
    }

    public static List<Double> SCItoLuv(double[] data_SCI, int m_nGyType, int Degree_mode) {
        List list = comuteXYZ(data_SCI, m_nGyType, Degree_mode);
        double X = (double) list.get(0);
        double Y = (double) list.get(1);
        double Z = (double) list.get(2);
        List list1 = XYZtoLuv(X, Y, Z, m_nGyType, Degree_mode);
        return list1;
    }

    public static List<Double> SCItoHunterLab(double[] data_SCI, int m_nGyType, int Degree_mode) {
        List list = comuteXYZ(data_SCI, m_nGyType, Degree_mode);
        double X = (double) list.get(0);
        double Y = (double) list.get(1);
        double Z = (double) list.get(2);
        List list1 = XYZtoHunterLab(X, Y, Z, m_nGyType, Degree_mode);
        return list1;
    }

    public static List<Double> SCItoYxy(double[] data_SCI, int m_nGyType, int Degree_mode) {
        List list = comuteXYZ(data_SCI, m_nGyType, Degree_mode);
        double X = (double) list.get(0);
        double Y = (double) list.get(1);
        double Z = (double) list.get(2);
        List list1 = XYZtoYxy(X, Y, Z);
        return list1;
    }

    public static List<Double> SCItoMSE_HVC(double[] data_SCI, int m_nGyType, int Degree_mode) {
        List list = comuteXYZ(data_SCI, m_nGyType, Degree_mode);
        double X = (double) list.get(0);
        double Y = (double) list.get(1);
        double Z = (double) list.get(2);
        List list1 = XYZtoYxy(X, Y, Z);
        double X1 = (double) list1.get(0);
        double Y1 = (double) list1.get(1);
        double Z1 = (double) list1.get(2);
        List list2 = YxyToMSE_HVC(X1, Y1, Z1);
        return list2;
    }

    public static List<Double> SCItoRGB(double[] data_SCI, int m_nGyType, int Degree_mode) {
        List list = SCItoCIE_LabCH(data_SCI, m_nGyType, Degree_mode);
        double X1 = (double) list.get(0);
        double Y1 = (double) list.get(1);
        double Z1 = (double) list.get(2);
        List list1 = LabtoRGB(X1, Y1, Z1);
        return list1;
    }

    public static double SCItoSaybolt(double[] data_SCI, int m_nGyType, int Degree_mode) {
        List list = comuteXYZ(data_SCI, m_nGyType, Degree_mode);
        double X = (double) list.get(0);
        double Y = (double) list.get(1);
        double Z = (double) list.get(2);
        List list1 = XYZtoCIE_LabCH(X, Y, Z, m_nGyType, Degree_mode);
        double L = (double) list1.get(0);
        double a = (double) list1.get(1);
        double b = (double) list1.get(2);
        double saybolt = getSaybolt(L, a, b);
        return saybolt;
    }

    public static double SCItoASTM_Color(double[] data_SCI, int m_nGyType, int Degree_mode) {
        List list = comuteXYZ(data_SCI, m_nGyType, Degree_mode);
        double X = (double) list.get(0);
        double Y = (double) list.get(1);
        double Z = (double) list.get(2);
        double ASTM_Color = getASTM_Color(X, Y, Z);
        return ASTM_Color;
    }

    public static List<Double> SCItoWI_TINT_CIE(double[] data_SCI, int m_nGyType, int Degree_mode) {
        List list = comuteXYZ(data_SCI, m_nGyType, Degree_mode);
        double X = (double) list.get(0);
        double Y = (double) list.get(1);
        double Z = (double) list.get(2);
        List list2 = XYZtoWI_TINT_CIE(X, Y, Z, m_nGyType, Degree_mode);
        return list2;
    }


    public static List<Double> CIELabtoMSE_HVC(Double X, Double Y, Double Z, int m_nGyType, int Degree_mode) {
        List list = CIELabtoXYZ(X, Y, Z, m_nGyType, Degree_mode);
        double X1 = (double) list.get(0);
        double Y1 = (double) list.get(1);
        double Z1 = (double) list.get(2);
        List list1 = XYZtoMSE_HVC(X1, Y1, Z1);
        return list1;
    }

    public static List<Double> CIELabtoCIE_LabCH(Double X, Double Y, Double Z, int m_nGyType, int Degree_mode) {
        List list = CIELabtoXYZ(X, Y, Z, m_nGyType, Degree_mode);
        double X1 = (double) list.get(0);
        double Y1 = (double) list.get(1);
        double Z1 = (double) list.get(2);
        List list1 = XYZtoCIE_LabCH(X1, Y1, Z1, m_nGyType, Degree_mode);
        return list1;
    }

}
