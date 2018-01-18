package com.labelwall.mywall;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.labelwall.mywall.util.net.RestClient;
import com.labelwall.mywall.util.net.callback.IError;
import com.labelwall.mywall.util.net.callback.IFailure;
import com.labelwall.mywall.util.net.callback.ISuccess;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<String> mList = new ArrayList<>();
    private MainAdapter mAdapter;
    private MainAdapter.OnItemClickListener onItemClickListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mList.add("1");
        mList.add("2");
        mList.add("3");
        mList.add("4");
        mList.add("5");
        mList.add("6");
        mList.add("7");
        mList.add("8");
        mList.add("9");
        mList.add("10");
        mList.add("11");
        mList.add("12");
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_test);
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));

        mAdapter = new MainAdapter(mList, this);
        /*mAdapter.setOnItemClickListener(new MainAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, "点击了第" + position + "条记录", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, final int position) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("确认删除嘛")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mAdapter.removeItem(position);
                            }
                        })
                        .show();
            }
        });*/
        mRecyclerView.setAdapter(mAdapter);
        //mRecyclerView.addItemDecoration(new MainDecoration(this));

    }
}
