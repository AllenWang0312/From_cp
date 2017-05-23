package color.measurement.com.from_cp20.module.other.tools;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.base.BaseActivity;

public class ToolsActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.bvb_tools) BottomNavigationBar mBvbTools;
    @BindView(R.id.fl_tools) FrameLayout mFlTools;

    private ColorTransform mFindFragment;
    private NullPager mFollowFragment, mCenterFragment;

    private FragmentTransaction ft;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);

        ButterKnife.bind(this);
        mToolbar.setTitle("工具");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        BadgeItem badgeItem = new BadgeItem().setBackgroundColor(Color.RED).setText("99").setHideOnSelect(true);
        BadgeItem badgeItem = new BadgeItem().setBorderWidth(1)
                .setBackgroundColorResource(R.color.colorAccent)
                .setText("2")
                .setHideOnSelect(true);

        mBvbTools.setAutoHideEnabled(true);
        mBvbTools.setMode(
                BottomNavigationBar.MODE_DEFAULT
//                BottomNavigationBar.MODE_FIXED
//                BottomNavigationBar.MODE_SHIFTING
        );
        mBvbTools.setBackgroundStyle(
//                BottomNavigationBar.BACKGROUND_STYLE_RIPPLE
                BottomNavigationBar.BACKGROUND_STYLE_DEFAULT
//                BottomNavigationBar.BACKGROUND_STYLE_STATIC
        );

        mBvbTools.addItem(new BottomNavigationItem(R.drawable.ic_color_lens_white_24dp, "色值转换").setActiveColorResource(R.color.accent_orange).setBadgeItem(badgeItem))
                .addItem(new BottomNavigationItem(R.drawable.ic_picture_as_pdf_white_24dp, "文件管理").setActiveColorResource(R.color.accent_blue))
                .addItem(new BottomNavigationItem(R.drawable.ic_insert_chart_white_24dp, "图表管理").setActiveColorResource(R.color.accent_red))
                .initialise();
        mBvbTools.setTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(int position) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (position) {
            case 0:
                if (null == mFindFragment) {
                    mFindFragment = new ColorTransform(this);
                }
                ft.replace(R.id.fl_tools, mFindFragment);
                break;
            case 1:
                if (null == mFollowFragment) {
                    mFollowFragment = new NullPager(this);
                }
                ft.replace(R.id.fl_tools, mFollowFragment);
                break;
            case 2:
                if (null == mCenterFragment) {
                    mCenterFragment = new NullPager(this);
                }
                ft.replace(R.id.fl_tools, mCenterFragment);
                break;
            default:
                if (null == mFindFragment) {
                    mFindFragment = new ColorTransform(this);
                }
                ft.add(R.id.fl_tools, mFindFragment);
                break;
        }
        ft.commit();
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return true;
    }
}
