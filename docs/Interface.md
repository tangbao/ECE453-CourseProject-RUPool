```java
public interface OnGetUserListener{
    void onGetUser(User user, int ACTION_CODE, int RESULT_CODE);
}

public interface OnGetActivityListener{
    void onGetActivity(PoolActivity pa, int ACTION_CODE, int RESULT_CODE);
}

public interface OnFindActivityByPlaceListener{
    void onFindActivityByPlace(List<PoolActivity> lpa, int ACTION_CODE, int RESULT_CODE);
}
```
