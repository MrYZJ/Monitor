package leavesc.hello.monitor.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.recyclerview.extensions.AsyncListDiffer;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import leavesc.hello.monitor.R;
import leavesc.hello.monitor.db.entity.HttpInformation;
import leavesc.hello.monitor.utils.FormatUtils;

/**
 * 作者：leavesC
 * 时间：2019/2/9 13:55
 * 描述：
 * GitHub：https://github.com/leavesC
 * Blog：https://www.jianshu.com/u/9df45b87cfdf
 */
public class MonitorAdapter extends RecyclerView.Adapter<MonitorAdapter.MonitorViewHolder> {

    private final int colorSuccess;
    private final int colorRequested;
    private final int colorError;
    private final int color300;
    private final int color400;
    private final int color500;

    private OnClickListener clickListener;

    private AsyncListDiffer<HttpInformation> asyncListDiffer;

    public MonitorAdapter(Context context) {
        asyncListDiffer = new AsyncListDiffer<>(this, new DiffUtilItemCallback());
        colorSuccess = ContextCompat.getColor(context, R.color.monitor_status_success);
        colorRequested = ContextCompat.getColor(context, R.color.monitor_status_requested);
        colorError = ContextCompat.getColor(context, R.color.monitor_status_error);
        color300 = ContextCompat.getColor(context, R.color.monitor_status_300);
        color400 = ContextCompat.getColor(context, R.color.monitor_status_400);
        color500 = ContextCompat.getColor(context, R.color.monitor_status_500);
    }

    public void setData(List<HttpInformation> dataList) {
        asyncListDiffer.submitList(dataList);
    }

    public void clear() {
        asyncListDiffer.submitList(null);
    }

    @NonNull
    @Override
    public MonitorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MonitorAdapter.MonitorViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull final MonitorViewHolder holder, int position) {
        final HttpInformation httpInformation = asyncListDiffer.getCurrentList().get(position);
        holder.tv_id.setText(String.valueOf(httpInformation.getId()));
        holder.tv_path.setText(String.format("%s  %s", httpInformation.getMethod(), httpInformation.getPath()));
        holder.tv_host.setText(httpInformation.getHost());
        holder.tv_requestDate.setText(FormatUtils.getDateFormatShort(httpInformation.getRequestDate()));
        holder.iv_ssl.setVisibility(httpInformation.isSsl() ? View.VISIBLE : View.GONE);
        if (httpInformation.getStatus() == HttpInformation.Status.Complete) {
            holder.tv_code.setText(String.valueOf(httpInformation.getResponseCode()));
            holder.tv_duration.setText(httpInformation.getDurationFormat());
            holder.tv_size.setText(httpInformation.getTotalSizeString());
        } else {
            if (httpInformation.getStatus() == HttpInformation.Status.Failed) {
                holder.tv_code.setText("!!!");
            } else {
                holder.tv_code.setText(null);
            }
            holder.tv_duration.setText(null);
            holder.tv_size.setText(null);
        }
        setStatusColor(holder, httpInformation);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onClick(holder.getAdapterPosition(), httpInformation);
                }
            }
        });
    }

    private void setStatusColor(MonitorViewHolder holder, HttpInformation httpInformation) {
        int color;
        if (httpInformation.getStatus() == HttpInformation.Status.Failed) {
            color = colorError;
        } else if (httpInformation.getStatus() == HttpInformation.Status.Requested) {
            color = colorRequested;
        } else if (httpInformation.getResponseCode() >= 500) {
            color = color500;
        } else if (httpInformation.getResponseCode() >= 400) {
            color = color400;
        } else if (httpInformation.getResponseCode() >= 300) {
            color = color300;
        } else {
            color = colorSuccess;
        }
        holder.tv_code.setTextColor(color);
        holder.tv_path.setTextColor(color);
    }

    @Override
    public int getItemCount() {
        return asyncListDiffer.getCurrentList().size();
    }

    public void setClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    static class MonitorViewHolder extends RecyclerView.ViewHolder {

        public final View view;
        public final TextView tv_id;
        public final TextView tv_code;
        public final TextView tv_path;
        public final TextView tv_host;
        public final ImageView iv_ssl;
        public final TextView tv_requestDate;
        public final TextView tv_duration;
        public final TextView tv_size;

        MonitorViewHolder(@NonNull ViewGroup viewGroup) {
            super(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_monitor, viewGroup, false));
            this.view = itemView;
            tv_id = view.findViewById(R.id.tv_id);
            tv_code = view.findViewById(R.id.tv_code);
            tv_path = view.findViewById(R.id.tv_path);
            tv_host = view.findViewById(R.id.tv_host);
            iv_ssl = view.findViewById(R.id.iv_ssl);
            tv_requestDate = view.findViewById(R.id.tv_requestDate);
            tv_duration = view.findViewById(R.id.tv_duration);
            tv_size = view.findViewById(R.id.tv_size);
        }

    }

    private class DiffUtilItemCallback extends DiffUtil.ItemCallback<HttpInformation> {

        @Override
        public boolean areItemsTheSame(@NonNull HttpInformation oldItem, @NonNull HttpInformation newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull HttpInformation oldItem, @NonNull HttpInformation newItem) {
            return oldItem.equals(newItem);
        }

        @Nullable
        @Override
        public Object getChangePayload(@NonNull HttpInformation oldItem, @NonNull HttpInformation newItem) {
            return super.getChangePayload(oldItem, newItem);
        }

    }

    public interface OnClickListener {
        void onClick(int position, HttpInformation model);
    }

}