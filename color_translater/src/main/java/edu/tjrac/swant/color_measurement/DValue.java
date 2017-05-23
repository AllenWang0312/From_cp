package edu.tjrac.swant.color_measurement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cimcenter on 2017/1/12.
 */

public class DValue {
    static double d_H;
    //大写的为标样数据  小写的为试样数据
    //返回参数为资料上从上到下一共10个
    //沽色、变色 1.5位“2-3” 以此类推
    //顺序 LAB lab CH ch UV uv Hunter_L,A,B Hunter_l,a,b SCI(标样) SCI（试样）
    public static List<Double> getDValue(double L,double A,
                                         double B,double l,
                                         double a,double b,
                                         double C,double H,
                                         double c,double h,
                                         double U,double V,
                                         double u,double v,
                                         double Hunter_L,double Hunter_A,double Hunter_B,
                                         double Hunter_l,double Hunter_a,double Hunter_b,
                                       List<Double> p, List<Double> p1){
        double dL = l - L;
        double da = a - A;
        double db = b - B;
        double dE = Math.sqrt(dL * dL + da * da + db * db);
        double dC = c - C;
        double dH = h - H;
        if (dH < 0) {
            d_H = -Math.sqrt(dE * dE - dC * dC - dL * dL);
        } else {
            d_H = Math.sqrt(dE * dE - dC * dC - dL * dL);
        }
        double dE94 = Math.sqrt((dL / (1 * 1)) * (dL / (1 * 1)) + (dC / (1 * (1 + 0.045 * c))) * (dC / (1 * (1 + 0.045 * c))) + (d_H / (1 * (1 + 0.015 * c))) * (d_H / (1 * (1 + 0.015 * c))));

        //----------------------------------------
        double dv = v-V;
        double du = u-U;
        double dEuv = Math.sqrt(dL*dL +dv*dv +du*du);
        double Hunter_dL = Hunter_l-Hunter_L;	//hunterLab
        double Hunter_da = Hunter_a-Hunter_A;
        double Hunter_db = Hunter_b-Hunter_B;
        double Hunter_Eab = Math.sqrt(Hunter_dL*Hunter_dL+Hunter_da*Hunter_da+Hunter_db*Hunter_db);
        //--------------------------------------------

        double CIE00_G = 0.5 * (1 - Math.sqrt(Math.pow((C + c) / 2, (7.0)) / (Math.pow((C + c) / 2, (7.0)) + Math.pow((25.0), (7.0)))));
        double dE00_b_l = l;
        double dE00_b_a = (1 + CIE00_G) * a;
        double dE00_b_b = b;
        double dE00_b_c = Math.sqrt(dE00_b_a * dE00_b_a + dE00_b_b * dE00_b_b);
        double dE00_b_h;

        if ((dE00_b_a == 0) && (dE00_b_b > 0))
            dE00_b_h = 90;
        else if ((dE00_b_a == 0) && (dE00_b_b < 0))
            dE00_b_h = 270;
        else if ((dE00_b_a >= 0) && (dE00_b_b == 0))
            dE00_b_h = 0;
        else if ((dE00_b_a < 0) && (dE00_b_b == 0))
            dE00_b_h = 180;
        else {
            dE00_b_h = Math.atan(dE00_b_b / dE00_b_a);
            dE00_b_h = dE00_b_h * 180 / 3.141592654;    // ?????
            if ((dE00_b_a > 0) && (dE00_b_b > 0))
                dE00_b_h = dE00_b_h;
            else if (dE00_b_a < 0)
                dE00_b_h = 180 + dE00_b_h;
            else
                dE00_b_h = 360 + dE00_b_h;
        }

        double dE00_s_l;
        double dE00_s_a;
        double dE00_s_b;
        double dE00_s_c;
        double dE00_s_h;
        dE00_s_l = L;
        dE00_s_a = (1 + CIE00_G) * a;
        dE00_s_b = b;
        dE00_s_c = Math.sqrt(dE00_s_a * dE00_s_a + dE00_s_b * dE00_s_b);

        if ((dE00_s_a == 0) && (dE00_s_b > 0))
            dE00_s_h = 90;
        else if ((dE00_s_a == 0) && (dE00_s_b < 0))
            dE00_s_h = 270;
        else if ((dE00_s_a >= 0) && (dE00_s_b == 0))
            dE00_s_h = 0;
        else if ((dE00_s_a < 0) && (dE00_s_b == 0))
            dE00_s_h = 180;
        else {
            dE00_s_h = Math.atan(dE00_s_b / dE00_s_a);
            dE00_s_h = dE00_s_h * 180 / 3.141592654;    //
            if ((dE00_s_a > 0) && (dE00_s_b > 0))
                dE00_s_h = dE00_s_h;
            else if (dE00_b_a < 0)
                dE00_s_h = 180 + dE00_s_h;
            else
                dE00_s_h = 360 + dE00_s_h;
        }


        double dCIE00_L = dE00_s_l - dE00_b_l;
        double dCIE00_C = dE00_s_c - dE00_b_c;
        double dCIE00_h = dE00_s_h - dE00_b_h;
        double dCIE00_H = 2 * Math.sqrt(dE00_s_c * dE00_b_c) * Math.sin((dCIE00_h / 2.0) * (3.14159265359 / 180));

        if (Math.abs(dE00_b_h - dE00_s_h) > 180){   // 象限差
            if (dE00_b_h > dE00_s_h) {
                dE00_b_h = dE00_b_h - 360;
            } else {
                dE00_s_h = dE00_s_h - 360;
            }
        }
        double CIE00_SL = 1 + (0.015 * ((dE00_b_l + dE00_s_l) / 2 - 50) * ((dE00_b_l + dE00_s_l) / 2 - 50) / Math.sqrt(20 + ((dE00_b_l + dE00_s_l) / 2 - 50) * ((dE00_b_l + dE00_s_l) / 2 - 50)));
        double CIE00_SC = 1 + 0.045 * ((dE00_b_c + dE00_s_c) / 2);
        double CIE00_T = 1 - 0.17 * Math.cos((3.14159265359 / 180) * ((dE00_b_h + dE00_s_h) / 2 - 30)) + 0.24 * Math.cos((3.14159265359 / 180) * (2 * (dE00_b_h + dE00_s_h) / 2)) + 0.32 * Math.cos((3.14159265359 / 180) * (3 * (dE00_b_h + dE00_s_h) / 2 + 6)) - 0.2 * Math.cos((3.14159265359 / 180) * (4 * (dE00_b_h + dE00_s_h) / 2 - 63));
        double CIE00_RC = 2 * Math.sqrt(Math.pow((C + c) / 2, (7.0)) / (Math.pow((C + c) / 2, (7.0)) + Math.pow((25.0), (7.0))));
        double CIE00_do = 30 * Math.exp(-1 * Math.pow(((dE00_b_h + dE00_s_h) / 2 - 275) / 25, 2));
        double CIE00_RT = -1 * Math.sin(CIE00_do * 2 * 3.14159265359 / 180) * CIE00_RC;
        double CIE00_SH = 1 + 0.015 * ((dE00_b_c + dE00_s_c) / 2) * CIE00_T;
        double dCIE00 = Math.sqrt(Math.pow(dCIE00_L / CIE00_SL, 2) + Math.pow(dCIE00_C / CIE00_SC, 2) + Math.pow(dCIE00_H / CIE00_SH, 2) + CIE00_RT * (dCIE00_C / CIE00_SC) * (dCIE00_H / CIE00_SH));

        //---------------------------------DcMc L:C-----------------------------------

        double CMC_SL = 0;
        if (l >= 16) {
            CMC_SL = 0.040975 * l / (1 + 0.01765 * l);
        } else {
            CMC_SL = 0.511f;
        }
        double CMC_SC = 0;
        CMC_SC = 0.0638 * c / (1 + 0.0131 * c) + 0.638;
        double CMC_F = 0;
        CMC_F = Math.sqrt(Math.pow(c, 4) / (Math.pow(c, 4) + 1900));

        double CMC_T = 0;
        if (h >= 164 && h < 345) {
            CMC_T = 0.56 + Math.abs(0.2 * Math.cos(3.14 * (h + 168) / 180));
        } else {
            CMC_T = 0.36 + Math.abs(0.4 * Math.cos(3.14 * (h + 35) / 180));
        }
        double CMC_SH = 0;
        CMC_SH = CMC_SC * (CMC_T * CMC_F + 1 - CMC_F);
        double dCMC11 = Math.sqrt((dL / (1 * CMC_SL)) * (dL / (1 * CMC_SL)) + (dC / (1 * CMC_SC)) * (dC / (1 * CMC_SC)) + (d_H / CMC_SH) * (d_H / CMC_SH));
        double dCMC21 = Math.sqrt((dL / (2 * CMC_SL)) * (dL / (2 * CMC_SL)) + (dC / (1 * CMC_SC)) * (dC / (CMC_SC)) + (d_H / CMC_SH) * (d_H / CMC_SH));

        //-------------------555色调分类--------------------------------

        double temp, tempL, tempa, tempb;
        temp = Math.abs(dL / L);
        if (temp > 4.0)
            tempL = 4;
        else if (temp > 3.0)
            tempL = 3;
        else if (temp > 2.0)
            tempL = 2;
        else if (temp > 1.0)
            tempL = 1;
        else
            tempL = 0;
        temp = Math.abs(da / A);
        if (temp > 4.0)
            tempa = 4;
        else if (temp > 3.0)
            tempa = 3;
        else if (temp > 2.0)
            tempa = 2;
        else if (temp > 1.0)
            tempa = 1;
        else
            tempa = 0;
        temp = Math.abs(db / B);
        if (temp > 4.0)
            tempb = 4;
        else if (temp > 3.0)
            tempb = 3;
        else if (temp > 2.0)
            tempb = 2;
        else if (temp > 1.0)
            tempb = 1;
        else
            tempb = 0;
        if (dL < 0)
            tempL = -tempL;
        if (da < 0)
            tempa = -tempa;
        if (db < 0)
            tempb = -tempb;

        double  SCI_555 = (int) ((500 + tempL * 100) + (50 + tempa * 10) + (5 + tempb));
        //---------------------色强度--------------------

        double MINFLOAT = 100000;
        double sum = 0;
        double sum1 = 0;
        int NUM = 0;
        for (int i = 0; i < 31; i++) {
            if (MINFLOAT >= p.get(i)) {
                MINFLOAT = p.get(i);
            }
        }
        for (int j = 0; j < 31; j++) {
            if (p.get(j)== MINFLOAT) {
                NUM = j;
                break;
            }
        }
        double a1, b1, c1;

        a1 = ((1 - p1.get(NUM)* 0.01) * (1 - p1.get(NUM)* 0.01)) / (2 * p1.get(NUM) * 0.01);
        b1 = ((1 - p.get(NUM)* 0.01) * (1 - p.get(NUM) * 0.01)) / (2 * p.get(NUM) * 0.01);
        double Colorstrength = a1 / b1;

        for (int k = 0; k < 31; k++) {
            a1 = ((1 - p1.get(k) * 0.01) * (1 - p1.get(k) * 0.01)) / (2 * p1.get(k) * 0.01);
            sum1 += a1;
            b1 = ((1 - p.get(k) * 0.01) * (1 - p.get(k) * 0.01)) / (2 * p.get(k) * 0.01);
            sum += b1;
        }
        double appearance = (sum1 / sum);

        //----------------------沽色牢度----------------------------------
        double dEgs = (dE - 0.4 * Math.sqrt(dE * dE - dL * dL));
        double SSR = 6.1 - 1.45 * Math.log10(dEgs);
        double  staining_fastness;
        int S_Fastness;
        if (SSR > 4) {
            SSR = 5.0 - 0.23 * dEgs;
        }
        if (SSR < 1.25) {
            S_Fastness = 8;
            //staining_fastness = "1";
            staining_fastness =1;
        } else if (SSR < 1.75) {
            S_Fastness = 7;
            //staining_fastness = "1-2";
            staining_fastness =1.5;
        } else if (SSR < 2.25) {
            S_Fastness = 6;
            //staining_fastness = "2";
            staining_fastness =2;
        } else if (SSR < 2.75) {
            S_Fastness = 5;
            //staining_fastness = "2-3";
            staining_fastness =2.5;
        } else if (SSR < 3.25) {
            S_Fastness = 4;
           // staining_fastness = "3";
            staining_fastness =3;
        } else if (SSR < 3.75) {
            S_Fastness = 3;
            //staining_fastness = "3-4";
            staining_fastness =3.5;
        } else if (SSR < 4.25) {
            S_Fastness = 2;
            //staining_fastness = "4";
            staining_fastness =4;
        } else if (SSR < 4.75) {
            S_Fastness = 1;
            //staining_fastness = "4-5";
            staining_fastness =4.5;
        } else {//if(SSR<5)
            S_Fastness = 0;
            //staining_fastness = "5";
            staining_fastness =5;
        }

        //-----------------------变色牢度-----------------------
        double C_hM = 0;
        if (Math.abs(h - H) <= 180) {
            C_hM = (H + h) / 2;
        } else if (Math.abs(h - H) > 180 && Math.abs(H + h) < 360) {
            C_hM = (H + h) / 2 + 180;
        } else if (Math.abs(h - H) > 180 && Math.abs(H + h) >= 360) {
            C_hM = (H + h) / 2 - 180;
        }
        double C_x;
        if (Math.abs(C_hM - 280) <= 180) {
            C_x = ((C_hM - 280) / 30) * ((C_hM - 280) / 30);
        } else {
            C_x = ((360 - Math.abs(C_hM - 280)) / 30) * ((360 - Math.abs(C_hM - 280)) / 30);
        }
        double C_Cm = (C + c) / 2;
        double C_D = (dC * C_Cm * Math.exp(-C_x)) / 100.0;
        double C_dCk = dC - C_D;
        double C_dHk = d_H - C_D;
        double C_dCf = C_dCk / (1 + (20 * C_Cm / 1000.0) * (20 * C_Cm / 1000.0));
        double C_dHf = C_dHk / (1 + (10 * C_Cm / 1000.0) * (10 * C_Cm / 1000.0));
        double C_dEf = Math.sqrt(dL * dL + C_dCf * C_dCf + C_dHf * C_dHf);

        double C_Fastness;// 变色牢度
        double  color_fastness;
        if (C_dEf < 0.4) {
            C_Fastness = 0;
            //color_fastness = "5";
            color_fastness =5;
        } else if (C_dEf < 1.25) {
            C_Fastness = 1;
            //color_fastness = "4-5";
            color_fastness =4.5;

        } else if (C_dEf < 2.10) {
            C_Fastness = 2;
            //color_fastness = "4";
            color_fastness =4;
        } else if (C_dEf < 2.95) {
            C_Fastness = 3;
            //color_fastness = "3-4";
            color_fastness =3.5;
        } else if (C_dEf < 4.10) {
            C_Fastness = 4;
            //color_fastness = "3";
            color_fastness =3;
        } else if (C_dEf < 5.80) {
            C_Fastness = 5;
            //color_fastness = "2-3";
            color_fastness =2.5;
        } else if (C_dEf < 8.20) {
            C_Fastness = 6;
            //color_fastness = "2";
            color_fastness =2;
        } else if (C_dEf < 11.60) {
            C_Fastness = 7;
            //color_fastness = "1-2";
            color_fastness =1.5;
        } else {
            C_Fastness = 8;
            //color_fastness = "1";
            color_fastness =1;
        }



        //---------k/s-----------
       /* double[] ks400 = new double[31];
        double a2,b2,c2;
        for (int k = 0;k < 31;k++)
        {
            a2 = ((1-p[k]*0.01)*(1-p[k]*0.01))/(2*p[k]*0.01);
            ks400[k] = a2;
        }
        double  MINFLOAT1 = 0.000001;
        int NUM1=0;
        for (int i = 0;i < 31;i++)
        {
            if (MINFLOAT1 <= p[i])
            {
                MINFLOAT1 = p[i];
            }
        }
        for (int j = 0;j < 31;j++)
        {
            if (p[j] == MINFLOAT1)
            {
                NUM1 = j;
                break;
            }
        }

        b2 = ((1-p[NUM1]*0.01)*(1-p[NUM1]*0.01))/(2*p[NUM1]*0.01);
        String  ks_max = getDouble(b2);*/

        List<Double> list = new ArrayList<>();
        list.add(dE);
        list.add(dE);
        list.add(dCMC11);
        list.add(dCIE00);
        list.add(dE94);
        list.add(Hunter_Eab);
        list.add(staining_fastness);
        list.add(color_fastness);
        list.add(SCI_555);
        list.add(Colorstrength);
        return list;
    }


}
