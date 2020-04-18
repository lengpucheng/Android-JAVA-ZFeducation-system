# Android-JAVA-ZFeducation-system
在安卓上使用JAVA-HttpClient5.0从正方教务系统上爬取课程表等相关信息，并且设计了用户显示界面，按课程时间和周显示课表

### 当前功能
1、使用HttpClient模拟登录正方教务系统；

2、自动爬取验证码，登录后爬取课程表；

3、将课程表解析并保存到本地；

4、设计了显示界面，可以显示日期、课程；

5、可以手动添加、删除、修改、重新导入课程表；

6、根据当前周数`动态显示`本周是否有课（非本周会黑白）。

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




