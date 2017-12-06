package edu.rutgers.ece453.rupool;

/**
 * Created by Zhongze Tang on 2017/12/5.
 *
 *
 */

class Interface {

    public interface OnGetUserListener{
        void onGetUser(User user);
    }

    public interface OnGetActivityListener{
        void onGetActivity(PoolActivity pa);
    }

}
