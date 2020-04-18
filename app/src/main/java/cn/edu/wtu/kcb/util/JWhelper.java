package cn.edu.wtu.kcb.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.edu.wtu.kcb.model.Course;

public class JWhelper {
    private static String usernam = "";//用户名
    private static String password = "";//密码
    private static String cheack = "";//验证码
    private static String lt = "";//页面lt
    private static String JSESSION = "";//cookies 1/2
    private static String ROUTE = ""; //cookies 2/2
    private static String iPlanetDirectoryPro = null;  //真实cookies
    private static String loginuri = "http://jwglxt.wtu.edu.cn"; //真实登录页地址
    //登录页面
    private static String url = "https://auth.wtu.edu.cn/authserver/login?service=http%3A%2F%2Fjwglxt.wtu.edu.cn%2Fsso%2Fjziotlogin";
    private static CloseableHttpClient httpClient = null; //浏览器
    private static CloseableHttpResponse response = null;//响应
    private static HttpGet httpGet = null;//GET请求
    private static HttpPost httpPost = null;//Post请求
    private static int statusCode = 0;//状态码
    private static String tepUri = "";//临时存储重定向页面
    private static String eff = "";//错误原因
    private static Bitmap checkIMG = null;//验证码
    private static List<Course> courses=new ArrayList<>();
    public JWhelper() {

    }

    //获取错误信息
    public String getEff() {
        return eff;
    }

    //获取验证码
    public Bitmap getCheckIMG() {
        return checkIMG;
    }


    //打开一个页面，解析验证码图片
    public boolean isCheckIMG() {
        RequestConfig config = RequestConfig.custom().setRedirectsEnabled(false).build();//不允许重定向
        httpClient = HttpClients.custom().setDefaultRequestConfig(config).build();//配置浏览器
        //解析网页
        httpGet = new HttpGet(url);
        try {
            response = httpClient.execute(httpGet);//执行
        } catch (IOException e) {
            return toEffexecute("网络错误");
        }
        statusCode = response.getCode();//获取状态码
        if (statusCode != 200) {
            return toEffexecute("错误代码：Get-1-" + statusCode);
        }
        //获取响应内容
        HttpEntity entity = response.getEntity();
        //将内容编码为字符串
        String vlue = null;
        try {
            vlue = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            return toEffexecute("解析错误:Get-1");
        } catch (ParseException e) {
            return toEffexecute("解析错误:Get-1");
        }
        Document document = Jsoup.parseBodyFragment(vlue);//转换为文档树
        Element body = document.body();//获取主体
        //找到lt
        lt = body.select("[name=lt]").attr("value");
        //得到Cookies
        Header[] headers = response.getHeaders("Set-Cookie");
        ROUTE = headers[0].getValue().split(";")[0].split("=")[1];
        JSESSION = headers[1].getValue().split(";")[0].split("=")[1];

        Log.i("JWXTEFF", "Cookies GET1 Jsession:" + JSESSION);
        Log.i("JWXTEFF", "lt: " + lt);
        Log.i("JWXTEFF", "Cookies GET1 Route:" + ROUTE);
        /*
         * 获取验证码
         * */
        String chakUrl = "https://auth.wtu.edu.cn/authserver/captcha.html";
        httpGet = new HttpGet(chakUrl);
        try {
            response = httpClient.execute(httpGet);
        } catch (IOException e) {
            return toEffexecute("解析验证码错误");
        }
        HttpEntity entityCheck = response.getEntity();
        InputStream inputStream = null;//获取字符流
        try {
            inputStream = entityCheck.getContent();
            checkIMG = BitmapFactory.decodeStream(inputStream);//读取图像数据
            inputStream.close();
        } catch (IOException e) {
            return toEffexecute("加载验证码失败");
        }
        return true;
    }

    /*
     * 第二步:POST登录 获取Location和iPlanetDirectoryPro
     * */
    public boolean login(String id, String pass, String code) {
        this.usernam = id;
        this.password = pass;
        this.cheack = code;
        String loURI = "https://auth.wtu.edu.cn/authserver/login;jsessionid=" + JSESSION + "?service=http%3A%2F%2Fjwglxt.wtu.edu.cn%2Fsso%2Fjziotlogin";
        ;
        httpPost = new HttpPost(loURI);
        //请求头
        httpPost.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        httpPost.setHeader("Accept-Encoding", "gzip, deflate, br");
        httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
        httpPost.setHeader("Connection", "keep-alive");
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        httpPost.setHeader("Cookie", "route=" + ROUTE + "; JSESSIONID_auth=" + JSESSION);
        httpPost.setHeader("Host", "auth.wtu.edu.cn");
        httpPost.setHeader("Origin", "https://auth.wtu.edu.cn");
        httpPost.setHeader("Referer", url);
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:74.0) Gecko/20100101 Firefox/74.0");
        //参数
        List<NameValuePair> pairs = new ArrayList<>();//创建List集合，封装表单请求参数
        pairs.add(new BasicNameValuePair("username", usernam));
        pairs.add(new BasicNameValuePair("password", password));
        pairs.add(new BasicNameValuePair("captchaResponse", cheack));
        pairs.add(new BasicNameValuePair("lt", lt));
        pairs.add(new BasicNameValuePair("dllt", "userNamePasswordLogin"));
        pairs.add(new BasicNameValuePair("execution", "e1s1"));
        pairs.add(new BasicNameValuePair("_eventId", "submit"));
        pairs.add(new BasicNameValuePair("rmShown", "1"));
        //创建表单的Entity对象,将表单存入其中用UTF-8编码
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(pairs, Charset.forName("UTF-8"));
        //写入参数
        httpPost.setEntity(formEntity);

