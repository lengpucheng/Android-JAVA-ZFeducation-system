package cn.edu.wtu.kcb.activity;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.edu.wtu.kcb.model.Course;
import cn.edu.wtu.kcb.db.CourseProvider;
import cn.edu.wtu.kcb.util.JWhelper;
import cn.edu.wtu.kcb.R;
import cn.edu.wtu.kcb.util.ThreadUtils;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.name)
    EditText name;
    @InjectView(R.id.pass)
    EditText pass;
    @InjectView(R.id.check)
    EditText check;
    @InjectView(R.id.checkIMG)
    ImageView checkIMG;
    @InjectView(R.id.ok)
    Button ok;
    String id;
    JWhelper helper = new JWhelper();
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    checkIMG.setImageBitmap(helper.getCheckIMG());
                    break;
                case 2:
                    Toast.makeText(MainActivity.this, "获取验证码失败" + helper.getEff(), Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(MainActivity.this, "登录成功"+ helper.getEff(), Toast.LENGTH_SHORT).show();
                    CheckTest();
                    break;
                case 4:
                    Toast.makeText(MainActivity.this, "登录失败" + helper.getEff(), Toast.LENGTH_SHORT).show();
                    break;
                case 9:
                    Intent intent = new Intent(MainActivity.this, CourseActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };
    @InjectView(R.id.spinner)
    Spinner spinner;
    @InjectView(R.id.xueQi)
    RadioGroup xueQi;

    String year;
    String semester;
    private void CheckTest() {
        ThreadUtils.runInThread(new Runnable() {
            @Override
            public void run() {
                if (helper.getURI()){
                    if (helper.isCourse(year, semester)) {
                        getContentResolver().delete(CourseProvider.URI_COURSE, null,null);
                        List<Course> courses = helper.getCourses();
                        for (Course course : courses) {
                            Log.i("KCB", course.toString());
                            getContentResolver().insert(CourseProvider.URI_COURSE, course.getCV());
                        }
                    }
                Message msg = new Message();
                msg.what = 9;
                handler.sendMessage(msg);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        //获取当前年
        int y= Calendar.getInstance().get(Calendar.YEAR);
        final int[] items={y-3,y-2,y-1,y,y+1,y+2};
        //获取月
        y= Calendar.getInstance().get(Calendar.MONTH)+1;
        if(y<7) {
            xueQi.check(R.id.radioButtonShang);
            semester= String.valueOf(12);
        }
        else {
            xueQi.check(R.id.radioButtonXia);
            semester= String.valueOf(3);
        }

        xueQi.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButtonXia:
                        semester= String.valueOf(3);
                        break;
                    case R.id.radioButtonShang:
                        semester= String.valueOf(12);
                        break;
                }
            }
        });

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < items.length; i++) // Maximum size of i upto --> Your Array Size
        {
            dataAdapter.add(String.valueOf(items[i]));
        }
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(3,true);
        ThreadUtils.runInThread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                if (helper.isCheckIMG())
                    msg.what = 1;
                else
                    msg.what = 2;
                handler.sendMessage(msg);
            }
        });
    }

    @OnClick({R.id.checkIMG, R.id.ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.checkIMG:
                helper = new JWhelper();
                ThreadUtils.runInThread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        if (helper.isCheckIMG())
                            msg.what = 1;
                        else
                            msg.what = 2;
                        handler.sendMessage(msg);
                    }
                });
                break;
            case R.id.ok:
                year=spinner.getSelectedItem().toString();
                if(semester.equals("12"))
                    year= String.valueOf(Integer.valueOf(year)-1);
                id = name.getText().toString();
                final String password = pass.getText().toString();
                final String cheack = check.getText().toString();
                ThreadUtils.runInThread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        if (helper.login(id, password, cheack))
                            message.what = 3;
                        else
                            message.what = 4;
                        handler.sendMessage(message);
                    }
                });
                break;
        }
    }
}
