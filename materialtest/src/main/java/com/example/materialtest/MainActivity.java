package com.example.materialtest;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.materialtest.adapter.FruitAdapter;
import com.example.materialtest.bean.Fruit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private NavigationView navView;
    private FloatingActionButton fab;

    private Fruit[] fruits = {new Fruit("apple",R.drawable.apple),
            new Fruit("banana",R.drawable.banana),
            new Fruit("kiwifruit",R.drawable.kiwifruit),
            new Fruit("lemon",R.drawable.lemon),
            new Fruit("mango",R.drawable.mango),
            new Fruit("orange",R.drawable.orange),
            new Fruit("peach",R.drawable.peach),
            new Fruit("pitaya",R.drawable.pitaya),
            new Fruit("strawberry",R.drawable.strawberry)};

    private List<Fruit> fruitList = new ArrayList<>();
    private FruitAdapter fruitAdapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // 设置显示导航按钮
            actionBar.setDisplayHomeAsUpEnabled(true);
            // 设置导航按钮的图标
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        }
        // 设置默认选中项
        navView.setCheckedItem(R.id.nav_call);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // 关闭滑动菜单(写相应的逻辑操作)
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Snackbar并不是Toast的替代品，他们有着不同的运用场景
                Snackbar.make(v, "Data deleted", Snackbar.LENGTH_SHORT).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "Data restored", Toast.LENGTH_SHORT).show();
                    }
                }).show();
            }
        });
        initFruits();
        recyclerView = findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        fruitAdapter = new FruitAdapter(fruitList);
        recyclerView.setAdapter(fruitAdapter);
        // 处理下拉刷新
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        // 设置进度条的颜色（这里选择主题colorPrimary作为进度条的颜色）
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        // 设置下拉刷新的监听器
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 这里面执行具体的刷新逻辑
                refreshFruits();
            }
        });
    }

    private void refreshFruits() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 将线程切回主线程
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 重新生成数据
                        initFruits();
                        // 通知适配器数据发生变化
                        fruitAdapter.notifyDataSetChanged();
                        // 刷新结束，隐藏进度条
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    private void initFruits() {
        fruitList.clear();
        for (int i = 0;i<100;i++){
            Random random = new Random();
            int index = random.nextInt(fruits.length);
            fruitList.add(fruits[index]);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // HomeAsUp按钮的id永远都是android.R.id.home
            case android.R.id.home:
                // 将滑动菜单显示出来，方向根据系统语言判断
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.backup:
                Toast.makeText(this, "You clicked Backup", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(this, "You clicked Delete", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this, "You clicked Settings", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }
}
