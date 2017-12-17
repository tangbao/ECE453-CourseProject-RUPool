初始化类

```java
DatabaseUtils du = new DatabaseUtils();
```

目前提供的方法：

-  添加用户到数据库

```java
  void addUser(User user){}
```

- 更新用户到数据库

```java
  void updateUser(User user){}
```

- 获得用户，必须注册获得用户的listener

```java
  void getUser(String uid, final int ACTION_CODE){}
    //ACTION_CODE用于区分操作
```

- 获得用户的listener //参见demo_codes

```java
  void setOnGetUserListener(OnGetUserListener onGetUserListener){}
```

- 添加活动, 返回PoolActivity的id，请使用setId()存入PoolActivity中。

```java
  String addActivity(PoolActivity pa){}
```

- 更新活动到数据库

```java
  void updateActivity(PoolActivity pa){}
```

- 获得活动，必须注册获得用户的listener

```java
  void getActivity(String aid, int ACTION_CODE){}
    //ACTION_CODE用于区分操作
```

- 获得活动的listner //参见demo_codes

```java
  void setOnGetActivityListener(OnGetActivityListener onGetActivityListener){}
```

- 搜索所有在place的活动, 必须注册获得结果的listener

```java
  void findActivityByLocation(Place place,final int ACTION_CODE){}
    //ACTION_CODE用于区分操作
```

- 搜索活动的listener // 参见参见demo_codes

```java
  void setOnFindActivityByLocationListener(OnFindActivityByPlaceListener onFindActivityByPlaceListener){}
```
