# Android-JAVA-ZFeducation-system
在安卓上使用JAVA-HttpClient5.0从正方教务系统上爬取课程表等相关信息，并且设计了用户显示界面，按课程时间和周显示课表

### 目前已经实现了如下功能：
+ 1、通过教务系统`判断学号密码`
+ 2、模拟登录`抓取课程信息`
+ 3、根据课程信息`显示课程表`
+ 4、`动态判断是否本周`
+ 5、自定义`添加`、`编辑`、`删除`课程
+ 6、自动`判断课程导入`
+ 7、`高亮显示当天`
### 并且预留了以下实现接口：
(*主要是一个人，一次性写完时间太长*）
+ 1、通过教务系统`读取学籍信息`、`抓取照片`
+ 2、`抓取成绩`
+ 3、`空教室查询`
+ 3、`POST选课`
+ 4、`教学评价`
+ 5、`新闻和通知获取`

### 开发环境
`Android Studio3.6` `JDK1.8` `API29`

### 目录结构
* `cn.edu.wtu.kcb`主包
+ `activity` 活动
   `OpenActivity`启动页面，判断是否已经有课表数据
   `CourseActivity`显示课表
   `MainActivity` 登录页面
   `EditCourseActivity` 编辑/添加课程
+ `db` 存放数据库相关
* `model`存放对象
+ `util`工具包
   `JWhelper` 正方教务系统模拟登录
### 测试DOME
存放于app-debug中，在华为MENU上通过真机测试

### 已知问题
1、偶尔（非常少）会出现登录用户名、密码、验证码都正确但提示登录失败的情况；

2、偶尔（经常）在重新导入课表时候不会删除之前的课表造成叠加。

### 作者
Signed-off-by: lengpucheng <lpc@hll520.cn>


## 2020/4/18日更新内容：
*更新了项目结构，将方法按用途分包分类，便于维护和扩展*
*优化了数据结构，将周和节变为`int MIN,MAX` 分别存储，便于扩展*



# 详情信息说明

## 前言
之前写过一篇[JAVA使用HttpClient模拟登录正方教务系统，爬取学籍信息和课程表成绩等，超详细登录分析和代码注解](https://blog.csdn.net/XiaoYunKuaiFei/article/details/105438789)的教程，在移植到移动平台时候，发现了如下问题：
+ 抓取课表偶尔会不完全，出现**全部乱码**的情况
+ HttpClient相关包**与SDK冲突**，导致移植安卓出现问题
+ 教务系统偶尔会弹出**验证码**，导致*登陆失败*
+ 没有现成的**课程表界面**



### 问题分析
#### 解决抓取课程乱码
>当时使用的HttpClient3 已经过时，官方已经更新新版本

造成这个问题的原因是因为当时使用的`HttpClient3`已经过时，官方已经停止维护，因为年过许久部分功能已经不在适应当前的HTTP协议，导致部分传输不完全或者丢包从而造成乱码，解决方法也很简单粗暴，**直接换用`HttpClient5`**，抛弃之前的Post/getmode，换用新的HttpGet/Post，以及结果集；
**为了避免乱码，这里使用了阿里巴巴的JSON库`fastjson`来解析得到的响应JSON**
```java
  response=httpClient.execute(httpPost);
   HttpEntity kcb=response.getEntity();
    jsonObject = JSON.parseObject(EntityUtils.toString(kcb,"UTF-8"));
 	JSONArray timeTable = JSON.parseArray(jsonObject.getString("kbList"));
        for (Iterator iterator = timeTable.iterator(); iterator.hasNext();){
         JSONObject lesson = (JSONObject) iterator.next();
         Course course=new Course();
         course.setId(usernam);
         course.setName(lesson.getString("kcmc"));
            ……
        //这里都是一样的，太长了，为优化阅读，此次省略，参见下文
         courses.add(course);
        }
```
*注意：这里的Course为一个课程对象，方便对课程进行操作，具体结构会在下文提到*
#### 解决HttpClient与SDK冲突

>HttpClient相关包**与SDK冲突**，导致移植安卓出现问题

造成这个问题的原因是由于谷歌抛弃了阿帕奇的Http架构，移除了SDK，并且和现有SDK包内方法重名，这个问题困扰了很长时间，也在CSDN和其他等平台查阅了很多相关资料，均无从解决，后来在阿帕奇的官方资料上找到了一篇关于这个的解决方案，很简单，**直接在`Gradle`中添加依赖即可**
```
dependencies {
    api 'com.github.ok2c.hc5.android:httpclient-android:0.1.0'
}

```
具体可以参考我的另一篇文章[在安卓9.0以上版本使用HttpClient](https://blog.csdn.net/XiaoYunKuaiFei/article/details/105543103)

#### 解决教务系统验证码
>教务系统偶尔会弹出**验证码**，导致*登陆失败*

解决这个问题，先手动模拟登录出现验证码的情况试一试
![验证码出现](https://img-blog.csdnimg.cn/20200418144512714.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L1hpYW9ZdW5LdWFpRmVp,size_16,color_FFFFFF,t_70)
先用控制台查看验证码相关的HTML代码
![HTML代码](https://img-blog.csdnimg.cn/20200418144610164.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L1hpYW9ZdW5LdWFpRmVp,size_16,color_FFFFFF,t_70)
可以看到其指向了一个页面`captcha.html?ts=557`，在前面加上登录页面的前缀得到以下这个网址`https://auth.wtu.edu.cn/authserver/captcha.html?ts=557`
经过反复刷新和测试，发现后面的`ts=`偶尔变化偶尔不变，猜想这个ts可能是一个无关的时间参数，直接去掉ts访问网页试一试![验证码](https://img-blog.csdnimg.cn/20200418144911226.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L1hpYW9ZdW5LdWFpRmVp,size_16,color_FFFFFF,t_70)
果然，验证码出现了，查看网络请求：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200418145035976.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L1hpYW9ZdW5LdWFpRmVp,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/2020041814511431.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L1hpYW9ZdW5LdWFpRmVp,size_16,color_FFFFFF,t_70)
**一个Get带上请求的Cookie，没有其他参数**
虽然无法得知验证码为什么会出现，但是已经得到了如何获取验证码，那么直接简单粗暴的每一次请求都获取验证码，并且输入，这样就可以避免偶尔出现验证码时导致登录失败；
##### 使用`HttpClient5`抓取验证码：
同之前一样，直接用GET方法带上之前的Cookie发送请求，拿到响应内容
```java
String chakUrl = "https://auth.wtu.edu.cn/authserver/captcha.html";
        httpGet = new HttpGet(chakUrl);
        try {
            response = httpClient.execute(httpGet);
        } catch (IOException e) {
            return toEffexecute("解析验证码错误");
        }
        HttpEntity entityCheck = response.getEntity();
```
响应内容是一张图片，那就直接转换为字符流显示出来

```java
        InputStream inputStream = null;//获取字符流
        try {
            inputStream = entityCheck.getContent();
            checkIMG =BitmapFactory.decodeStream(inputStream);//读取图像数据
            inputStream.close();
        } catch (IOException e) {
            return toEffexecute("加载验证码失败");
        }
        checkIMG.setImageBitmap(helper.getCheckIMG());
```

*以上是在Android上使用字符流保存图片然后直接显示在屏幕上*
移动平台上可以采用以下方法将字符流转换为字节流写入到文件直接保存到本地：

```java
 InputStream inputStream=entityCheck.getContent();//获取字符流
        OutputStream os = new FileOutputStream("C:\\chakOK.jpg");//写入流
        byte[] b = new byte[1024];//缓冲区
        int temp = 0;//长度
        while ((temp = inputStream.read(b)) != -1) {
            os.write(b, 0, temp);
        }
        os.close();
        inputStream.close();
```
##### 发送验证码结果
上述是抓取验证码，那拿到了验证码后如何将验证码的结果告诉教务系统呢？继续手动登录模拟：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200418150230709.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L1hpYW9ZdW5LdWFpRmVp,size_16,color_FFFFFF,t_70)
对比之前的模拟登录，可以看到参数中多了一个`captchaResponse	`字段，很明显这就验证码，那么这就与之前模拟登录一模一样了，这是多了个验证码字段，将它填入即可

```java
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
```
至此，关于验证码的问题就解决了，又可以链接上教务系统了！

#### 解决没有课程表界面
>没有现成的**课程表界面**

遇到了这个问题，没有别的解决办法，就直接造一个吧！简单梳理多款相关软件的布局，并且自己就是学生，按照自己的需求，将界面进行如下刨析：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200418151541290.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L1hpYW9ZdW5LdWFpRmVp,size_16,color_FFFFFF,t_70)
布局如上，一个很简单的三段布局，但是有一个很致命的问题出现了，课程不一定是连续的，并且可能会有课程重合*例如部分有些课程时间一样，但是起止周不相同*，这个时候使用典型的线性布局肯定不可以，在思考后，发现了帧布局`FrameLayout`这么个玩意儿，我的解决方法如下：
>1.整个全局为一个`LinearLayout`，最上方的时间处用一个横向的`LinearLayout`平均8等分，用于之后的天数定位
>
>2.下方为一个`ScrollView`，包裹一个`LinearLayout`和一个`FrameLayout`，按照之前天数的划分分别占1份和7份，在`LinearLayout`添加若干高度相同的子块用于表示节次；
>3.之后直接在运行时动态新建`view`宽度和之前划分的每一份相同，高度为`节次数*子块高度`，根据课程的时间设置`Margins`：，
>`上边距=子块高度*（上课节-1）`
> `左边距=份数大小*（星期数-1）`

具体布局代码参考 `app/layout/course_activity.xml`

自此，刚刚遇到的4个问题都已经解决，现在可以开始构建最终的APP了；

## 最终代码
#### 定义Course对象
为了存储课程信息方便后续使用，定义一个`Course`对象；

```java
  	private String id="";//课程唯一编号
    private String name="";//课程名
    private String campus="";//校区
    private String room="";//教室
    private String week="";//星期
    private int tMin=1;//开始节
    private int tMax=2;//结束节
    private int wMin=1;//开始周
    private int wMax=2;//结束周
    private String teacher ="";//老师
    private String job="";//职务
    private String test="";//考试方法
```
其中第一`getCV()`方法用于将对象转换为`ContentValues`键值对，方便写数据库使用：

```java
public ContentValues getCV(){
        ContentValues values=new ContentValues();
        values.put("id",id);
        values.put("name",name);
        values.put("campus",campus);
        values.put("room",room);
        values.put("week",week);
        values.put("tMin",tMin);
        values.put("tMax",tMax);
        values.put("wMin",wMin);
        values.put("wMax",wMax);
        values.put("teacher", teacher);
        values.put("job",job);
        values.put("test",test);
        return values;
    }
```
同理定义一个静态`getCourses(Cursor cursor)`方法，直接将`Cursor`转换为对象数组，方便读取数据库显示课表使用：

```java
 public static List<Course> getCourses(Cursor cursor){
        List<Course> courses=new ArrayList<>();
        while (cursor.moveToNext()){
            Course course=new Course();
            course.setId(cursor.getString(0));
            course.setName(cursor.getString(2));
            course.setCampus(cursor.getString(3));
            course.setRoom(cursor.getString(4));
            course.setWeek(cursor.getString(5));
            course.settMin(cursor.getInt(6));
            course.settMax(cursor.getInt(7));
            course.setwMin(cursor.getInt(8));
            course.setwMax(cursor.getInt(9));
            course.setTeacher(cursor.getString(10));
            course.setJob(cursor.getString(11));
            course.setTest(cursor.getString(12));
            courses.add(course);
        }
        return courses;
    }
```
#### 建立SQLite数据表
避免每次启动都有登录教务系统，直接将课程信息保存到本地数据库，登录一次后每次就可以直接跳转到课表
```java
public class DBhelp extends SQLiteOpenHelper {
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
```
#### 封装模拟登录工具类
为了方便调用和日后扩展，我将所有与模拟登录相关的方法全部封装于一个`JWhelper`类中，供之后直接调用，在这里使用的是`HttpClient5`与上一篇文章的实现大致相仿，只不过实现方法的细节不同，因此就****不再在此次进行原理分析****，具体原理可以参考[我的上一篇文章](https://blog.csdn.net/XiaoYunKuaiFei/article/details/105438789)，

参看`app\cn.edu.wtu.kcb.util.JWhelper`


说明：
+ `isCheckIMG()`用于用Get方法获取验证码，并解析，之后可以调用`getCheckIMG()`获取到解析的图片，并显示在界面上
+ `login(String id, String pass, String code)`方法在其中判断返回的状态码是否为302判断登录是否成功并使用返回一个`boolean`，为此这个方法还可以进行一些扩展操作例如**判断绑定学号**等
+ `getURI()`方法获取最终的Cookie和URI，并存储在内存中，同样也可以扩展，列如**获取学生信息、获取成绩、抢课**等操作
+ `isCourse(String year,String semester)`方法用于获取课程表，并提供JSON解析，结果用` getCourses()`方法返回一个课程数组，同样结合`getURI()`,按照本方法的范例可以实现**获取学生信息、获取成绩、抢课、一键教学评价**等更多操作；

*写入课程到数据库的方法就不在这里赘述了*
****
#### 显示课程表
上文已经定义了XML布局，这里直接用代码来实现刚刚留下的空白
##### 1、获取参数
首先获取屏幕宽度，并且平均为8，用于之后定位课程位置与大小
```java
   int d = getWindowManager().getDefaultDisplay().getWidth() / 8;
```
##### 2、获取当前周数
利用`Calendar`取得当前的日期和周数，显示到布局上的星期上，并且将当天的背景显示颜色突出
*需要注意的是这里使用的`SharedPreferences`来判断和保存用户是否设置过周数*
```java
TextView[] weekTexts = {weekOne, weekTwo, weekSan, weekSi, weekWU, weekLiu, weekDAY};
        //获取当前周数
        int weeks = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
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
         TextView textNow = new TextView(this);
        LinearLayout.LayoutParams paramNow = new LinearLayout.LayoutParams(d, dip2px(14 * 65));
        textNow.setBackgroundColor(0x40E91E63);
        paramNow.setMargins((weekday - 1) * d, 0, 0, 0);
        textNow.setLayoutParams(paramNow);
        PFF.addView(textNow);
```
##### 3、读取数据库中的课程信息
这里用到了之前上文定义的`getCourses(cursor)` 方法，直接返回了数组，两行代码就完成了，是不是非常方便
```java
 Cursor cursor = getContentResolver().query(CourseProvider.URI_COURSE, null, null, null, null);
        List<Course> courses = Course.getCourses(cursor);
```

##### 4、判断是否非本周并给课程表上色
这里用到了一个变量`i`，由于每个课程最多会有4个相邻的课程，为了****避免显示到一起颜色相同不便分辨****我设置了5种不同的颜色，这样可以尽量避免相邻的两个课程颜色相同，并且比较**本周与起止周数**的大小判断本周是否有课，如果没课直接涂灰，并加上`[非本周]`提示，拼接课程信息用于显示；
```java
int i=0
for (final Course course : courses) {
            //新建文本
            final TextView textView = new TextView(this);
            //设置颜色
            i++;
            switch (i % 5) {
                case 0:
                    textView.setBackgroundColor(0xffCDDC39);
                    break;
                case 1:
                    textView.setBackgroundColor(0xff22ccff);
                    break;
                case 2:
                    textView.setBackgroundColor(0xff22ffcc);
                    break;
                case 3:
                    textView.setBackgroundColor(0xff00BCD4);
                    break;
                case 4:
                    textView.setBackgroundColor(0xffF44336);
                    break;
            }
            //显示字符串
            String str = "";

            //获取当前课的周期,转换为纯数字和-，按-分隔
           int MIN=course.getwMin();
           int MAX=course.getwMax();
            if (MIN > weeks || weeks > MAX) {
                str += "[非本周]";
                //设置灰色
                textView.setBackgroundColor(0xcccccccc);
            }
            str += course.getName();
            str += "@" + course.getRoom();
            str += "#" + course.getTeacher() + course.getJob();
            str += "|" + course.getTest();
            textView.setText(str);
```
##### 5、定位并显示课程
如上文所诉的原理，工具起止节和星期，分别设置高度，和左、上边距，从而达到定位课程的效果，最后添加到`FrameLayout`，显示课表；
```java
 MIN=course.gettMin();
            MAX=course.gettMax();
            int height =MAX-MIN+1;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(d, dip2px(height* 65));
            //设置位置
            int week = 6;
            switch (course.getWeek()) {
                case "星期一":
                    week = 0;
                    break;
                case "星期二":
                    week = 1;
                    break;
                case "星期三":
                    week = 2;
                    break;
                case "星期四":
                    week = 3;
                    break;
                case "星期五":
                    week = 4;
                    break;
                case "星期六":
                    week = 5;
                    break;
                case "星期七":
                    week = 6;
                    break;
                default:
                    break;
            }
            //左边-星期，  上边-节
            params.setMargins(week * d, dip2px((MIN - 1) * 65), 0, 0);
            textView.setLayoutParams(params);
            textView.setTextSize(12);
            textView.setTextColor(Color.WHITE);
            //结尾显示省略号
            textView.setEllipsize(TextUtils.TruncateAt.END);
            //添加到容器
            PFF.addView(textView);
```
##### 6、内部方法：dp转px
手机屏幕和方法内设置的数值单位是px，XML中的布局单位是DP，为了保持一致，查阅资料后发现了一个方法，直接将dp 转换为px：

```java
  public int dip2px(float dpValue) {
        Context context = this;
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
```
至此这个APP搭建完毕*省略了登录页面，这个实现方法已经在上文`JWhelper（）`中实现，具体界面可以自己实现,本文实在是太长了，不在赘述*，康康效果吧！

## 效果演示
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200418162200474.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L1hpYW9ZdW5LdWFpRmVp,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200418162257502.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L1hpYW9ZdW5LdWFpRmVp,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200418162215102.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L1hpYW9ZdW5LdWFpRmVp,size_16,color_FFFFFF,t_70)

![在这里插入图片描述](https://img-blog.csdnimg.cn/2020041816232963.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L1hpYW9ZdW5LdWFpRmVp,size_16,color_FFFFFF,t_70)

## 后记
以上是安卓APP抓取教务系统课表等信息并显示的主要部分分析和代码实现，**本项目的源码我已经开源放到了GitHub上**，大家可以在GitHub上下载源码进行扩展创作：
>[ **点击跳转GitHub Android-JAVA-ZFeducation-system**](https://github.com/lengpucheng/Android-JAVA-ZFeducation-system)

目前已经实现了如下功能：
+ 1、通过教务系统`判断学号密码`
+ 2、模拟登录`抓取课程信息`
+ 3、根据课程信息`显示课程表`
+ 4、`动态判断是否本周`
+ 5、自定义`添加`、`编辑`、`删除`课程
+ 6、自动`判断课程导入`
+ 7、`高亮显示当天`
并且预留了以下实现接口：
(*主要是一个人，一次性写完时间太长*）
+ 1、通过教务系统`读取学籍信息`、`抓取照片`
+ 2、`抓取成绩`
+ 3、`空教室查询`
+ 3、`POST选课`
+ 4、`教学评价`
+ 5、`新闻和通知获取`
因此，欢迎大家拷贝下载，一起完善该项目！

**转载请注明出处**




