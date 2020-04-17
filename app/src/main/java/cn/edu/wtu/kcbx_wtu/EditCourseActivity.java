package cn.edu.wtu.kcbx_wtu;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class EditCourseActivity extends AppCompatActivity {

    @InjectView(R.id.topText)
    TextView topText;
    @InjectView(R.id.couName)
    EditText couName;
    @InjectView(R.id.couHouse)
    EditText couHouse;
    @InjectView(R.id.couWeek)
    Spinner couWeek;
    @InjectView(R.id.couWekaMin)
    EditText couWekaMin;
    @InjectView(R.id.couWekaMax)
    EditText couWekaMax;
    @InjectView(R.id.couClaMin)
    EditText couClaMin;
    @InjectView(R.id.couClaMax)
    EditText couClaMax;
    @InjectView(R.id.couTech)
    EditText couTech;
    @InjectView(R.id.couTes)
    EditText couTes;
    @InjectView(R.id.couOk)
    Button couOk;
    Course kcourse = null;
    boolean isUP = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        ButterKnife.inject(this);
        kcourse = (Course) getIntent().getSerializableExtra("course");
        topText.setText("添加课程");
        if (kcourse != null) {
            iniTdata();
            isUP = true;
        }else
            kcourse=new Course();

    }

    private void iniTdata() {
        topText.setText("编辑课程");
        couName.setText(kcourse.getName());
        couHouse.setText(kcourse.getRoom());
        //星期
        switch (kcourse.getWeek()) {
            case "星期一":
                couWeek.setSelection(0, true);
                break;
            case "星期二":
                couWeek.setSelection(1, true);
                break;
            case "星期三":
                couWeek.setSelection(2, true);
                break;
            case "星期四":
                couWeek.setSelection(3, true);
                break;
            case "星期五":
                couWeek.setSelection(4, true);
                break;
            case "星期六":
                couWeek.setSelection(5, true);
                break;
            case "星期七":
                couWeek.setSelection(6, true);
                break;
            default:
                break;
        }
        String[] data = kcourse.getTime().replaceAll("[^\\d-]", "").split("-");
        String MIN = data[0];
        String MAX = data[1];
        couClaMin.setText(MIN);
        couClaMax.setText(MAX);
        data = kcourse.getData().replaceAll("[^\\d-]", "").split("-");
        MIN = data[0];
        MAX = data[1];
        couWekaMin.setText(MIN);
        couWekaMax.setText(MAX);
        couTech.setText(kcourse.getTeacther());
        couTes.setText(kcourse.getTest());
    }

    @OnClick(R.id.couOk)
    public void onViewClicked() {
        if (cheack()) {
            ContentValues cv = kcourse.getCV();
            cv.put(DBhelp.ContactTable.NAME, kcourse.getName());
            if (isUP)
                getContentResolver().update(CourseProvider.URI_COURSE, cv, "_id=?", new String[]{kcourse.getId()});
            else
                getContentResolver().insert(CourseProvider.URI_COURSE, cv);

            Intent intent=new Intent(EditCourseActivity.this,Main2Activity.class);
            //将这个意图设置为窗口顶端，并释放其他窗口
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private boolean cheack() {
        if (couName.getText().toString().length() == 0) {
            couName.setError("课程不能为空");
            return false;
        }
        if (couHouse.getText().toString().length() == 0) {
            couHouse.setError("地点不能为空");
            return false;
        }
        if (couClaMax.getText().toString().length() == 0 || couClaMin.length() == 0) {
            couClaMax.setError("节次不能为空");
            return false;
        }
        if (couWekaMax.getText().toString().length() == 0 || couWekaMin.length() == 0) {
            couWekaMax.setError("周期不能为空");
            return false;
        }
        kcourse.setName(couName.getText().toString());
        kcourse.setRoom(couHouse.getText().toString());
        kcourse.setWeek(couWeek.getSelectedItem().toString());
        kcourse.setTime(couClaMin.getText().toString() + "-" + couClaMax.getText().toString());
        kcourse.setData(couWekaMin.getText().toString() + "-" + couWekaMax.getText().toString() + "周");
        kcourse.setTeacther(couTech.getText().toString());
        kcourse.setTest(couTes.getText().toString());
        return true;
    }
}
