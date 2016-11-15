package net.lzzy.mymemo.activitys;

import android.app.ActionBar;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import net.lzzy.mymemo.fragments.MemoFragment;


public class MemoActivity extends BaseActivity {

    @Override
    protected Fragment getFragment() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        return new MemoFragment();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
