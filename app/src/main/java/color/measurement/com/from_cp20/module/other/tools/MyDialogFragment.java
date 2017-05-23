package color.measurement.com.from_cp20.module.other.tools;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.manager.db.MySqlConsts;
import color.measurement.com.from_cp20.module.App;
import color.measurement.com.from_cp20.util.utils.L;
import es.dmoral.toasty.Toasty;

/**
 * Created by cimcenter on 2017/5/8.
 */
@SuppressLint("ValidFragment")
public class MyDialogFragment extends DialogFragment {

    private String title;
    private String str;
    String biao;
    private EditText mEditText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return super.onCreateDialog(savedInstanceState);
    }

    public MyDialogFragment() {

    }

    public interface LoginInputListener
    {
        void onLoginInputComplete(String username);
    }
    public MyDialogFragment(String title, String str) {
        this.title = title;
        this.str = str;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_message, container);
        TextView textView = (TextView) view.findViewById(R.id.mess_dialog_title);
        textView.setText(title);
        mEditText = (EditText) view.findViewById(R.id.mess_edit_xiugai);
        mEditText.setText(str);
        mEditText.setSelection(mEditText.getText().length());
        Button quBt = (Button) view.findViewById(R.id.mess_dialog_quxiao);
        Button quedingBt = (Button) view.findViewById(R.id.mess_dialog_queding);
        quBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        if(title.equals("填写姓名")){
            biao = "name";
        }else if (title.equals("填写城市")){
            biao = "address";
        }else if (title.equals("填写签名")){
            biao = "sign";
        }else if (title.equals("填写生日")){
            biao = "birthday";
        }else if (title.equals("填写行业")){
            biao = "business";
        }else if (title.equals("修改密码")){
            biao = "password";
        }
        final String colm = biao;
        quedingBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mes = mEditText.toString().trim();
                ConnectivityManager connectivityManager = (ConnectivityManager)
                        getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager
                        .getActiveNetworkInfo();
                if (null != networkInfo && networkInfo.isConnected()) {
                    Log.e("main", "当前网络名称：" + networkInfo.getTypeName());
                } else {
                    Log.e("main", "没有可用网络");
                    mHandler.sendEmptyMessage(5);
                    return;
                }
                new Thread() {
                    @Override
                    public void run() {
                        Connection con = null;
                        String sql;
                        ResultSet rs = null;
                        Statement mStmt;
                        try {
                            Class.forName("com.mysql.jdbc.Driver");
                        } catch (ClassNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        try {
                            con = DriverManager
                                    .getConnection(
                                            "jdbc:mysql://rdsz4kl5za13u8d0m3i8spublic.mysql.rds.aliyuncs.com:3306/chnspec",
                                            "spec3205722251", "spec3205722251");
                            mStmt = con.createStatement();
                            if(mEditText.getText().toString().trim().equals("")){
                                mHandler.sendEmptyMessage(2);
                                return;
                            }
                            sql="update "+ MySqlConsts.user_table+" set "+colm+"='"+ mEditText.getText().toString().trim()+"'  where name ='"+ App.logged_user.getName()+"'";
                            L.e("sql=="+sql);
                            L.e("mStmt.execute(sql);=="+mStmt.executeUpdate(sql));
                            mHandler.sendEmptyMessage(0);
                            dismiss();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        super.run();
                    }
                }.start();
                L.e("queding");
            }
        });
        return view;
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    LoginInputListener listener = (LoginInputListener) getActivity();
                    listener.onLoginInputComplete(mEditText
                            .getText().toString());
                    break;
                case 2:
                    Toasty.error(getActivity(),"密码不能为空").show();
                    break;
                case 5:
                    Toasty.error(getActivity(),"当前无网络").show();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @Override
    public void onStart() {
        Window win = getDialog().getWindow();
        // 一定要设置Background，如果不设置，window属性设置无效
        win.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        WindowManager.LayoutParams params = win.getAttributes();
        params.gravity = Gravity.BOTTOM;
        // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        win.setAttributes(params);
        super.onStart();
    }
}
