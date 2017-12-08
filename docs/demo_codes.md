# 用法示范

### 初始化数据库辅助类
```java
DatabaseUtils du = new DatabaseUtils(this); //in activities
DatabaseUtils du = new DatabaseUtils(getActivity()); //in fragments
```

### 新建用户并存入数据库，更改用户性别并存入数据库
```java
//在注册完之后获得uid，并获得用户的性别
User user = new User(uid, gender); //可以不存入性别
du.addUser(user); //存入数据库
//update user
user.setGender("Male");
du.updateUser(user); //将用户数据的更改存数据库
```

### 新建活动，存入数据库；在活动中修改每人要给的钱，添加与删除用户
```java
PoolActivity pa = new PoolActivity("test","123",5,"today", "NJ",5.5);
String paid = du.addActivity(pa); //添加活动的时候，会将创建人自动添加到活动成员中
pa.setId(paid); //此处不需要使用updateActivity将id存入数据库，因为已经自动存入
//update activity
pa.addMember(user.getUid());
du.updateActivity(pa);
```

### 获取用户与活动
```java
public class TestActivity extends AppCompatActivity
        implements Interface.OnGetUserListener, Interface.OnGetActivityListener{

  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      String uid = "123";
      String paid = "fdsafa";
      du.getUser(uid);
      du.getActivity(paid);
  }

  public void onGetUser(User user){
      Log.d(TAG, "onGetUser");
  }

  public void onGetActivity(PoolActivity pa){
      Log.d(TAG, "onGetActivity");
  }

}
