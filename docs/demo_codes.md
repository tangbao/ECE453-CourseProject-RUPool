# 用法示范

update: 2017年12月16日

### 初始化数据库辅助类，不再有参数
```java
DatabaseUtils du = new DatabaseUtils();
```

### 新建用户并存入数据库，更改用户性别并存入数据库
```java
//在注册完之后获得uid，并获得用户的性别
User user = new User(uid, gender);
du.addUser(user); //存入数据库

//update user
user.setGender("Male");
du.updateUser(user); //将用户数据的更改存数据库
```

### 新建活动，存入数据库；在活动中修改每人要给的钱，添加与删除用户
```java
PoolActivity pa = new PoolActivity("test","123","today", "a test pa",
  "BSC", 5,5.5);
String paid = du.addActivity(pa); //添加活动的时候，会将创建人自动添加到活动成员中
pa.setId(paid); //此处不需要使用updateActivity将id存入数据库，因为已经自动存入
//没有存入目的地,也没有存入数据库，需要使用下面的方法：
pa.addPlace(place); //会自动将place的经纬度、名字和地址存入到pa中
du.addActivity(pa); //存入数据库

//update activity
pa.addMember(user.getUid());
du.updateActivity(pa);
```

### 获取用户、活动，根据Place查找活动
```java
public class TestActivity extends AppCompatActivity{

private static final String TAG = "TestActivity";

private static final int ACTION_GET_USER_ONE = 1;
private static final int ACTION_GET_USER_TWO = 2;
private static final int ACTION_GET_ACTIVITY = 1;
private static final int ACTION_FIND_ACTIVITY = 1;

//see Constant.java for the definition of constants

private User u1, u2;
private PoolActivity PA;
private List<PoolActivity> LPA;
private DatabaseUtils du;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    String uid1 = FirebaseAuth.getInstance().getCurrentUser().getId();
    String uid2 = "123132";
    String paid = "fdsafa";

    du = new DatabaseUtils();

    du.getUser(uid1, ACTION_GET_USER_ONE);
    du.getUser(uid2, ACTION_GET_USER_TWO);
    du.getActivity(paid, ACTION_GET_ACTIVITY);

    du.setOnGetUserListener(new Interface.OnGetUserListener() {
      @Override
      public void onGetUser(User user, int ACTION_CODE, int RESULT_CODE) {
        if(RESULT_CODE){
          switch(ACTION_CODE){
            case ACTION_GET_USER_ONE:
              u1 = user;
              break;
            case ACTION_GET_USER_TWO:
              u2 = user;
              break;
          }
        }else{
          Log.e(TAG, "Error: user not found");
        }
      }
    });

    du.setOnGetActivityListener(new Interface.OnGetActivityListener() {
      @Override
      public void onGetActivity(PoolActivity pa, int ACTION_CODE, int RESULT_CODE) {
          if(RESULT_CODE){
            PA = pa;
          }
      }
    });

    du.setOnFindActivityByLocationListener(new Interface.OnFindActivityByPlaceListener() {
      @Override
      public void onFindActivityByPlace(List<PoolActivity> lpa, int ACTION_CODE, int RESULT_CODE) {
          if(RESULT_CODE){
            LPA = lpa;
          }
      }
    });

    //…… 省略了一些 autocomplete相关的语句
    autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
      @Override
      public void onPlaceSelected(Place place) {
          // TODO:Get info about the selected place.
          du.findActivityByLocation(place, ACTION_FIND_ACTIVITY);
          //find activity by location, update the list in listener
          Log.i("AUTO", "Place: " + place.getName());
      }

      @Override
      public void onError(Status status) {
          // TODO:Handle the error.
          Log.i("AUTO", "An error occurred: " + status);
      }
    });


  }
}
