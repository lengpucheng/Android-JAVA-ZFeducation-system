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
    private String time="";//节
    private String data="";//持续时间
    private String teacther="";//老师
    private String job="";//职务
    private String test="";//考试方法

    public ContentValues getCV(){
        ContentValues values=new ContentValues();
        values.put("id",id);
        values.put("name",name);
        values.put("campus",campus);
        values.put("room",room);
        values.put("week",week);
        values.put("time",time);
        values.put("data",data);
        values.put("teacther",teacther);
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
            course.setTime(cursor.getString(6));
            course.setData(cursor.getString(7));
            course.setTeacther(cursor.getString(8));
            course.setJob(cursor.getString(9));
            course.setTest(cursor.getString(10));
            courses.add(course);
        }
        return courses;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTeacther() {
        return teacther;
    }

    public void setTeacther(String teacther) {
        this.teacther = teacther;
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
                ", time='" + time + '\'' +
                ", data='" + data + '\'' +
                ", teacther='" + teacther + '\'' +
                ", job='" + job + '\'' +
                ", test='" + test + '\'' +
                '}';
    }
    public String toString2() {
        return "课程："+name+"\n地点："+campus+room+"\n时间："+data+week+time+"节\n老师："+teacther+job+"\n考试方式："+test;
    }
}
