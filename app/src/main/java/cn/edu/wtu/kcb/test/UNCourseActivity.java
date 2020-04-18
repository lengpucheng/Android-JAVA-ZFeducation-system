package cn.edu.wtu.kcb.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.edu.wtu.kcb.R;
import cn.edu.wtu.kcb.db.CourseProvider;
import cn.edu.wtu.kcb.model.Course;

import static cn.edu.wtu.kcb.activity.CourseActivity.KCB;

public class UNCourseActivity extends AppCompatActivity {

    int weeks = 0;
    @InjectView(R.id.weekNums)
    TextView weekNums;
    @InjectView(R.id.okWeek)
    LinearLayout okWeek;
    @InjectView(R.id.weekOne)
    TextView weekOne;
    @InjectView(R.id.weekTwo)
    TextView weekTwo;
    @InjectView(R.id.weekSan)
    TextView weekSan;
    @InjectView(R.id.weekSi)
    TextView weekSi;
    @InjectView(R.id.weekWU)
    TextView weekWU;
    @InjectView(R.id.weekLiu)
    TextView weekLiu;
    @InjectView(R.id.weekDAY)
    TextView weekDAY;
    @InjectView(R.id.Top)
    LinearLayout Top;
    @InjectView(R.id.textView16)
    TextView textView16;
    @InjectView(R.id.textView17)
    TextView textView17;
    @InjectView(R.id.textView18)
    TextView textView18;
    @InjectView(R.id.textView19)
    TextView textView19;
    @InjectView(R.id.textView20)
    TextView textView20;
    @InjectView(R.id.textView22)
    TextView textView22;
    @InjectView(R.id.textView21)
    TextView textView21;
    @InjectView(R.id.textView23)
    TextView textView23;
    @InjectView(R.id.textView24)
    TextView textView24;
    @InjectView(R.id.textView25)
    TextView textView25;
    @InjectView(R.id.textView27)
    TextView textView27;
    @InjectView(R.id.textView28)
    TextView textView28;
    @InjectView(R.id.PFF)
    FrameLayout PFF;
    @InjectView(R.id.JCK)
    LinearLayout JCK;
    @InjectView(R.id.body)
    ScrollView body;
    @InjectView(R.id.About)
    TextView About;
    @InjectView(R.id.reset)
    LinearLayout reset;

    private List<UNCourse> unCourses;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_activity);
        ButterKnife.inject(this);
        List<Course> courses = Course.getCourses(getContentResolver().query(CourseProvider.URI_COURSE,
                null, null, null, null));
        unCourses = new UNCoursehelper(courses, "dome").getUnCourses();
        iniView();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void iniView() {

        //获取屏幕宽度，平均为8
        int d = getWindowManager().getDefaultDisplay().getWidth() / 8;
        TextView[] weekTexts = {weekOne, weekTwo, weekSan, weekSi, weekWU, weekLiu, weekDAY};
        //获取当前周数
        weeks = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        //打开SP
        SharedPreferences sp = getSharedPreferences(KCB, MODE_PRIVATE);
        //判断是否设置周数
        if (sp.getBoolean("flag", false)) {
            //读取设置时候的,如果没有就是weeks,默认为1
            int theWeek = sp.getInt("week", 1);
            //读取设置时候的日期
            int y = sp.getInt("year", 2020);
            int m = sp.getInt("math", 3);
            int day = sp.getInt("day", 3);
            //读取设置时候的日期
            Calendar calendar = new GregorianCalendar(y, m, day);//日期对象
            //获取设置那天的周数
            int theWeekofY = calendar.get(Calendar.WEEK_OF_YEAR);
            Log.d("TAGG", "iniView: " + theWeekofY);
            //得到当前周数
            weeks = (weeks - theWeekofY) + theWeek;
        }


        weekNums.setText(weeks + "周");
        //获取当前周几，转换为中国周（周一为第一天）
        int weekday = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
        //设置日期
        for (int i = 0; i < 7; i++)
            weekTexts[i].append("\n" + getDate(i - weekday));

        //设置当天的背景为有颜色
        TextView textNow = new TextView(this);
        LinearLayout.LayoutParams paramNow = new LinearLayout.LayoutParams(d, dip2px(14 * 65));
        textNow.setBackgroundColor(0x40E91E63);
        paramNow.setMargins((weekday - 1) * d, 0, 0, 0);
        textNow.setLayoutParams(paramNow);
        PFF.addView(textNow);
        //生成课表占位符
        TextView[][] textViews = new TextView[5][5];
        for (int i = 0; i < 5; i++) {
            textViews[i][0]=new TextView(this);
            textViews[i][0].setBackgroundColor(0xff22ffcc);
            textViews[i][1]=new TextView(this);
            textViews[i][2]=new TextView(this);
            textViews[i][2].setBackgroundColor(0xff22ffcc);
            textViews[i][3]=new TextView(this);
            textViews[i][4]=new TextView(this);
            textViews[i][4].setBackgroundColor(0xff22ffcc);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(d, dip2px(2 * 65));
            params.setMargins(i * d, 0, 0, 0);
            textViews[i][0].setLayoutParams(params);
            PFF.addView(textViews[i][0]);
            params.setMargins(i * d, dip2px(65*2), 0, 0);
            textViews[i][1].setLayoutParams(params);
            PFF.addView(textViews[i][1]);
            params.setMargins(i * d, dip2px(65*5), 0, 0);
            textViews[i][2].setLayoutParams(params);
            PFF.addView(textViews[i][2]);
            params.setMargins(i * d, dip2px(65*7), 0, 0);
            textViews[i][3].setLayoutParams(params);
            PFF.addView(textViews[i][3]);
            params.setMargins(i * d, dip2px(65*9), 0, 0);
            textViews[i][4].setLayoutParams(params);
            PFF.addView(textViews[i][4]);
        }

        //添加人员显示
        for (UNCourse unCourse : unCourses) {
            switch (unCourse.getWeek()) {
                case "星期一":
                    addPeople(unCourse, textViews[0]);
                    break;
                case "星期二":
                    addPeople(unCourse, textViews[1]);
                    break;
                case "星期三":
                    addPeople(unCourse, textViews[2]);
                    break;
                case "星期四":
                    addPeople(unCourse, textViews[3]);
                    break;
                case "星期五":
                    addPeople(unCourse, textViews[4]);
                    break;
            }
        }
    }

    private void addPeople(UNCourse unCourse, TextView[] textView) {
        for (int i = 0; i < 5; i++) {
            if (weeks <unCourse.getMax_Min(i)[0] ||weeks > unCourse.getMax_Min(i)[1])
                textView[i].append(unCourse.getName() + "\n");
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private String getDate(int day) {
        Date date = new Date();//取当前时间
        Calendar calendar = new GregorianCalendar();//日期对象
        calendar.setTime(date);//日期设置为当前时间
        calendar.add(calendar.DATE, day + 1);//往后推一天.整数往后推,负数往前移动（Calendar为0开始，故推一天为今天）
        date = calendar.getTime(); //这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd");//设置日期格式
        String Day = formatter.format(date);
        return Day;
    }

    //dp转px
    public int dip2px(float dpValue) {
        Context context = this;
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}

