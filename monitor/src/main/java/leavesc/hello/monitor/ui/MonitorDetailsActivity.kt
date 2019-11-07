package leavesc.hello.monitor.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import leavesc.hello.monitor.R
import leavesc.hello.monitor.db.HttpInformation
import leavesc.hello.monitor.utils.FormatUtils
import leavesc.hello.monitor.viewmodel.MonitorViewModel
import java.util.*

/**
 * 作者：leavesC
 * 时间：2019/11/7 15:19
 * 描述：
 */
class MonitorDetailsActivity : AppCompatActivity() {

    companion object {

        private const val KEY_ID = "keyId"

        fun navTo(context: Context, id: Long) {
            val intent = Intent(context, MonitorDetailsActivity::class.java)
            intent.putExtra(KEY_ID, id)
            context.startActivity(intent)
        }
    }

    private lateinit var monitorViewModel: MonitorViewModel

    private var tvTitle: TextView? = null

    private var httpInformation: HttpInformation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monitor_details)
        initView()
        initViewModel()
        val id = intent.getLongExtra(KEY_ID, 0)
        monitorViewModel.queryRecordById(id)
        monitorViewModel.recordLiveData?.observe(this, Observer { httpInformation ->
            this@MonitorDetailsActivity.httpInformation = httpInformation
            if (httpInformation != null) {
                tvTitle!!.text = String.format("%s  %s", httpInformation.method, httpInformation.path)
            } else {
                tvTitle!!.text = null
            }
        })
    }

    private fun initView() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        tvTitle = findViewById(R.id.tvToolbarTitle)
        val viewPager: ViewPager = findViewById(R.id.viewPager)
        val fragmentPagerAdapter = PagerAdapter(supportFragmentManager)
        fragmentPagerAdapter.addFragment(MonitorOverviewFragment.newInstance(), "overview")
        fragmentPagerAdapter.addFragment(MonitorPayloadFragment.newInstanceRequest(), "request")
        fragmentPagerAdapter.addFragment(MonitorPayloadFragment.newInstanceResponse(), "response")
        viewPager.adapter = fragmentPagerAdapter
        val tabLayout: TabLayout = findViewById(R.id.tabs)
        tabLayout.setupWithViewPager(viewPager)
    }

    private class PagerAdapter constructor(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        private val fragmentList = ArrayList<Fragment>()

        private val fragmentTitleList = ArrayList<String>()

        internal fun addFragment(fragment: Fragment, title: String) {
            fragmentList.add(fragment)
            fragmentTitleList.add(title)
        }

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return fragmentTitleList[position]
        }
    }

    private fun initViewModel() {
        monitorViewModel = ViewModelProviders.of(this).get(MonitorViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_share, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.share) {
            if (httpInformation != null) {
                share(FormatUtils.getShareText(httpInformation!!))
            }
        } else if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
    }

    private fun share(content: String) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, content)
        sendIntent.type = "text/plain"
        startActivity(Intent.createChooser(sendIntent, null))
    }

}