package cn.edu.wtu.kcb.test;

import java.util.List;

import cn.edu.wtu.kcb.model.Course;

public class UNCoursehelper {
    private List<UNCourse> unCourses;

    public UNCoursehelper(List<Course> courses,String name){
        unCourses=UNCourse.getDeft(name);
        for(Course course:courses){
            switch (course.getWeek()){
                case "星期一":
                    setTime(unCourses.get(0),course);
                    break;
                case "星期二":
                    setTime(unCourses.get(1),course);
                    break;
                case "星期三":
                    setTime(unCourses.get(2),course);
                    break;
                case "星期四":
                    setTime(unCourses.get(3),course);
                    break;
                case "星期五":
                    setTime(unCourses.get(4),course);
                    break;
            }
        }
    }

    public List<UNCourse> getUnCourses(){
        return unCourses;
    }

    private void    setTime(UNCourse unCourse, Course course) {
        if(course.gettMin()<3)
            unCourse.setAm1_2(course.getwMin()+"-"+course.getwMax());
        if(course.gettMax()>2&&course.gettMax()<=5)
            unCourse.setAm3_5(course.getwMin()+"-"+course.getwMax());
        if(course.gettMin()>5&&course.gettMin()<8)
            unCourse.setPm6_7(course.getwMin()+"-"+course.getwMax());
        if(course.gettMax()>7&&course.gettMax()<10)
            unCourse.setPm8_9(course.getwMin()+"-"+course.getwMax());
        if(course.gettMin()>9)
            unCourse.setNi10_12(course.getwMin()+"-"+course.getwMax());
    }


}

