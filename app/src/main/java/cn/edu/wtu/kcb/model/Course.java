package cn.edu.wtu.kcb.model;

import android.content.ContentValues;
import android.database.Cursor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Course implements Serializable {
    private String id="";//课程唯一编号
    private String name="";//课程名
    private String campus="";//校区
    private String room="";//教室
    private String week="";//星期
    private int tMin=1;//开始节
    private int tMax=2;//结束节
    private int wMin=1;//开始周
    private int wMax=2;//结束周
    private String teacher ="";//老师
    private String job="";//职务
    private String test="";//考试方法

    public ContentValues getCV(){
        ContentValues values=new ContentValues();
        values.put("id",id);
        values.put("name",name);
        values.put("campus",campus);
        values.put("room",room);
        values.put("week",week);
        values.put("tMin",tMin);
        values.put("tMax",tMax);
        values.put("wMin",wMin);
        values.put("wMax",wMax);
        values.put("teacher", teacher);
        values.put("job",job);
        values.put("test",test);
        return values;
    }
    public static List<Course> getCourses(Cursor cursor){
        List<Course> courses=new ArrayList<>();
        while (cursor.moveToNext()){
            Course course=new Course();
            course.setId(cursor.getString(0));
            course.setName(cursor.getString(2));
            course.setCampus(cursor.getString(3));
            course.setRoom(cursor.getString(4));
            course.setWeek(cursor.getString(5));
            course.settMin(cursor.getInt(6));
            course.settMax(cursor.getInt(7));
            course.setwMin(cursor.getInt(8));
            course.setwMax(cursor.getInt(9));
            course.setTeacher(cursor.getString(10));
            course.setJob(cursor.getString(11));
            course.setTest(cursor.getString(12));
            courses.add(course);
        }
        return courses;
    }

    public int gettMin() {
        return tMin;
    }

    public void settMin(int tMin) {
        this.tMin = tMin;
    }

    public int gettMax() {
        return tMax;
    }

    public void settMax(int tMax) {
        this.tMax = tMax;
    }

    public int getwMin() {
        return wMin;
    }

    public void setwMin(int wMin) {
        this.wMin = wMin;
    }

    public int getwMax() {
        return wMax;
    }

    public void setwMax(int wMax) {
        this.wMax = wMax;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }


    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", campus='" + campus + '\'' +
                ", room='" + room + '\'' +
                ", week='" + week + '\'' +
                ", tMin=" + tMin +
                ", tMax=" + tMax +
                ", wMin=" + wMin +
                ", wMax=" + wMax +
                ", teacther='" + teacher + '\'' +
                ", job='" + job + '\'' +
                ", test='" + test + '\'' +
                '}';
    }

    public String showInfo() {
        return "课程："+name+"\n地点："+campus+room+"\n时间："+wMin+"-"+wMax+"周"+week
                +tMin+"-"+tMax+"节\n老师："+ teacher +job+"\n考试方式："+test;
    }
}
