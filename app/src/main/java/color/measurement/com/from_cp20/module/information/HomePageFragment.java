package color.measurement.com.from_cp20.module.information;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.SpaceDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import color.measurement.com.from_cp20.module.information.javaBean.NewsGson;
import color.measurement.com.from_cp20.module.information.javaBean.NewslistBean;
import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.base.BaseFragment;
import color.measurement.com.from_cp20.common.backgroundcreater.RandomBgCreater;
import color.measurement.com.from_cp20.util.blankj.SizeUtils;
import es.dmoral.toasty.Toasty;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static color.measurement.com.from_cp20.module.App.tianxing_apiKey;

/**
 * Created by wpc on 2017/2/9.
 */
@SuppressLint("ValidFragment")
public class HomePageFragment extends BaseFragment {

    @BindView(R.id.recyc_homepage) EasyRecyclerView mRecyclerView;
    NewsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homapage_frg_layout, container, false);
        ButterKnife.bind(this, view);

        adapter = new NewsAdapter(getActivity(), new RandomBgCreater(null, true, true));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        SpaceDecoration itemDecoration = new SpaceDecoration((int) SizeUtils.dp2px(8));
        itemDecoration.setPaddingEdgeSide(true);
        itemDecoration.setPaddingStart(true);
        itemDecoration.setPaddingHeaderFooter(false);
        mRecyclerView.addItemDecoration(itemDecoration);
        adapter.setMore(R.layout.view_more, new RecyclerArrayAdapter.OnMoreListener() {
            @Override
            public void onMoreShow() {

            }

            @Override
            public void onMoreClick() {

            }
        });
        mRecyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.clear();
                        page = 0;
                        getData();
                    }
                }, 1000);
            }
        });
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ArrayList<String> data = new ArrayList<String>();
                data.add(adapter.getAllData().get(position).getPicUrl());
                data.add(adapter.getAllData().get(position).getUrl());
                Intent intent = new Intent(getActivity(), NewsDetiaslActivity.class);
                //用Bundle携带数据
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("data", data);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
//        mRecyclerView.addHeaderView();

        return view;
    }

    int page = 1;

    void getData() {
        Log.i("page", page + "");
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://api.tianapi.com/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        ApiService apiManger = retrofit.create(ApiService.class);
        apiManger.getNewsData(tianxing_apiKey, "10", page)
                .subscribeOn(Schedulers.io()).map(new Func1<NewsGson, List<NewslistBean>>() {
            @Override
            public List<NewslistBean> call(NewsGson newsGson) {
                return newsGson.getNewslist();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<NewslistBean>>() {
                    @Override
                    public void onNext(List<NewslistBean> newsList) {
                        adapter.addAll(newsList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toasty.error(getActivity(), "网络连接失败").show();
                    }

                    @Override
                    public void onCompleted() {

                    }
                });

        page += 1;
    }
}
