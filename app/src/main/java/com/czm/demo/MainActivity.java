package com.czm.demo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.czm.xcrefreshlayout.R;
import com.czm.xcrefreshlayout.XCRefreshLayout;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private XCRefreshLayout mRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        mRefreshLayout = (XCRefreshLayout) findViewById(R.id.refresh_layout);
        mListView = (ListView) findViewById(R.id.listview);
        String[] strs =  new String[20];
        for(int i = 0; i < strs.length; i++){
            strs[i] = "ListView Item "+ i;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strs);
        mListView.setAdapter(adapter);

        mRefreshLayout.setOnRefreshListener(new XCRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefreshFinish() {
                Log.v("czm", "onRefreshFinish");

            }

            @Override
            public void onRefreshing() {
                Log.v("czm","onRefreshing");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.refreshComplete();
                    }
                }, 1000);
            }
        });
    }

}
