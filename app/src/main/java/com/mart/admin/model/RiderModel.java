package com.mart.admin.model;

/**
 * Created by WeMartDevelopers .
 */
public class RiderModel {
    /*
    * "id": 1,
      "name": "New Shop",
      "password": "j10S8Rgc1wBMaftzsZ7DHjFYm0Y04bWP5GG0wXi1fgI=",
      "email": "newshop@gmail.com",
      "number": "03423519691",
      "token": "UmlkZXI6MDM0MjM1MTk2OTFCc3RpbmRhd3JsZA==",
      "isApproved": true,
      "img": "E:\\Visual Studio Project\\Wemart\\Wemart.Api\\wwwroot",
      "createdDate": "2020-10-04T00:00:00",
      "createdTime": "23:28:27.3122292"
    * */
    private String riderId,riderName,riderEmail,riderNo,riderImage;
    private boolean isApproved;

    public RiderModel(String riderId, String riderName, String riderEmail, String riderNo, String riderImage, boolean isApproved) {
        this.riderId = riderId;
        this.riderName = riderName;
        this.riderEmail = riderEmail;
        this.riderNo = riderNo;
        this.riderImage = riderImage;
        this.isApproved = isApproved;
    }

    public RiderModel() {
    }

    public String getRiderId() {
        return riderId;
    }

    public void setRiderId(String riderId) {
        this.riderId = riderId;
    }

    public String getRiderName() {
        return riderName;
    }

    public void setRiderName(String riderName) {
        this.riderName = riderName;
    }

    public String getRiderEmail() {
        return riderEmail;
    }

    public void setRiderEmail(String riderEmail) {
        this.riderEmail = riderEmail;
    }

    public String getRiderNo() {
        return riderNo;
    }

    public void setRiderNo(String riderNo) {
        this.riderNo = riderNo;
    }

    public String getRiderImage() {
        return riderImage;
    }

    public void setRiderImage(String riderImage) {
        this.riderImage = riderImage;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }
}
