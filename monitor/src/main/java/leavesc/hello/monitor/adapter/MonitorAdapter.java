package leavesc.hello.monitor.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import leavesc.hello.monitor.R;
import leavesc.hello.monitor.db.entity.MonitorHttpInformation;

/**
 * 作者：leavesC
 * 时间：2019/2/9 13:55
 * 描述：
 * GitHub：https://github.com/leavesC
 * Blog：https://www.jianshu.com/u/9df45b87cfdf
 */
public class MonitorAdapter extends RecyclerView.Adapter<MonitorAdapter.MonitorViewHolder> {

    private List<MonitorHttpInformation> dataList;

    private final int colorDefault;
    private final int colorRequested;
    private final int colorError;
    private final int color500;
    private final int color400;
    private final int color300;

    public interface OnClickListener {
        void onClick(int position, MonitorHttpInformation model);
    }

    private OnClickListener clickListener;

    public MonitorAdapter(Context context, List<MonitorHttpInformation> dataList) {
        this.dataList = dataList;
        colorDefault = ContextCompat.getColor(context, R.color.chuck_status_default);
        colorRequested = ContextCompat.getColor(context, R.color.chuck_status_requested);
        colorError = ContextCompat.getColor(context, R.color.chuck_status_error);
        color500 = ContextCompat.getColor(context, R.color.chuck_status_500);
        color400 = ContextCompat.getColor(context, R.color.chuck_status_400);
        color300 = ContextCompat.getColor(context, R.color.chuck_status_300);
    }

    @NonNull
    @Override
    public MonitorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MonitorAdapter.MonitorViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull final MonitorViewHolder holder, int position) {
        final MonitorHttpInformation transaction = dataList.get(position);
        holder.path.setText(transaction.getMethod() + " " + transaction.getPath());
        holder.host.setText(transaction.getHost());
        holder.start.setText(transaction.getRequestDateFormat());
        holder.ssl.setVisibility(transaction.isSsl() ? View.VISIBLE : View.GONE);
        if (transaction.getStatus() == MonitorHttpInformation.Status.Complete) {
            holder.code.setText(String.valueOf(transaction.getResponseCode()));
            holder.duration.setText(String.valueOf(transaction.getDuration()));
            holder.size.setText(transaction.getTotalSizeString());
        } else {
            holder.code.setText(null);
            holder.duration.setText(null);
            holder.size.setText(null);
        }
        if (transaction.getStatus() == MonitorHttpInformation.Status.Failed) {
            holder.code.setText("!!!");
        }
        setStatusColor(holder, transaction);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onClick(holder.getAdapterPosition(), transaction);
                }
            }
        });
    }

    private void setStatusColor(MonitorViewHolder holder, MonitorHttpInformation transaction) {
        int color;
        if (transaction.getStatus() == MonitorHttpInformation.Status.Failed) {
            color = colorError;
        } else if (transaction.getStatus() == MonitorHttpInformation.Status.Requested) {
            color = colorRequested;
        } else if (transaction.getResponseCode() >= 500) {
            color = color500;
        } else if (transaction.getResponseCode() >= 400) {
            color = color400;
        } else if (transaction.getResponseCode() >= 300) {
            color = color300;
        } else {
            color = colorDefault;
        }
        holder.code.setTextColor(color);
        holder.path.setTextColor(color);
    }

    @Override
    public int getItemCount() {
        if (dataList == null) {
            return 0;
        }
        return dataList.size();
    }

    public void setClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    static class MonitorViewHolder extends RecyclerView.ViewHolder {

        public final View view;
        public final TextView code;
        public final TextView path;
        public final TextView host;
        public final TextView start;
        public final TextView duration;
        public final TextView size;
        public final ImageView ssl;

        MonitorViewHolder(@NonNull ViewGroup viewGroup) {
            super(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chuck_list_item_transaction, viewGroup, false));
            this.view = itemView;
            code = (TextView) view.findViewById(R.id.code);
            path = (TextView) view.findViewById(R.id.path);
            host = (TextView) view.findViewById(R.id.host);
            start = (TextView) view.findViewById(R.id.start);
            duration = (TextView) view.findViewById(R.id.duration);
            size = (TextView) view.findViewById(R.id.size);
            ssl = (ImageView) view.findViewById(R.id.ssl);
        }

    }

}
