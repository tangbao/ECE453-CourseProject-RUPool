package edu.rutgers.ece453.rupool;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by zhu_z on 2017/12/16.
 */
public class DataTimeUtilTest {
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void dateToISO8601() throws Exception {
        String ret = DataTimeUtil.dateToISO8601(2017, 12, 16, 16, 26);
        System.out.println(ret);
    }

}