**用户**

用户其实有两个类，一个是Firebase自带的[FirebaseUser](https://firebase.google.com/docs/reference/android/com/google/firebase/auth/FirebaseUser?authuser=0), 它自己提供了很多内置的方法（包括登陆验证等），非常方便。

看一下[这里](https://firebase.google.com/docs/auth/android/manage-users?authuser=0)就可以了解最基本的使用了。

另一个类是我们自己实现的User类，主要是为了存性别（String gender）和用户参与的活动列表（List<String> activities，其中String为activities的id）

默认构造函数为空（为了可以dataSnapshot.getValue())

构造函数，传入性别即可（新建用户的时候默认用户没有参加活动）

```java
public User(String gender){
}
```

获得性别

```java
public String getGender() {
}
```

设置性别

```java
public void setGender(String gender) {
}
```

获得用户参与的活动列表

```java
public List<String> getActivities() {
}
```

设置用户参与的活动列表

```java
public void setActivities(List<String> activities){
}
```

将一个活动id添加到用户活动列表里去

```java
public void addActivity(String activity_id) {
}
```


将一个活动id从用户活动列表中移除

```java
public void quitActivity(String activity_id) {
}
```
