package net.lzzy.mymemo.models;

import android.content.Context;

import net.lzzy.mymemo.dataPersist.DbConstants;
import net.lzzy.sqlitelib.SqlRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2016/9/20.
 */
public class MemoFactory {
    private List<Memo> memos;
    private static MemoFactory instance;
    private SqlRepository<Memo> repository;

    private MemoFactory(Context context) {
        repository = new SqlRepository<>(context, Memo.class, DbConstants.dbPackage);
        memos = repository.get();
    }

    public static MemoFactory getInstance(Context context) {//锁定多线程
        if (instance == null) {
        synchronized (MemoFactory.class) {
                if (instance == null)
                    instance = new MemoFactory(context);
            }
        }
        return instance;
    }



    public void replaceMemos() {
        memos.clear();
        memos.addAll(repository.get());
    }

    public List<Memo> findMemo(String kw) {
        try {
            return repository.getByKeyword(kw, new String[]{DbConstants.MEMO_CONTENT}, false);
        } catch (IllegalAccessException | InstantiationException e) {
            return new ArrayList<>();
        }
    }

    public Memo getById(String id) {
        for (Memo m : memos) {
            if (m.getId().toString().equals(id))
                return m;
        }
        return null;
    }

    public List<Memo> getMemos() {
        return memos;
    }

    public void createMemo(Memo memo) {
        memos.add(0, memo);
        repository.insert(memo);
    }

    public void deleteMemo(Memo memo) {
        try {
            repository.delete(memo.getId());
            memos.remove(memo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Memo memo) {
        repository.update(memo);
    }


    public void sort() {
        Collections.sort(memos, new Comparator<Memo>() {
            @Override
            public int compare(Memo lhs, Memo rhs) {
                long lTime = lhs.getUpdateTime().getTime();
                long rTime = rhs.getUpdateTime().getTime();
                if (lTime > rTime)
                    return -1;
                if (rTime > lTime)
                    return 1;
                return 0;
            }
        });
    }

}
