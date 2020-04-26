package com.hiennd1412.ration.Entity;

import com.hiennd1412.ration.Utils.Utils;

import org.json.JSONObject;

import java.util.Date;

public class CallLogModel {
    public String callNumber;
    public Date callTime;
    public String callType;

    public CallLogModel(String callNumber, Date callTime, String callType) {
        this.callNumber = callNumber;
        this.callTime = callTime;
        this.callType = callType;
    }
}
