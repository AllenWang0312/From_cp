package color.measurement.com.from_cp20.common.widget.table;//package color.measurement.com.from_cp20.common.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.common.util.UiUtils;
import color.measurement.com.from_cp20.module.been.interfaze.CompareableData;

/**
 * Created by wpc on 2017/5/3.
 */
@SuppressLint("ValidFragment")
public class TableFragment extends Fragment {
    Context mContext;

    @BindView(R.id.cb_all_table) CheckBox mCbAllTable;
    @BindView(R.id.recyc_names_table) RecyclerView mRecycNamesTable;
    @BindView(R.id.ll_titles_table) LinearLayout mLlTitlesTable;
    @BindView(R.id.recyc_content_table) RecyclerView mRecycContentTable;
    NameAdapter mNameAdapter;
    ContentAdapter mContentAdapter;

    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_table, container, false);
        unbinder = ButterKnife.bind(this, view);

        mRecycNamesTable.setLayoutManager(new LinearLayoutManager(mContext));
        mRecycContentTable.setLayoutManager(new LinearLayoutManager(mContext));

        return view;
    }

    public void setCheckBoxCheckedListener(CompoundButton.OnCheckedChangeListener checkBoxCheckedListener) {
        mCbAllTable.setOnCheckedChangeListener(checkBoxCheckedListener);
    }

    public void setNames(ArrayList<String> names) {
        mNameAdapter = new NameAdapter(mContext, names);
        mRecycNamesTable.setAdapter(mNameAdapter);
    }

    public void setContent(ArrayList<String> titles, ArrayList<CompareableData> values) {
        setTitles(titles);
        mContentAdapter = new ContentAdapter(mContext, titles, values);
        mRecycContentTable.setAdapter(mContentAdapter);
    }
    private void setTitles(ArrayList<String> titles) {
        mLlTitlesTable.removeAllViews();
        for (String str : titles) {
            UiUtils.addTextViewToLinearLayout(mContext, mLlTitlesTable, str, null);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public NameAdapter getNameAdapter() {
        return mNameAdapter;
    }

    public void setNameAdapter(NameAdapter nameAdapter) {
        mNameAdapter = nameAdapter;
    }

    public ContentAdapter getContentAdapter() {
        return mContentAdapter;
    }

    public void setContentAdapter(ContentAdapter contentAdapter) {
        mContentAdapter = contentAdapter;
    }
}
