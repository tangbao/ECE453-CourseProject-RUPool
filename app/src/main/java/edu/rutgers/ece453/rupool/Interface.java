package edu.rutgers.ece453.rupool;

/**
 * Created by Zhongze Tang on 2017/12/5.
 *
 *
 */

class Interface {

    public interface OnGetUserListener{
        void onGetUser(User user, int ACTION_CODE);
    }

    public interface OnGetActivityListener{
        void onGetActivity(PoolActivity pa, int ACTION_CODE);
    }

}
