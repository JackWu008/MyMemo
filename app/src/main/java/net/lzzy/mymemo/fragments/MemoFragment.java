package net.lzzy.mymemo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.lzzy.mymemo.R;
import net.lzzy.mymemo.activitys.MemosActivity;
import net.lzzy.mymemo.dataPersist.DbConstants;
import net.lzzy.mymemo.models.Memo;
import net.lzzy.mymemo.models.MemoFactory;
import net.lzzy.mymemo.utils.Utils;


public class MemoFragment extends Fragment {

    private Memo memo;
    private EditText edt_content;
    private Button btn_save;
    private boolean isNew;
    private MemoFactory factory;

    public static MemoFragment newInstance(String memoId) {//静态工厂方法
        MemoFragment fragment = new MemoFragment();
        Bundle args = new Bundle();
        args.putString(DbConstants.MEMO_ID, memoId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        factory = MemoFactory.getInstance(getContext());
        isNew = getActivity().getIntent().getBooleanExtra(MemosActivity.IS_NEW, false);
        if (isNew) {
            memo = new Memo();
        } else {
            String id = getArguments().getString(DbConstants.MEMO_ID);
            memo = MemoFactory.getInstance(getContext()).getById(id);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memo, container, false);
        edt_content = (EditText) view.findViewById(R.id.fragment_memo_edt_content);
        btn_save = (Button) view.findViewById(R.id.fragment_memo_btn_save);
        initView();
        return view;
    }

    private void initView() {
        if (!isNew) {
            edt_content.setText(memo.getContent());
            edt_content.setSelection(edt_content.getText().length());//让光标后置
        }
        Utils.popupKeyboard(edt_content);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edt_content.getText().toString();
                if (content.trim().length() > 0) {
                    memo.setContent(content);
                    if (isNew)
                        factory.createMemo(memo);
                    else
                        factory.update(memo);
                    getActivity().finish();
                } else
                    Toast.makeText(getContext(), "未输入内容", Toast.LENGTH_SHORT).show();
            }
        });


    }

}
