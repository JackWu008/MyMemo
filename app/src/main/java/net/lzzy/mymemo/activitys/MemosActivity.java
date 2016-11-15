package net.lzzy.mymemo.activitys;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import net.lzzy.mymemo.dataPersist.DbConstants;
import net.lzzy.mymemo.fragments.MemosFragment;

/**
 * Created by Administrator on 2016/9/20.
 */
public class MemosActivity extends BaseActivity implements MemosFragment.MemosSelectListener {
    public static final String IS_NEW = "isNew";
    private long exitTime = 0;
    @Override
    protected Fragment getFragment() {
        return new MemosFragment();
    }


    @Override
    public void onMemoItemClick(String id) {
        Intent intent = new Intent(MemosActivity.this, PagerActivity.class);
        intent.putExtra(DbConstants.MEMO_ID, id);
        startActivity(intent);
    }

    @Override
    public void onMemoClick() {
        Intent intent = new Intent(MemosActivity.this, MemoActivity.class);
        intent.putExtra(IS_NEW, true);
        startActivity(intent);

    }



    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(MemosActivity.this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
            System.exit(0);
        }
    }
}
