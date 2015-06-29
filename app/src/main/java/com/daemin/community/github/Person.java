package com.daemin.community.github;

/**
 * Created by hernia on 2015-06-20.
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Person {

    @JsonProperty("login")
    private String login;
    @JsonProperty("id")
    private String id;
    @JsonProperty("avatar_url")
    private String avatarUrl;
    @JsonProperty("url")
    private String url;
    @JsonProperty("type")
    private String type;
    @JsonProperty("name")
    private String name;
    @JsonProperty("email")
    private String email;
    @JsonProperty("company")
    private String company;
    @JsonProperty("created_at")
    private String createdDate;
    @JsonProperty("updated_at")
    private String updatedDate;

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("ID : " + id + "\n");
        b.append("Name : " + getName() + "\n");
        b.append("Login : " + getLogin() + "\n");
        b.append("Email : " + getEmail() + "\n");
        b.append("Company : " + getCompany() + "\n");
        b.append("Created date : " + getCreatedDate() + "\n");
        b.append("Updated date : " + getUpdatedDate() + "\n");
        return b.toString();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }
}