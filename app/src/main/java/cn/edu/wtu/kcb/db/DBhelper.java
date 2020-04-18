package cn.edu.wtu.kcb.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.Nullable;

public class DBhelper extends SQLiteOpenHelper {
    public static final String T_COURSE="t_course";
    public class ContactTable implements BaseColumns {
        public static final  String ID="id";
        public static final  String NAME="name";//课程名
        public static final  String CAMPUS="campus";//校区
        public static final  String ROOM="room";//教室
        public static final  String WEEK="week";//星期
        public static final  String TMIN="tMin";//开始节
        public static final  String TMAX="tMax";//下课节
        public static final  String WMIN="wMin";//开始周
        public static final  String WMAX="wMax";//结束周
        public static final  String TEACHER="teacher";//老师
        public static final  String JOB="job";//职务
        public static final  String TEST="test";//考试方法
    }

    public DBhelper(@Nullable Context context) {
        super(context, "course.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="Create table "+T_COURSE+" (_id integer  PRIMARY KEY AUTOINCREMENT,"
                +ContactTable.ID+" text,"
                +ContactTable.NAME+" text,"
                +ContactTable.CAMPUS+" text,"
                +ContactTable.ROOM+" text,"
                +ContactTable.WEEK+" text,"
                +ContactTable.TMIN+" integer,"
                +ContactTable.TMAX+" integer,"
                +ContactTable.WMIN+" integer,"
                +ContactTable.WMAX+" integer,"
                +ContactTable.TEACHER +" text,"
                +ContactTable.JOB+" text,"
                +ContactTable.TEST+" text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
