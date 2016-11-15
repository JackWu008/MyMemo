package net.lzzy.mymemo.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import net.lzzy.mymemo.R;
import net.lzzy.mymemo.models.Memo;
import net.lzzy.mymemo.models.MemoFactory;
import net.lzzy.mymemo.utils.Utils;
import net.lzzy.sqlitelib.GenericAdapter;
import net.lzzy.sqlitelib.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2016/9/20.
 */
public class MemosFragment extends Fragment {
    private GenericAdapter<Memo> adapter;
    private List<Memo> memos;
    private ListView lv_memos;
    private ImageButton imgBtn;
    private boolean allSelected;
    private MemosSelectListener listener;
    private SearchView search;
    private MemoFactory factory;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        factory = MemoFactory.getInstance(getContext());
        factory.sort();
        memos = factory.getMemos();
        adapter = new GenericAdapter<Memo>(getContext(), R.layout.memos_item, memos) {
            @Override
            public void populate(ViewHolder viewHolder, Memo memo) {
                viewHolder.setTextView(R.id.memos_item_tv_title, memo.getTitle())
                        .setTextView(R.id.memos_item_tv_time, Utils.DEFAULT_DATE_FORMAT.format(memo.getUpdateTime()));
            }
        };
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memos, container, false);
        lv_memos = (ListView) view.findViewById(R.id.fragment_memos_lv);
        imgBtn = (ImageButton) view.findViewById(R.id.fragment_memo_ib);
        initView();
        return view;
    }


    private void initView() {
        lv_memos.setAdapter(adapter);
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onMemoClick();

            }
        });
        lv_memos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              if (listener!=null)
                  listener.onMemoItemClick(adapter.getItem(position).getId().toString());

            }
        });

        lv_memos.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        lv_memos.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                mode.setTitle("已选中" + lv_memos.getCheckedItemCount() + "项");
                if (lv_memos.getCheckedItemCount() == memos.size()) {
                    mode.getMenu().getItem(0).setTitle("取消");
                    allSelected = true;
                } else {
                    mode.getMenu().getItem(0).setTitle("全选");
                    allSelected = false;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.menu_memos, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_memos_choose:
                        if (allSelected) {
                            for (int i = adapter.getCount() - 1; i >= 0; i--) {
                                lv_memos.setItemChecked(i, false);
                            }
                        } else {
                            for (int i = adapter.getCount() - 1; i >= 0; i--) {
                                lv_memos.setItemChecked(i, true);
                            }
                        }
                        break;
                    case R.id.menu_memos_delete:
                        if (lv_memos.getCheckedItemCount() > 0) {
                            new AlertDialog.Builder(getContext())
                                    .setTitle("提示")
                                    .setMessage("确定删除？")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            for (int i = adapter.getCount() - 1; i >= 0; i--) {
                                                if (lv_memos.isItemChecked(i)) {
                                                    factory.deleteMemo(adapter.getItem(i));
                                                }
                                            }
                                            mode.finish();
                                            adapter.notifyDataSetChanged();
                                        }
                                    })
                                    .setNegativeButton("取消", null).show();
                        } else {
                            Toast.makeText(getContext(), "未选中！", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.menu_memos_invertSelection:
                        for (int i = 0; i < adapter.getCount(); i++)
                            lv_memos.setItemChecked(i, !lv_memos.isItemChecked(i));
                        break;
                }
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });


    }

    public interface MemosSelectListener {
        void onMemoItemClick(String id);

        void onMemoClick();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_title, menu);


        search = (SearchView) menu.findItem(R.id.menu_title_search_edt).getActionView();
        search.setQueryHint("请输入关键字搜索");
        search.setFocusable(true);
        search.setIconified(true);
        search.requestFocusFromTouch();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                memos.clear();
                memos.addAll(factory.findMemo(newText));
                adapter.notifyDataSetChanged();
                return true;
            }
        });
        search.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memos.clear();
                imgBtn.setVisibility(View.INVISIBLE);
            }
        });
        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                imgBtn.setVisibility(View.VISIBLE);
                factory.replaceMemos();
                adapter.notifyDataSetChanged();
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (MemosSelectListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "必须实现MemosSelectListener");
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        listener = null;
    }
}
