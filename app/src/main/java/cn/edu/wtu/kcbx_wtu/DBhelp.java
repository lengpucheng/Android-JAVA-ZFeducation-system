package cn.edu.wtu.kcbx_wtu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.Nullable;

public class DBhelp extends SQLiteOpenHelper {
    public static final String T_COURSE="t_course";
    public class ContactTable implements BaseColumns {
        public static final String ID="id";
        public static final  String NAME="name";//课程名
        public static final String CAMPUS="campus";//校区
        public static final  String ROOM="room";//教室
        public static final String WEEK="week";//星期
        public static final  String TIME="time";//节
        public static final String DATA="data";//持续时间
        public static final  String TEACTHER="teacther";//老师
        public static final  String JOB="job";//职务
        public static final  String TEST="test";//考试方法
    }

    public DBhelp(@Nullable Context context) {
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
                +ContactTable.TIME+" text,"
                +ContactTable.DATA+" text,"
                +ContactTable.TEACTHER+" text,"
                +ContactTable.JOB+" text,"
                +ContactTable.TEST+" text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
