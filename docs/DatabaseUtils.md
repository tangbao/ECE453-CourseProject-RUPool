初始化类, 在frag中需要将this替换为getAcitivity()

```java
DatabaseUtils du = new DatabaseUtils(this);
```

目前提供的方法：

-  添加用户到数据库

```java
  void addUser(User user){}
```

- 获得用户，需实现callback Interface.OnGetUserListener

```java
  void getUser(String uid){}
```

- 更新用户到数据库

```java
  void updateUser(User user){}
```

- 添加活动, 返回PoolActivity的id，请使用setId()存入PoolActivity中。

```java
  String addActivity(PoolActivity pa){}
```

- 获得活动，需实现 Interface.OnGetActivityListener

```java
  void getActivity(String aid){}
```

- 更新活动到数据库

```java
  void updateActivity(PoolActivity pa){}
```
