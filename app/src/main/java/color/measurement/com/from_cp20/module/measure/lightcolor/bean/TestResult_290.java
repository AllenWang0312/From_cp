package color.measurement.com.from_cp20.module.measure.lightcolor.bean;

import android.graphics.Color;

import java.util.HashMap;
import java.util.List;

import edu.tjrac.swant.color_measurement.ColorUtil_Measure;
import edu.tjrac.swant.color_measurement.constast.Contast1;

/**
 * Created by wpc on 2017/2/10.
 */

public class TestResult_290 {
    List<Double> SCI;//41  41  3
    List<Double> Lab_Ch, Luv, Hunter_Lab, XYZ, Yxy, RGB, WI_YI_Tint, MSE_HVCList;
    int type;
    int c;

    public TestResult_290(int type, List<Double> SCI, int light, int angle) {
        this.type = type;
        this.SCI = SCI;
        XYZ = ColorUtil_Measure.comuteXYZ(SCI, light, angle);
        Yxy = ColorUtil_Measure.XYZtoYxy(XYZ.get(0), XYZ.get(1), XYZ.get(2));
        RGB = ColorUtil_Measure.XYZtoRGB(XYZ.get(0), XYZ.get(1), XYZ.get(2), light, angle);
        Hunter_Lab = ColorUtil_Measure.XYZtoHunterLab(XYZ.get(0), XYZ.get(1), XYZ.get(2), light, angle);
        Luv = ColorUtil_Measure.XYZtoLuv(XYZ.get(0), XYZ.get(1), XYZ.get(2), light, angle);
        Lab_Ch = ColorUtil_Measure.XYZtoCIE_LabCH(XYZ.get(0), XYZ.get(1), XYZ.get(2), light, angle);
        WI_YI_Tint = ColorUtil_Measure.XYZtoWI_TINT_CIE(XYZ.get(0), XYZ.get(1), XYZ.get(2), light, angle);
        MSE_HVCList = ColorUtil_Measure.XYZtoMSE_HVC(XYZ.get(0), XYZ.get(1), XYZ.get(2));
        c = Color.rgb(RGB.get(0).intValue(), RGB.get(1).intValue(), RGB.get(2).intValue());
    }

    public List<Double> getMSE_HVCList() {
        if (MSE_HVCList != null) {
            return MSE_HVCList;
        }
        return ColorUtil_Measure.XYZtoMSE_HVC(XYZ.get(0), XYZ.get(1), XYZ.get(2));
    }

    public int getColor() {
        if (c != 0) {
            return c;
        }
        return Color.rgb(RGB.get(0).intValue(), RGB.get(1).intValue(), RGB.get(2).intValue());
    }

    public List<Double> getLab_Ch() {
        if (Lab_Ch != null) {
            return Lab_Ch;
        }
        return ColorUtil_Measure.XYZtoCIE_LabCH(XYZ.get(0), XYZ.get(1), XYZ.get(2), Contast1.ILLUMINANT_D65, 0);
    }

    public List<Double> getLuv() {
        if (Luv != null) {
            return Luv;
        }
        return ColorUtil_Measure.XYZtoLuv(XYZ.get(0), XYZ.get(1), XYZ.get(2), Contast1.ILLUMINANT_D65, 0);
    }

    public List<Double> getHunter_Lab() {
        if (Hunter_Lab != null) {
            return Hunter_Lab;
        }
        return ColorUtil_Measure.XYZtoHunterLab(XYZ.get(0), XYZ.get(1), XYZ.get(2), Contast1.ILLUMINANT_D65, 0);
    }

    public List<Double> getXYZ() {

        if (XYZ != null) {
            return XYZ;
        }
        return ColorUtil_Measure.comuteXYZ(SCI, Contast1.ILLUMINANT_D65, 0);
    }

    public List<Double> getYxy() {
        if (Yxy != null) {
            return Yxy;
        }
        return ColorUtil_Measure.XYZtoYxy(XYZ.get(0), XYZ.get(1), XYZ.get(2));
    }


    public List<Double> getRGB() {
        if (RGB != null) {
            return RGB;
        }
        return ColorUtil_Measure.XYZtoRGB(XYZ.get(0), XYZ.get(1), XYZ.get(2), Contast1.ILLUMINANT_D65, 0);
    }


    public List<Double> getWI_YI_Tint() {
        if (WI_YI_Tint != null) {
            return WI_YI_Tint;
        }
        return ColorUtil_Measure.XYZtoWI_TINT_CIE(XYZ.get(0), XYZ.get(1), XYZ.get(2), Contast1.ILLUMINANT_D65, 0);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public HashMap<String, Double> toHashMap() {
        HashMap<String, Double> values = new HashMap<>();

        values.put("L*", Lab_Ch.get(0));
        values.put("a*", Lab_Ch.get(1));
        values.put("b*", Lab_Ch.get(2));
        values.put("c*", Lab_Ch.get(3));
        values.put("h", Lab_Ch.get(4));

        values.put("u*", Luv.get(1));
        values.put("v*", Luv.get(2));

        values.put("L(Hunter)", Hunter_Lab.get(0));
        values.put("a(Hunter)", Hunter_Lab.get(1));
        values.put("b(Hunter)", Hunter_Lab.get(2));

        values.put("X", XYZ.get(0));
        values.put("Y", XYZ.get(1));
        values.put("Z", XYZ.get(2));
        values.put("x", Yxy.get(1));
        values.put("y", Yxy.get(2));

        values.put("MSE_H", MSE_HVCList.get(0));
        values.put("MSE_V", MSE_HVCList.get(1));
        values.put("MSE_C", MSE_HVCList.get(2));

        values.put("R", RGB.get(0));
        values.put("G", RGB.get(1));
        values.put("B", RGB.get(2));

        values.put("Tint(CIE)", WI_YI_Tint.get(0));
        values.put("Tint(E313)", WI_YI_Tint.get(1));
        values.put("Tint(Ganz)", WI_YI_Tint.get(2));

        values.put("YI(ASM E313-73)", WI_YI_Tint.get(3));
        values.put("YI(ASM E313-10)", WI_YI_Tint.get(4));
        values.put("YI(D 1925)", WI_YI_Tint.get(5));

        values.put("WI(CIE)", WI_YI_Tint.get(6));
        values.put("WI(Ganz)", WI_YI_Tint.get(7));
        values.put("WI(Hunter)", WI_YI_Tint.get(8));
        values.put("WI(Tauble)", WI_YI_Tint.get(9));
        values.put("WI(Berger)", WI_YI_Tint.get(10));
        values.put("WI(AATCC)", WI_YI_Tint.get(11));
        values.put("WI(ASM E313-73)", WI_YI_Tint.get(12));
        values.put("WI(ASM E313-10)", WI_YI_Tint.get(13));
        return values;
    }
}
