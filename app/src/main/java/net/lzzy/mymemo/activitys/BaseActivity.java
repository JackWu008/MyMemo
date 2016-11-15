package net.lzzy.mymemo.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import net.lzzy.mymemo.R;


/**
 * Created by Administrator on 2016/9/19.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragment_container_f);
        if (fragment == null) {
            fragment = getFragment();
            manager.beginTransaction().add(R.id.fragment_container_f, fragment).commit();
        }
    }

    protected abstract Fragment getFragment();
}
