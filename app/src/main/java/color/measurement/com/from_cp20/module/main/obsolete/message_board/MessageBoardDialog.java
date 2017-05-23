package color.measurement.com.from_cp20.module.main.obsolete.message_board;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import color.measurement.com.from_cp20.R;
import color.measurement.com.from_cp20.common.imageloader.ImageLoaderProxy;

/**
 * Created by wpc on 2017/3/27.
 */
@SuppressLint("ValidFragment")
public class MessageBoardDialog extends DialogFragment {

    ArrayList<Message> messages;

    Context mContext;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mContext = getActivity();
        initHistroyMessage();

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View v = LayoutInflater.from(mContext).inflate(R.layout.message_board_layout, null);
        RecyclerView lv = (RecyclerView) v.findViewById(R.id.lv_message_board);
        lv.setLayoutManager(new LinearLayoutManager(mContext));
        lv.setAdapter(new MessageAdapter());
        builder.setView(v);
        return builder.create();
    }


    void initHistroyMessage() {
        messages = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            messages.add(new Message(123, "", "name", "content_test"));
            messages.add(new Message("hahahahha"));
        }

    }

    class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        ServiceViewHolder serHolder;
        UserViewHolder userHolder;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 0) {
                if (serHolder == null) {
                    serHolder = new ServiceViewHolder(LayoutInflater.from(mContext).inflate(R.layout.service_item_recyc_board, parent, false));
                }
                return serHolder;
            } else {
                if (userHolder == null) {
                    userHolder = new UserViewHolder(LayoutInflater.from(mContext).inflate(R.layout.user_item_recyc_board, parent, false));
                }
                return userHolder;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (getItemViewType(position) == 0) {
                ((ServiceViewHolder) holder).text.setText(messages.get(position).content);
            } else {
                ((UserViewHolder) holder).text.setText(messages.get(position).content);
                ImageLoaderProxy.getInstance().displayImage(messages.get(position).portraitURL, ((UserViewHolder) holder).img, R.mipmap.ic_launcher);

            }
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }

        @Override
        public int getItemViewType(int position) {
            return messages.get(position).userId == null ? 0 : 1;
        }

        class UserViewHolder extends RecyclerView.ViewHolder {
            TextView text;
            ImageView img;

            public UserViewHolder(View itemView) {
                super(itemView);
                text = (TextView) itemView.findViewById(R.id.text_item_user);
                img = (ImageView) itemView.findViewById(R.id.img_item_user);
            }
        }

        class ServiceViewHolder extends RecyclerView.ViewHolder {
            TextView text;

            public ServiceViewHolder(View itemView) {
                super(itemView);
                text = (TextView) itemView.findViewById(R.id.tv_service_item);
            }
        }
    }
}
