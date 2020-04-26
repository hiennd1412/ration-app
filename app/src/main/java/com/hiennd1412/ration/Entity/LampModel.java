package com.hiennd1412.ration.Entity;

import com.hiennd1412.ration.Utils.Utils;

import org.json.JSONObject;

public class LampModel {
    private String lampName;
    private String createdDate;

    public LampModel(String lampName, String createdDate) {
        this.lampName = lampName;
        this.createdDate = createdDate;
    }

    public String getLampName() {
        return lampName;
    }
    public String getCreatedDate() {
        return createdDate;
    }

    public static LampModel createLampModelFromJsonObject(JSONObject userData)  {
            LampModel lampInfo = new LampModel(
                    Utils.getStringFromJSonObject(userData,"name"),
                    Utils.formatDate(Utils.getStringFromJSonObject(userData,"Created_date"))
            );
            return lampInfo;
    }
}
