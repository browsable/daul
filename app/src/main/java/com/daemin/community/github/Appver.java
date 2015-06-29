package com.daemin.community.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by hernia on 2015-06-20.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Appver {

   @JsonProperty("product")
    private List<String> product;
   @JsonProperty("success")
    private String success;


    /*public static class Product{
        @JsonProperty("_id")
        private String _id;
        @JsonProperty("appversion")
        private String appversion;
       @JsonProperty("dbversion")
        private String dbversion;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getAppversion() {
            return appversion;
        }

        public void setAppversion(String appversion) {
            this.appversion = appversion;
        }

        public String getDbversion() {
            return dbversion;
        }

        public void setDbversion(String dbversion) {
            this.dbversion = dbversion;
        }

        @Override
        public String toString() {
            return "Product{" +
                    "_id='" + _id + '\'' +
                    ", appversion='" + appversion + '\'' +
                    ", dbversion='" + dbversion + '\'' +
                    '}';
        }
    }*/
    public List<String> getProduct() {
        return product;
    }

    public void setProduct(List<String> product) {
        this.product = product;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        //b.append("product :" + getProduct().toString() + "\n");
        b.append("success : " + getSuccess() + "\n");
        return b.toString();
    }

}