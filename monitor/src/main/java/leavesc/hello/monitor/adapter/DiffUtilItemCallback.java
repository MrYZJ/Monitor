package leavesc.hello.monitor.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import leavesc.hello.monitor.db.entity.MonitorHttpInformation;

/**
 * 作者：leavesC
 * 时间：2019/2/9 14:26
 * 描述：
 * GitHub：https://github.com/leavesC
 * Blog：https://www.jianshu.com/u/9df45b87cfdf
 */
public class DiffUtilItemCallback extends DiffUtil.ItemCallback<MonitorHttpInformation> {

    @Override
    public boolean areItemsTheSame(@NonNull MonitorHttpInformation oldItem, @NonNull MonitorHttpInformation newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull MonitorHttpInformation oldItem, @NonNull MonitorHttpInformation newItem) {
        return oldItem.equals(newItem);
    }

    @Nullable
    @Override
    public Object getChangePayload(@NonNull MonitorHttpInformation oldItem, @NonNull MonitorHttpInformation newItem) {
        return super.getChangePayload(oldItem, newItem);
    }

}