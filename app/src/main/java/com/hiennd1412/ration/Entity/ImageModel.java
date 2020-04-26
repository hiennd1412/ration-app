package com.hiennd1412.ration.Entity;

import com.bumptech.glide.util.Util;
import com.hiennd1412.ration.Utils.Utils;

import org.json.JSONObject;

public class ImageModel {
    private String imageName;
    private String createdDate;

    public ImageModel(String imageName, String createdDate) {
        this.imageName = imageName;
        this.createdDate = createdDate;
    }

    public String getImageName() {
        return imageName;
    }
    public String getCreatedDate() {
        return createdDate;
    }

    public static ImageModel createImageModelFromJsonObject(JSONObject userData)  {
            ImageModel userInfo = new ImageModel(
                    Utils.getStringFromJSonObject(userData,"name"),
                    Utils.formatDate(Utils.getStringFromJSonObject(userData,"Created_date"))
            );
            return userInfo;
    }
}
