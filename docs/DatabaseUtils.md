初始化类, 在frag中需要将this替换为getAcitivity()

```java
DatabaseUtils du = new DatabaseUtils(this);
```

目前提供的方法：

-  添加用户到数据库

  其中uid从FirebaseUser中获得

```java
  String uid = FirebaseAuth.getInstance().getCurrentUser().getId();
```

```java
  void addUser(String uid, User user){}
```

- 获得用户，需实现callback Interface.OnGetUserListener

```java
  void getUser(String uid){}
```

- 添加活动

```java
  void addActivity(PoolActivity pa){}
```

- 获得活动，需实现 Interface.OnGetActivityListener

```java
  void getActivity(String aid){}
```
