package com.bitlove.fetlife.webapp.screen

import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import com.bitlove.fetlife.FetLifeApplication
import com.bitlove.fetlife.R
import com.bitlove.fetlife.view.screen.BaseActivity
import com.bitlove.fetlife.view.screen.component.MenuActivityComponent
import com.bitlove.fetlife.webapp.kotlin.getBooleanExtra
import com.bitlove.fetlife.webapp.kotlin.getStringArgument
import com.bitlove.fetlife.webapp.kotlin.getStringExtra
import com.bitlove.fetlife.webapp.navigation.WebAppNavigation
import kotlinx.android.synthetic.main.tool_bar_default.*

class FetLifeWebViewActivity : BaseActivity() {

    override fun onCreateActivityComponents() {
        addActivityComponent(MenuActivityComponent())
    }

    override fun onSetContentView() {
        setContentView(R.layout.webapp_activity_webview)
    }

    companion object {
        private const val EXTRA_PAGE_URL = "EXTRA_PAGE_URL"
        private const val EXTRA_HAS_BOTTOM_NAVIGATION = BaseActivity.EXTRA_HAS_BOTTOM_BAR
        private const val EXTRA_SELECTED_BOTTOM_NAV_ITEM = BaseActivity.EXTRA_SELECTED_BOTTOM_NAV_ITEM

        fun startActivity(context: Context, pageUrl: String, hasBottomNavigation: Boolean = false, selectedBottomNavigationItem: Int? = null, newTask: Boolean = false, options: Bundle?) {
            context.startActivity(createIntent(context, pageUrl, hasBottomNavigation, selectedBottomNavigationItem, newTask), options)
        }

        fun createIntent(context: Context, pageUrl: String, hasBottomNavigation: Boolean, selectedBottomNavigationItem: Int?, newTask: Boolean): Intent {
            return Intent(context, FetLifeWebViewActivity::class.java).apply {
                val pageUri = Uri.parse(pageUrl)
                if (pageUri.isAbsolute) {
                    putExtra(EXTRA_PAGE_URL, pageUrl)
                } else {
                    putExtra(EXTRA_PAGE_URL, WebAppNavigation.WEBAPP_BASE_URL + "/" + pageUrl)
                }
                putExtra(EXTRA_HAS_BOTTOM_NAVIGATION, hasBottomNavigation)
                putExtra(EXTRA_SELECTED_BOTTOM_NAV_ITEM, selectedBottomNavigationItem)
                flags = if (newTask) {
                    FLAG_ACTIVITY_CLEAR_TASK or FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_NO_ANIMATION
                } else {
                    FLAG_ACTIVITY_NO_ANIMATION
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.webapp_activity_webview)

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.content_layout, FetLifeWebViewFragment.newInstance(getStringExtra(EXTRA_PAGE_URL)!!, getBooleanExtra(EXTRA_HAS_BOTTOM_NAVIGATION) != true), "FetLifeWebViewFragment")
                    .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode != KeyEvent.KEYCODE_BACK) {
            return super.onKeyDown(keyCode, event)
        }

        val wentBack = (supportFragmentManager.fragments.getOrNull(0) as? FetLifeWebViewFragment)?.onKeyBack()
        return if (wentBack == true) {
            true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

    override fun getFabLink(): String? {
        return (supportFragmentManager.fragments.getOrNull(0) as? FetLifeWebViewFragment)?.getFabLink() ?:
            FetLifeApplication.getInstance().webAppNavigation.getFabLink(getStringExtra(EXTRA_PAGE_URL))
    }

}