package edu.rutgers.ece453.rupool.objects;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Zhongze Tang on 2017/11/21.
 *
 *
 */

public class Activity {
    private long id;
    private String name;
    private String a;//todo
    private int maxMember;
    private Date time;//todo set as a string or a Date object?
    private String location;//todo 建立一个loaction的obj，存Latlng?
    private double moneyPerPerson;
    private List<Map<String, Object>> members;

}
