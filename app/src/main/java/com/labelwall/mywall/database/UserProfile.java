package com.labelwall.mywall.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

/**
 * Created by Administrator on 2018-01-05.
 * 登录用户的实体，对于数据库中的表
 */
//表名是user_profile
@Entity(nameInDb = "user_profile")
public class UserProfile {

    @Id
    private long id;

    private String username;

    private String password;

    private String head;

    private String email;

    private String phone;

    private Date schoolDate;

    private String schoolName;

    private String locationProvince;

    private String locationCity;

    private String locationCounty;

    private Integer provinceId;

    private Integer cityId;

    private Integer countyId;

    private Integer schoolId;

    private String createTimeStr;

    private String updateTimeStr;

    private Integer gender;

    private String birthday;

    @Generated(hash = 77367964)
    public UserProfile(long id, String username, String password, String head,
            String email, String phone, Date schoolDate, String schoolName,
            String locationProvince, String locationCity, String locationCounty,
            Integer provinceId, Integer cityId, Integer countyId, Integer schoolId,
            String createTimeStr, String updateTimeStr, Integer gender,
            String birthday) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.head = head;
        this.email = email;
        this.phone = phone;
        this.schoolDate = schoolDate;
        this.schoolName = schoolName;
        this.locationProvince = locationProvince;
        this.locationCity = locationCity;
        this.locationCounty = locationCounty;
        this.provinceId = provinceId;
        this.cityId = cityId;
        this.countyId = countyId;
        this.schoolId = schoolId;
        this.createTimeStr = createTimeStr;
        this.updateTimeStr = updateTimeStr;
        this.gender = gender;
        this.birthday = birthday;
    }

    @Generated(hash = 968487393)
    public UserProfile() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHead() {
        return this.head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getSchoolDate() {
        return this.schoolDate;
    }

    public void setSchoolDate(Date schoolDate) {
        this.schoolDate = schoolDate;
    }

    public String getSchoolName() {
        return this.schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getLocationProvince() {
        return this.locationProvince;
    }

    public void setLocationProvince(String locationProvince) {
        this.locationProvince = locationProvince;
    }

    public String getLocationCity() {
        return this.locationCity;
    }

    public void setLocationCity(String locationCity) {
        this.locationCity = locationCity;
    }

    public String getLocationCounty() {
        return this.locationCounty;
    }

    public void setLocationCounty(String locationCounty) {
        this.locationCounty = locationCounty;
    }

    public Integer getProvinceId() {
        return this.provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public Integer getCityId() {
        return this.cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getCountyId() {
        return this.countyId;
    }

    public void setCountyId(Integer countyId) {
        this.countyId = countyId;
    }

    public Integer getSchoolId() {
        return this.schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public String getCreateTimeStr() {
        return this.createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public String getUpdateTimeStr() {
        return this.updateTimeStr;
    }

    public void setUpdateTimeStr(String updateTimeStr) {
        this.updateTimeStr = updateTimeStr;
    }

    public Integer getGender() {
        return this.gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return this.birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

}
