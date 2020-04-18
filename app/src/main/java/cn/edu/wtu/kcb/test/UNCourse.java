package cn.edu.wtu.kcb.test;

import java.util.ArrayList;
import java.util.List;

public class UNCourse{
    private String week="";
    private String name="";
    private String am1_2="0-0";
    private String am3_5="0-0";
    private String pm6_7="0-0";
    private String pm8_9="0-0";
    private String ni10_12="0-0";

    public static List<UNCourse> getDeft(String name){
        List<UNCourse> unCourses=new ArrayList<>();
        unCourses.add(new UNCourse("星期一",name));
        unCourses.add(new UNCourse("星期二",name));
        unCourses.add(new UNCourse("星期三",name));
        unCourses.add(new UNCourse("星期四",name));
        unCourses.add(new UNCourse("星期五",name));
        return unCourses;
    }

    public int[] getMax_Min(int i){
        int[] Max_Min={0,0};
        switch (i){
            case 0:
                String[] am1=getAm1_2().split("-");
                Max_Min=new int[]{Integer.parseInt(am1[0]), Integer.parseInt(am1[1])};
                break;
            case 1:
                String[] am2=getAm3_5().split("-");
                Max_Min=new int[]{Integer.parseInt(am2[0]), Integer.parseInt(am2[1])};
                break;
            case 2:
                String[] pm1=getPm6_7().split("-");
                Max_Min=new int[]{Integer.parseInt(pm1[0]), Integer.parseInt(pm1[1])};
                break;
            case 3:
                String[] pm2=getPm8_9().split("-");
                Max_Min=new int[]{Integer.parseInt(pm2[0]), Integer.parseInt(pm2[1])};
                break;
            case 4:
                String[] ni=getNi10_12().split("-");
                Max_Min=new int[]{Integer.parseInt(ni[0]), Integer.parseInt(ni[1])};
                break;
        }
        return Max_Min;
    }

    public UNCourse(String week,String name){
        this.week=week;
        this.name=name;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAm1_2() {
        return am1_2;
    }

    public void setAm1_2(String am1_2) {
        this.am1_2 = am1_2;
    }

    public String getAm3_5() {
        return am3_5;
    }

    public void setAm3_5(String am3_5) {
        this.am3_5 = am3_5;
    }

    public String getPm6_7() {
        return pm6_7;
    }

    public void setPm6_7(String pm6_7) {
        this.pm6_7 = pm6_7;
    }

    public String getPm8_9() {
        return pm8_9;
    }

    public void setPm8_9(String pm8_9) {
        this.pm8_9 = pm8_9;
    }

    public String getNi10_12() {
        return ni10_12;
    }

    public void setNi10_12(String ni10_12) {
        this.ni10_12 = ni10_12;
    }
}

