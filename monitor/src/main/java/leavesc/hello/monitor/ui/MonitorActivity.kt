package leavesc.hello.monitor.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import leavesc.hello.monitor.R
import leavesc.hello.monitor.adapter.MonitorAdapter
import leavesc.hello.monitor.adapter.OnClickListener
import leavesc.hello.monitor.db.HttpInformation
import leavesc.hello.monitor.viewmodel.MonitorViewModel

/**
 * 作者：leavesC
 * 时间：2019/11/7 15:20
 * 描述：
 */
class MonitorActivity : AppCompatActivity() {

    private lateinit var monitorViewModel: MonitorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monitor)
        initViewModel()
        initView()
        val adapter = MonitorAdapter(this)
        adapter.clickListener = object : OnClickListener {
            override fun onClick(position: Int, model: HttpInformation) {
                MonitorDetailsActivity.navTo(this@MonitorActivity, model.id)
            }
        }
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        monitorViewModel.allRecordLiveData.observe(this, Observer { list ->
            adapter.setData(list)
        })
    }

    private fun initViewModel() {
        monitorViewModel = ViewModelProviders.of(this).get(MonitorViewModel::class.java)
    }

    private fun initView() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val tvTitle: TextView = findViewById(R.id.tvToolbarTitle)
        tvTitle.text = "Monitor"
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_monitor, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.clear) {
            monitorViewModel.clearAllCache()
            monitorViewModel.clearNotification()
        } else if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
    }

}