        //执行
        try {
            response = httpClient.execute(httpPost);
        } catch (IOException e) {
            return toEffexecute("发送请求失败");
        }

        //获取状态码，判断请求是否成功
        statusCode = response.getCode();
        if (statusCode != 302)
            return toEffexecute("用户名或密码错误 错误代码：" + statusCode);

        //拿到重定向地址
        tepUri = response.getHeaders("Location")[0].getValue();
        Log.i("JWXTEFF", "\nGET Uri2:" + tepUri);
        //拿到iPlanetDirectoryPro
        Header[] headerspost = response.getHeaders("Set-Cookie");
        iPlanetDirectoryPro = headerspost[2].getValue().split(";")[0];
        Log.i("JWXTEFF", "iPlanetDirectoryPro:" + iPlanetDirectoryPro);
        return true;
    }

    //获取真实请求地址
    public boolean getURI() {
        /*
         * 第三步：第二次GET，获取下一步的uri和中间cooki
         * */
        Log.e("JWXTEFF", "\n——————————————————————第二次GET——————————————————");
        String JSESSIONID2 = null;
        //第二次GET请求进入登录界面,拿到第二个JSESSIONID_iPlanetDirectoryPro
        httpGet = new HttpGet(tepUri);
        httpGet.setHeader("Cookie", iPlanetDirectoryPro);

        //执行
        try {
            response = httpClient.execute(httpGet);//执行
        } catch (IOException e) {
            return toEffexecute("解析失败 Get-2");
        }

        //获取状态码，判断请求是否成功
        statusCode = response.getCode();
        if (statusCode != 302)
            return toEffexecute("错误代码 Get-2:" + statusCode);

        Log.i("JWXTEFF", "Status code Get2:" + statusCode);
        //取得重定向地址
        tepUri = response.getHeaders("Location")[0].getValue();
        Log.i("JWXTEFF", "Get Uri 3 :" + tepUri);
        //得到set-Cookie 拿到JSESSIONID2
        JSESSIONID2 = response.getHeaders("Set-Cookie")[0].getValue().split(";")[0];
        Log.i("JWXTEFF", "JSESSIONID2: " + JSESSIONID2);

        /*
         * 第四步 第三次GET 获取下一步uri
         * */
        Log.e("JWXTEFF", "\n——————————————————————第三次GET——————————————————");
        httpGet = new HttpGet(tepUri);
        httpGet.setHeader("Cookie", JSESSIONID2);

        //执行
        try {
            response = httpClient.execute(httpGet);
        } catch (IOException e) {
            return toEffexecute("解析失败 Get-3");
        }

        //获取状态码，判断请求是否成功
        statusCode = response.getCode();
        if (statusCode != 302)
            return toEffexecute("错误代码 Get-3：" + statusCode);

        Log.i("JWXTEFF", "Status code Get3:" + statusCode);
        //获取重定向地址
        tepUri = response.getHeaders("Location")[0].getValue();
        Log.i("JWXTEFF", "Get Uri 4:" + tepUri);




        /*
         * 第五步 第四次GET 获取下一步uri和真实的Cookie
         * */
        Log.e("JWXTEFF", "\n——————————————————————第四次GET——————————————————");
        httpGet = new HttpGet(tepUri);
        httpGet.setHeader("Cookie", iPlanetDirectoryPro);
        //执行
        try {
            response = httpClient.execute(httpGet);
        } catch (IOException e) {
            return toEffexecute("解析失败 Get-4");
        }

        //获取状态码，判断请求是否成功
        statusCode = response.getCode();
        if (statusCode != 302)
            return toEffexecute("错误代码 Get-4-" + statusCode);

        Log.i("JWXTEFF", "Status code Get4:" + statusCode);
        //获取重定向地址
        tepUri = "http://jwglxt.wtu.edu.cn" + response.getHeaders("Location")[0].getValue();
        Log.i("JWXTEFF", "Get Uri 5:" + tepUri);
        //得到set-Cookie 拿到真实
        JSESSION = response.getHeaders("Set-Cookie")[0].getValue().split(";")[0];
        Log.i("JWXTEFF", "JSESSIONID: " + JSESSION);

        /*
         * 第六部 第五次GET 获取最终的登录页面
         * */
        Log.e("JWXTEFF", "\n——————————————————————第五次GET——————————————————");
        httpGet = new HttpGet(tepUri);
        httpGet.setHeader("Cookie", iPlanetDirectoryPro + ";" + JSESSION);
        //执行
        try {
            response = httpClient.execute(httpGet);
        } catch (IOException e) {
            return toEffexecute("解析失败 Get-5");
        }

        //获取状态码，判断请求是否成功
        statusCode = response.getCode();
        if (statusCode != 302)
            return toEffexecute("错误代码 Get-5-" + statusCode);

        Log.i("JWXTEFF", "Status code Get5:" + statusCode);
        //得到真实请求地址
        loginuri += response.getHeaders("Location")[0].getValue();
        Log.i("JWXTEFF", "Loginuri :" + loginuri);

        /*
         * 第七部 第六次GET 登录教务系统
         * */
        Log.e("JWXTEFF", "\n——————————————————————登录页面——————————————————");
        httpGet=new HttpGet(loginuri);
        httpGet.setHeader("Cookie", iPlanetDirectoryPro + ";" + JSESSION);
        //获取状态码，判断请求是否成功
        try {
            response=httpClient.execute(httpGet);
        } catch (IOException e) {
            return toEffexecute("解析失败 Get-End");
        }
        statusCode=response.getCode();
        if(statusCode!=200){
            return toEffexecute("错误代码 Get-End-"+statusCode);
        }
        Log.i("JWXTEFF", "Status code Getlog:" + statusCode);
        return true;
    }

    //解析课表-----注意 2020年的上学期参数是 2019，12  （即选课的时候）  2020下学期是 2020，3
    public boolean isCourse(String year,String semester){
        Log.e("JWXTEFF", "\n——————————————————————课表——————————————————");
        String kcbUri = "http://jwglxt.wtu.edu.cn/kbcx/xskbcx_cxXsKb.html?gnmkdm=N253508";
        httpPost=new HttpPost(kcbUri);
        //设置请求头
        httpPost.setHeader("Accept", "*/*");
        httpPost.setHeader("Accept-Encoding", "gzip, deflate, br");
        httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
        httpPost.setHeader("Connection", "keep-alive");
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        httpPost.setHeader("Cookie", iPlanetDirectoryPro + ";" + JSESSION);
        httpPost.setHeader("Host", "auth.wtu.edu.cn");
        httpPost.setHeader("Origin", "https://auth.wtu.edu.cn");
        httpPost.setHeader("Referer", "http://jwglxt.wtu.edu.cn/kbcx/xskbcx_cxXskbcxIndex.html?gnmkdm=N253508&layout=default&su=" + usernam);
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:74.0) Gecko/20100101 Firefox/74.0");
        httpPost.setHeader("X-Requested-With", "XMLHttpRequest");
        //表单数据
        List<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("xnm", year));
        pairs.add(new BasicNameValuePair("xqm", semester));
        //创建表单的Entity对象,将表单存入其中用UTF-8编码
        UrlEncodedFormEntity formEntity =new UrlEncodedFormEntity(pairs, Charset.forName("UTF-8"));

        //写入参数
        httpPost.setEntity(formEntity);
        try {
            response=httpClient.execute(httpPost);
        } catch (IOException e) {
           return toEffexecute("获取课表错误");
        }
        statusCode=response.getCode();
        if(statusCode!=200){
            return toEffexecute("错误代码-PostKCB-" + statusCode);
        }
        Log.i("JWXTEFF", "PostKCB-" + statusCode);
        HttpEntity kcb=response.getEntity();
        //解析Json
        JSONObject jsonObject = null;
        try {
            jsonObject = JSON.parseObject(EntityUtils.toString(kcb,"UTF-8"));
        } catch (Exception e) {
            return toEffexecute("解析课表错误-JSON");
        }
        if(jsonObject==null||jsonObject.get("kbList") == null){
            return toEffexecute("暂时没有发现课表");
        }
        JSONArray timeTable = JSON.parseArray(jsonObject.getString("kbList"));
        for (Iterator iterator = timeTable.iterator(); iterator.hasNext();){
            JSONObject lesson = (JSONObject) iterator.next();
            Course course=new Course();
            course.setId(usernam);
            course.setName(lesson.getString("kcmc"));
            course.setCampus(lesson.getString("xqmc"));
            course.setRoom(lesson.getString("cdmc"));
            course.setWeek(lesson.getString("xqjmc"));
            course.setTime(lesson.getString("jcs"));
            course.setData(lesson.getString("zcd"));
            course.setTeacther(lesson.getString("xm"));
            course.setJob(lesson.getString("zcmc"));
            course.setTest(lesson.getString("khfsmc"));
            courses.add(course);
        }
        return true;
    }

    //获取课表
    public List<Course> getCourses(){
        return courses;
    }


    //错误提示
    private boolean toEffexecute(String 错误提示) {
        eff = 错误提示;
        Log.e("JWXTEFF", 错误提示);
        return false;
    }
}
