package com.labelwall.mywall.main.user.profile.location.bean;

/**
 * 介绍：索引类的汉语拼音的接口
 * 作者：zhangxutong
 * 邮箱：mcxtzhang@163.com
 * CSDN：http://blog.csdn.net/zxt0601
 * 时间： 16/09/04.
 */

public abstract class BaseIndexPinyinBean extends
        BaseIndexTagBean implements IIndexTargetInterface {
    private String mPyCity;//城市的拼音

    public String getPyCity() {
        return mPyCity;
    }

    public void setPyCity(String pyCity) {
        this.mPyCity = pyCity;
    }
}
