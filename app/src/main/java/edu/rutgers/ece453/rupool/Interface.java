package edu.rutgers.ece453.rupool;

import java.util.List;

/**
 * Created by Zhongze Tang on 2017/12/5.
 *
 *
 */

class Interface {

    public interface OnGetUserListener{
        void onGetUser(User user, int ACTION_CODE, int RESULT_CODE);
    }

    public interface OnGetActivityListener{
        void onGetActivity(PoolActivity pa, int ACTION_CODE, int RESULT_CODE);
    }

    public interface OnFindActivityByPlaceListener{
        void onFindActivityByPlace(List<PoolActivity> lpa, int ACTION_CODE, int RESULT_CODE);
    }

    public interface OnFindAllActivityListener{
        void onFindAllActivity(List<PoolActivity> lpa, int RESULT_CODE);
    }

}
