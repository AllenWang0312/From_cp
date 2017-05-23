package edu.tjrac.swant.color_measurement.constast;

/**
 * Created by cimcenter on 2016/12/5.
 */

public class Contast1 {
    public static  int  WaveNum = 31;//波长个数
    static int Lab_XYZ_REF_Mode = 0;
    public static double Illuminant_Observer_X = 0;
    public static double Illuminant_Observer_Y = 0;
    public static double Illuminant_Observer_Z = 0;

   // CIE_Illuminants_C_2

    public static final double[] XYZ2LABCH_2_A = {109.85, 100.00, 35.58};
    public static final double[] XYZ2LABCH_2_C = {98.04, 100.00, 118.10};
    public static final double[] XYZ2LABCH_2_D50 = {96.42, 100.00, 82.49};
    public static final double[] XYZ2LABCH_2_D55 = {95.68, 100.00, 92.14};
    public static final double[] XYZ2LABCH_2_D65 = {95.04, 100.00, 108.88};
    public static final double[] XYZ2LABCH_2_D75 = {94.96, 100.00, 122.61};
    public static final double[] XYZ2LABCH_2_F1 = {92.19, 100.00, 91.54};
    public static final double[] XYZ2LABCH_2_F2 = {98.81, 100.00, 55.82};
    public static final double[] XYZ2LABCH_2_F3 = {103.64, 100.00, 38.32};
    public static final double[] XYZ2LABCH_2_F4 = {109.28, 100.00, 27.06};
    public static final double[] XYZ2LABCH_2_F5 = {90.28, 100.00, 87.44};
    public static final double[] XYZ2LABCH_2_F6 = {97.00, 100.00, 49.18};
    public static final double[] XYZ2LABCH_2_F7 = {94.72, 100.00, 97.16};
    public static final double[] XYZ2LABCH_2_F8 = {96.66, 100.00, 72.50};
    public static final double[] XYZ2LABCH_2_F9 = {100.66, 100.00, 57.38};
    public static final double[] XYZ2LABCH_2_F10 = {97.98, 100.00, 71.03};
    public static final double[] XYZ2LABCH_2_F11 = {103.12, 100.00, 51.53};
    public static final double[] XYZ2LABCH_2_F12 = {126.15, 100.00, 19.73};
    public static final double[] XYZ2LABCH_10_A = {111.14, 100.00, 35.2};
    public static final double[] XYZ2LABCH_10_C = {97.29, 100.00, 116.14};
    public static final double[] XYZ2LABCH_10_D50 = {96.72, 100.00, 81.41};
    public static final double[] XYZ2LABCH_10_D55 = {95.80, 100.00, 90.97};
    public static final double[] XYZ2LABCH_10_D65 = {94.81, 100.00, 107.32};
    public static final double[] XYZ2LABCH_10_D75 = {94.43, 100.00, 120.71};
    public static final double[] XYZ2LABCH_10_F1 = {93.95, 100.00, 90.53};
    public static final double[] XYZ2LABCH_10_F2 = {102.80, 100.00, 56.75};
    public static final double[] XYZ2LABCH_10_F3 = {108.78, 100.00, 39.53};
    public static final double[] XYZ2LABCH_10_F4 = {115.03, 100.00, 28.19};
    public static final double[] XYZ2LABCH_10_F5 = {92.59, 100.00, 86.82};
    public static final double[] XYZ2LABCH_10_F6 = {101.74, 100.00, 50.29};
    public static final double[] XYZ2LABCH_10_F7 = {95.20, 100.00, 95.68};
    public static final double[] XYZ2LABCH_10_F8 = {97.06, 100.00, 70.80};
    public static final double[] XYZ2LABCH_10_F9 = {102.15, 100.00, 56.72};
    public static final double[] XYZ2LABCH_10_F10 = {100.32, 100.00, 71.49};
    public static final double[] XYZ2LABCH_10_F11 = {105.76, 100.00, 52.11};
    public static final double[] XYZ2LABCH_10_F12 = {129.06, 100.00, 19.86};

    public static final int ILLUMINANT_A = 0x00; // 标准光源A（白炽灯，色温：2856K）
    public static final int ILLUMINANT_C = 0x01;// 光源C（日光，色温：6774K）
    public static final int ILLUMINANT_D50 = 0x02; // 光源D50（日光，色温：5003K）
    public static final int ILLUMINANT_D55 = 0x03; //
    public static final int ILLUMINANT_D65 = 0x04; // 标准光源D65（日光，色温：6504K）
    public static final int ILLUMINANT_D75 = 0x05; //
    public static final int ILLUMINANT_F1 = 0x06; //
    public static final int ILLUMINANT_F2 = 0x07; // 冷白（荧光灯）
    public static final int ILLUMINANT_F3 = 0x08; //
    public static final int ILLUMINANT_F4 = 0x09; //
    public static final int ILLUMINANT_F5 = 0x0a; //
    public static final int ILLUMINANT_F6 = 0x0b; // 冷白（荧光灯）
    public static final int ILLUMINANT_F7 = 0x0c; // 色补偿A日光白（荧光灯）
    public static final int ILLUMINANT_F8 = 0x0d; // 色补偿AAA自然白（荧光灯）
    public static final int ILLUMINANT_F9 = 0x0e; //
    public static final int ILLUMINANT_F10 = 0x0f; // 三波段自然白（荧光灯）
    public static final int ILLUMINANT_F11 = 0x10; // 三波段冷白（荧光灯）
    public static final int ILLUMINANT_F12 = 0x11; // 三波段暖白（荧光灯）
    public static final int ILLUMINANT_CWF = 0x12; //F2
    public static final int ILLUMINANT_U30 = 0x13; //F12
    public static final int ILLUMINANT_DLF = 0x14; //F7
    public static final int ILLUMINANT_NBF = 0x15; //F11
    public static final int ILLUMINANT_TL83 = 0x16; //F12
    public static final int ILLUMINANT_TL84 = 0x17; //F11
    public static final int ILLUMINANT_U35 = 0x18; //F3




}
