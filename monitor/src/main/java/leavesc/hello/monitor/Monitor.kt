package leavesc.hello.monitor

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import leavesc.hello.monitor.db.HttpInformation
import leavesc.hello.monitor.db.MonitorHttpInformationDatabase
import leavesc.hello.monitor.holder.ContextHolder
import leavesc.hello.monitor.holder.NotificationHolder
import leavesc.hello.monitor.ui.MonitorActivity

/**
 * 作者：leavesC
 * 时间：2019/11/7 14:48
 * 描述：
 */
object Monitor {

    fun getLaunchIntent(context: Context): Intent {
        val intent = Intent(context, MonitorActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        return intent
    }

    fun clearNotification() {
        NotificationHolder.getInstance(ContextHolder.context).clearBuffer()
        NotificationHolder.getInstance(ContextHolder.context).dismiss()
    }

    fun showNotification(showNotification: Boolean) {
        NotificationHolder.getInstance(ContextHolder.context).showNotification(showNotification)
    }

    fun clearCache() {
        MonitorHttpInformationDatabase.INSTANCE.httpInformationDao.deleteAll()
    }

    fun queryAllRecord(limit: Int): LiveData<List<HttpInformation>> {
        return MonitorHttpInformationDatabase.INSTANCE.httpInformationDao.queryAllRecordObservable(limit)
    }

    fun queryAllRecord(): LiveData<List<HttpInformation>> {
        return MonitorHttpInformationDatabase.INSTANCE.httpInformationDao.queryAllRecordObservable()
    }

}