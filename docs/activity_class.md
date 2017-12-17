### 活动

id String id

名字 String name

发起人 String sponsorId

时间 String date

描述 String description

起点 String startPoint

终点名字 String destiName

终点地址 String destiAddress

终点Latitude double destiLatitude

终点Longitude double destiLongitude

最多参与的人数 int maxMember

预计每人多少钱 double moneyPerPerson

参与人 List<String> members, 其中string为uid

默认构造函数为空

一般构造函数为传入名字，发起人id，时间，描述，起点，最大人数和每人需要多少钱。会将发起人自动添加到成员列表中。

需要再使用addPlace(Place place)传入目的地，将会自动从place中获得终点名字、地址、经纬度存入poolActivity中

```java
PoolActivity(String name, String sponsorId, String date, String description,
                 String startPoint, int maxMember, double moneyPerPerson){}
```

提供了除了members之外所有成员的set与get函数。members提供了getMembers函数，addMember(String uid)与removeMember(String uid)函数，分别用来获得所有成员列表，添加一名成员和删除一名成员。

```java
public void addMember(String uid) {
}

public void removeMember(String uid){
}
```
