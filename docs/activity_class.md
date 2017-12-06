**活动**

存在尚未解决的问题，请参见[Issues #3](https://github.com/tangbao/ECE453-CourseProject/issues/3)

id String id

名字 String name

发起人 String sponsorId

最多参与的人数 int maxMember

时间 String Date

地点 String location

预计每人多少钱 double moneyPerPerson

参与人 List<String> members, 其中string为uid

默认构造函数为空

一般构造函数为传入名字，发起人id，最大参与人数，时间，地点和每人需要多少钱

```java
PoolActivity(String name, String sponsorId, int maxMember,  String Date, String location, double moneyPerPerson){}
```

提供了类的大部分成员的set与get函数。
