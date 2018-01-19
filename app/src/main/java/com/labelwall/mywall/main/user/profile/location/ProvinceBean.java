package com.labelwall.mywall.main.user.profile.location;

import com.labelwall.mywall.main.user.profile.location.bean.BaseIndexPinyinBean;

/**
 * Created by zhangxutong .
 * Date: 16/08/28
 */

public class ProvinceBean extends BaseIndexPinyinBean {

    private String provinceName;//城市名字

    public ProvinceBean() {
    }

    public ProvinceBean(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    @Override
    public String getTarget() {
        return provinceName;
    }
}
