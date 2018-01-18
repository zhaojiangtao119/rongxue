package com.labelwall.mywall.main.user.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;

import com.labelwall.mywall.R;
import com.labelwall.mywall.R2;
import com.labelwall.mywall.database.DataBaseManager;
import com.labelwall.mywall.database.UserProfile;
import com.labelwall.mywall.delegates.base.WallDelegate;
import com.labelwall.mywall.main.user.list.ListAdapter;
import com.labelwall.mywall.main.user.list.ListBean;
import com.labelwall.mywall.main.user.list.ListItemType;
import com.labelwall.mywall.util.storage.WallPreference;
import com.labelwall.mywall.util.storage.WallTagType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by Administrator on 2018-01-17.
 * 用户信息更新的delegate
 */

public class UserProfileDelegate extends WallDelegate {

    @BindView(R2.id.rv_user_profile)
    RecyclerView mRecyclerView = null;
    private UserProfile mUserprofile = null;
    private long mUserId = WallPreference.getCurrentUserId(WallTagType.CURRENT_USER_ID.name());
    private String[] mGender = new String[]{"男", "女", "保密"};

    @Override
    public Object setLayout() {
        return R.layout.delegate_user_profile;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        List<UserProfile> userProfileList = DataBaseManager
                .getInstance()
                .getDao()
                .queryRaw("where _id = ?", new String[]{String.valueOf(mUserId)});
        mUserprofile = userProfileList.get(0);
        uploadUserProfileItem();
    }

    private void uploadUserProfileItem() {
        final ListBean username = new ListBean.builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(1)
                .setText("姓名")
                .setValue(mUserprofile.getUsername())
                .build();
        final ListBean gender = new ListBean.builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(2)
                .setText("性别")
                .setValue(mGender[mUserprofile.getGender()])
                .build();
        final ListBean birthday = new ListBean.builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(3)
                .setText("生日")
                .setValue(mUserprofile.getBirthday())
                .build();
        final ListBean phone = new ListBean.builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(4)
                .setText("电话")
                .setValue(mUserprofile.getPhone())
                .build();
        final ListBean email = new ListBean.builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(5)
                .setText("邮箱")
                .setValue(mUserprofile.getEmail())
                .build();
        final ListBean location = new ListBean.builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(6)
                .setText("位置")
                .setValue(mUserprofile.getLocationProvince() + "-"
                        + mUserprofile.getLocationCity() + "-"
                        + mUserprofile.getLocationCounty())
                .build();
        final List<ListBean> data = new ArrayList<>();
        data.add(username);
        data.add(gender);
        data.add(birthday);
        data.add(phone);
        data.add(email);
        data.add(location);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        final ListAdapter adapter = new ListAdapter(data);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnItemTouchListener(new UserProfileClickListener(this));
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }
}
