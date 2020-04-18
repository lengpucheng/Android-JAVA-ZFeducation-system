package cn.edu.wtu.kcb.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CourseProvider extends ContentProvider {
    //获取类绝对路径
    public static final String AUTHORITES=CourseProvider.class.getCanonicalName();
    private DBhelp dBhelp;
    public static final Uri URI_COURSE=Uri.parse("content://"+AUTHORITES+"/course");
    private static UriMatcher uriMatcher;
    public static final int CONTACT = 1;
    //每个类无论new多少对象，这个代码只执行一次（静态构造块）
    static{
        //匹配不成功
        uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITES,"/course", CONTACT);
    }
    @Override
    public boolean onCreate() {
        //获取当前上下文
        dBhelp=new DBhelp(getContext());
        //判断是否创建成功
        if(dBhelp!=null)
            return true;
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int mactch = uriMatcher.match(uri);
        Cursor cursor = null;
        switch (mactch) {
            case CONTACT:
                SQLiteDatabase db = dBhelp.getWritableDatabase();//只读
                cursor = db.query(DBhelp.T_COURSE, projection, selection, selectionArgs, null, null, sortOrder);
                if (cursor != null) {
                    Log.i("TAG", "query: Success");
                }
                break;
            default:
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int mactch=uriMatcher.match(uri);
        switch (mactch) {
            case CONTACT:
                SQLiteDatabase db = dBhelp.getReadableDatabase();
                long id = db.insert(DBhelp.T_COURSE, null, values);
                if (id != -1) {
                    Log.i("TAG", "insert: Success" + id);
                    //将uri向后拼接
                    uri = ContentUris.withAppendedId(uri, id);
                }
                break;
            default:
                break;
        }
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int mactch = uriMatcher.match(uri);
        int deletecount = 0;
        switch (mactch) {
            case CONTACT:
                SQLiteDatabase db = dBhelp.getReadableDatabase();
                deletecount = db.delete(DBhelp.T_COURSE, selection, selectionArgs);
                if (deletecount > 0)
                    Log.i("TAG", "delete: Success");
                break;
            default:
                break;
        }
        return deletecount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int mactch = uriMatcher.match(uri);
        int updatacount = 0;
        switch (mactch) {
            case CONTACT:
                SQLiteDatabase db = dBhelp.getReadableDatabase();
                updatacount = db.update(DBhelp.T_COURSE, values, selection, selectionArgs);
                if (updatacount > 0)
                    Log.i("TAG", "updata: Success");
                break;
            default:
                break;
        }
        return updatacount;
    }
}
