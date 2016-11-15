package net.lzzy.mymemo.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import net.lzzy.mymemo.R;
import net.lzzy.mymemo.dataPersist.DbConstants;
import net.lzzy.mymemo.fragments.MemoFragment;
import net.lzzy.mymemo.models.Memo;
import net.lzzy.mymemo.models.MemoFactory;

import java.util.List;

public class PagerActivity extends AppCompatActivity {
    public static final String INTENT_PAGER_MEMO_ID = "intent_pager_memo_id";
    private ViewPager pager;
    private List<Memo> memos;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);
        Toolbar toolbar= (Toolbar) findViewById(R.id.activity_crime_pager_toolbar);
        setSupportActionBar(toolbar);
        memos = MemoFactory.getInstance(PagerActivity.this).getMemos();
        String id = getIntent().getStringExtra(DbConstants.MEMO_ID);
        FragmentManager fragmentManager = getSupportFragmentManager();
        pager = (ViewPager) findViewById(R.id.activity_crime_pager_view_pager);
        pager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Memo memo = memos.get(position);
                return MemoFragment.newInstance(memo.getId().toString());
            }

            @Override
            public int getCount() {
                return memos.size();
            }
        });


        for (int i = 0; i < memos.size(); i++) {
            if (memos.get(i).getId().toString().equals(id)) {
                pager.setCurrentItem(i);
                break;
            }
        }

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("posintion", position+"");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
