package cn.edu.wtu.kcb.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import cn.edu.wtu.kcb.db.CourseProvider;
import cn.edu.wtu.kcb.R;

public class OpenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);
        Cursor flag=getContentResolver().query(CourseProvider.URI_COURSE,null,null,null,null);
        if(flag.getCount()>=1){
            Intent intent=new Intent(OpenActivity.this, CourseActivity.class);
            startActivity(intent);
            finish();
        }else {
            Intent intent=new Intent(OpenActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
