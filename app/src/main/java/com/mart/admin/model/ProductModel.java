package com.mart.admin.model;

/**
 * Created by WeMartDevelopers .
 */
public class  ProductModel {
    private String prod_id,prod_image,prod_name,prod_catg,prod_rating,prod_price,prod_unit,approval,createdDate,createdTime;

    public ProductModel() {
    }

    public ProductModel(String prod_id, String prod_image, String prod_name, String prod_catg, String prod_rating, String prod_price, String prod_unit, String approval) {
        this.prod_id = prod_id;
        this.prod_image = prod_image;
        this.prod_name = prod_name;
        this.prod_catg = prod_catg;
        this.prod_rating = prod_rating;
        this.prod_price = prod_price;
        this.prod_unit = prod_unit;
        this.approval = approval;
    }

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getProd_price() {
        return prod_price;
    }

    public void setProd_price(String prod_price) {
        this.prod_price = prod_price;
    }

    public String getProd_unit() {
        return prod_unit;
    }

    public void setProd_unit(String prod_unit) {
        this.prod_unit = prod_unit;
    }

    public String getProd_id() {
        return prod_id;
    }

    public void setProd_id(String prod_id) {
        this.prod_id = prod_id;
    }

    public String getProd_image() {
        return prod_image;
    }

    public void setProd_image(String prod_image) {
        this.prod_image = prod_image;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public String getProd_catg() {
        return prod_catg;
    }

    public void setProd_catg(String prod_catg) {
        this.prod_catg = prod_catg;
    }

    public String getProd_rating() {
        return prod_rating;
    }

    public void setProd_rating(String prod_rating) {
        this.prod_rating = prod_rating;
    }
}
