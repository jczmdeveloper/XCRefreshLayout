# XCRefreshLayout
This is a custom pull-to-refresh layout project,and the progress view animation can be customed
This is a RefreshLayout that is a custum Pull-To—Refresh layout ，and it can use a custum progress animation view.
一个下拉刷新的控件布局，支持自定义动画进度，使用起来和FrameLayout一样。

效果图如下：

![image](https://github.com/jczmdeveloper/XCRefreshLayout/blob/master/screenshots/01.gif)  

使用方法示例：


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
