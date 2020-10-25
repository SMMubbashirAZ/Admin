package com.mart.admin.model;

/**
 * Created by WeMartDevelopers .
 */
public class ShopModel {
    /*{
      "id": 2,
      "name": "New Shop",
      "number": "03423519691",
      "password": "j10S8Rgc1wBMaftzsZ7DHjFYm0Y04bWP5GG0wXi1fgI=",
      "email": "newshop@gmail.com",
      "token": "VmVuZG9yLFJpZGVyLFVzZXI6MDM0MjM1MTk2OTFCc3RpbmRhd3JsZA==",
      "img": "/ShopImgs/03423519691.PNG",
      "isApproved": false,
      "createdDate": "2020-10-04T00:00:00",
      "createdTime": "22:28:21.4689105",
      "address": "bht hi neear shop",
      "location": "bht hi neear shop",
      "latitude": 0,
      "longitude": 0
    }
    * */

    private String shopId,shopName,shopNo,shopEmail,shopAddress,shopImg;
    private boolean IsApproved;

    public ShopModel(String shopId, String shopName, String shopNo, String shopEmail, String shopAddress, String shopImg, boolean isApproved) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.shopNo = shopNo;
        this.shopEmail = shopEmail;
        this.shopAddress = shopAddress;
        this.shopImg = shopImg;
        IsApproved = isApproved;
    }

    public ShopModel() {
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopNo() {
        return shopNo;
    }

    public void setShopNo(String shopNo) {
        this.shopNo = shopNo;
    }

    public String getShopEmail() {
        return shopEmail;
    }

    public void setShopEmail(String shopEmail) {
        this.shopEmail = shopEmail;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getShopImg() {
        return shopImg;
    }

    public void setShopImg(String shopImg) {
        this.shopImg = shopImg;
    }

    public boolean isApproved() {
        return IsApproved;
    }

    public void setApproved(boolean approved) {
        IsApproved = approved;
    }
}

