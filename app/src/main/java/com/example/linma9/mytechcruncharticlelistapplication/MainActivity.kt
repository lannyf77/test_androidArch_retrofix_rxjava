package com.example.linma9.mytechcruncharticlelistapplication

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.Palette
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import com.example.linma9.mytechcruncharticlelistapplication.database.DataManager
import com.example.linma9.mytechcruncharticlelistapplication.model.service.NetworkUtils
import com.example.linma9.mytechcruncharticlelistapplication.eventbus.DataEvent
import com.example.linma9.mytechcruncharticlelistapplication.eventbus.GlobalEventBus
import com.example.linma9.mytechcruncharticlelistapplication.presentor.viewModel.TheLifeCycleObserve
import android.os.Build
import android.preference.PreferenceManager
import android.support.annotation.ColorRes
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.app.AppCompatDelegate
import android.util.SparseIntArray
import android.view.WindowManager
import java.util.*


class MainActivity : AppCompatActivity(), LifecycleRegistryOwner {

    private val mRegistry: LifecycleRegistry = LifecycleRegistry(this);
    override fun getLifecycle() : LifecycleRegistry {
        return mRegistry
    }

    private var theLifeCycleObserve: TheLifeCycleObserve? = null
    private var collapsedMenu: Menu? = null
    //toolbar
    private lateinit var collapsingToolbar: CollapsingToolbarLayout
    private lateinit var appBarLayout: AppBarLayout

    private var headerImgs: ArrayList<Int> = arrayListOf(
            R.drawable.header_img1,
            R.drawable.header_img2,
            R.drawable.header_img3,
            R.drawable.header_img4,
            R.drawable.header_img5,
            R.drawable.header_img6,
            R.drawable.header_img7,
            R.drawable.header_img8,
            R.drawable.header_img9
            )

    private var headerImg_index: Int = 0

    private var lastUsedIndex: SparseIntArray = SparseIntArray()

    private fun randomHeaderImg() : Int {
        val r = Random()
        var index = r.nextInt(headerImgs.size)

        var loop = 0
        while (loop < headerImgs.size && (lastUsedIndex.indexOfKey(index) >= 0)) {
            index = r.nextInt(headerImgs.size)
            loop++
        }

        if (lastUsedIndex.size() > headerImgs.size) {
            lastUsedIndex.clear()
        }

        if (index == headerImg_index) {
            index = r.nextInt(headerImgs.size)
        }

        headerImg_index = index
        lastUsedIndex.append(index, index)

        return headerImg_index
        //return (++headerImg_index % headerImgs.size)
    }

    private var appBarExpanded = true

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt("LAST_IMG_INDEX", headerImg_index)

    }

    private fun intiToolbar(savedInstanceState: Bundle?) {

        if (savedInstanceState != null) {
            headerImg_index = savedInstanceState.getInt("LAST_IMG_INDEX", 0)

            if (headerImg_index != 0) {
                findViewById<ImageView>(R.id.headerImg).setImageResource(headerImgs[headerImg_index])
                invalidateOptionsMenu()
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        appBarLayout = findViewById<AppBarLayout>(R.id.appbar)
        collapsingToolbar = findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar)
        collapsingToolbar.title = getString(R.string.article_list)
        appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            //Vertical offset == 0 indicates appBar is fully expanded.
            if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange() && appBarExpanded) {
                // Collapsed

                updateToolbarScrim(false)

                appBarExpanded = false
                headerImg_index = randomHeaderImg()
                findViewById<ImageView>(R.id.headerImg).setImageResource(headerImgs[headerImg_index])

                invalidateOptionsMenu()
            } else if (verticalOffset == 0 && !appBarExpanded) {

                updateToolbarScrim(true)

                // Expanded
                appBarExpanded = true
                invalidateOptionsMenu()

            }
        }

        updateToolbarScrim(true)
    }

    private fun updateToolbarScrim(expand: Boolean) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            val bitmap = BitmapFactory.decodeResource(resources, headerImgs[headerImg_index])
            if (bitmap != null && !bitmap.isRecycled) {

                Palette.from(bitmap).generate(object : Palette.PaletteAsyncListener {
                    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                    override fun onGenerated(palette: Palette) {

                        var mutedColor = palette.getMutedColor(ContextCompat.getColor(this@MainActivity, R.color.colorPrimary))
                        var mutedDarkColor = palette.getDarkMutedColor(ContextCompat.getColor(this@MainActivity, R.color.colorPrimaryDark))
                        var vibrantColor = palette.getVibrantColor(ContextCompat.getColor(this@MainActivity, R.color.colorAccent))
                        collapsingToolbar.setContentScrimColor(mutedColor)

                        collapsingToolbar.setStatusBarScrimColor(mutedDarkColor)

                        var window = getWindow()
                        if (expand) {
                            window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                        } else {
                            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                            window.setStatusBarColor(mutedDarkColor)
                        }

                        findViewById<FloatingActionButton>(R.id.fab).setBackgroundTintList(ColorStateList.valueOf(vibrantColor));
                    }
                })
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initTheme()
        val toolbar = findViewById<Toolbar>(R.id.anim_toolbar) as Toolbar
        setSupportActionBar(toolbar)

        intiToolbar(savedInstanceState)

        if (savedInstanceState == null) {
            changeFragment(ArticlesFragment())
        }

        val fab = findViewById<FloatingActionButton>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show() }

        theLifeCycleObserve = TheLifeCycleObserve(lifecycle, object : TheLifeCycleObserve.OnLifeCycleChange {
            override fun onCreate() {
                //Log.i("TheLifeCycleObserve","+++ +++ mainActivity::TheLifeCycleObserve:onCreate(), thread:"+Thread.currentThread().getId())
            }

            override fun onStop() {
                //Log.i("TheLifeCycleObserve","+++ +++ --- mainActivity::TheLifeCycleObserve:onStop(), thread:"+Thread.currentThread().getId())
            }

            override fun onStar() {
                //Log.i("TheLifeCycleObserve","+++ +++ mainActivity::TheLifeCycleObserve:onStar(), call initSession(), thread:"+Thread.currentThread().getId())
                initSession()
            }

            override fun onDestroy() {
                //Log.i("TheLifeCycleObserve","+++ +++ --- mainActivity::TheLifeCycleObserve:onDestroy(), call clearSession() && lifecycle.removeObserver, thread:"+Thread.currentThread().getId())
                try {
                    clearSession()
                } catch(e: Exception) {
                    //Log.i("TheLifeCycleObserve","+++ +++ --- mainActivity::theLifeCycleObserve:onDestroy(), !!! exception,  DataManager.instance"+DataManager.instance+", e:"+e.toString())
                }
                lifecycle.removeObserver(theLifeCycleObserve)
            }

        })
        lifecycle.addObserver(theLifeCycleObserve)
    }

    fun initTheme() {
        var mPrefs =  PreferenceManager.getDefaultSharedPreferences(this)
        if ((AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) || mPrefs.getBoolean("NIGHT_THEME_MODE", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.NightTheme)
        } else {
            setTheme(R.style.DayTheme)
        }
        (this as AppCompatActivity).delegate.applyDayNight()
    }

    fun initSession() {
        NetworkUtils.instance!!.init()
        DataManager.instance!!.registerEvtBus()
    }

    fun clearSession() {
        DataManager.instance!!.unregisterEvtBus()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        collapsedMenu = menu
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {

        if (collapsedMenu != null) {
            var menuItem_showHeader = collapsedMenu?.findItem(R.id.action_show_header)
            menuItem_showHeader?.setVisible(!appBarExpanded)

            var menuItem_modeToggler = collapsedMenu?.findItem(R.id.action_toggle_night_mode)
            if(menuItem_modeToggler != null) {

                tintMenuIcon(this, menuItem_modeToggler, if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) android.R.color.white else android.R.color.black)
            }
        }
        return super.onPrepareOptionsMenu(collapsedMenu)
    }

    fun tintMenuIcon(context: Context, item: MenuItem, @ColorRes color: Int) {
        var normalDrawable = item.getIcon();
        var wrapDrawable = DrawableCompat.wrap(normalDrawable);
        DrawableCompat.setTint(wrapDrawable, context.getResources().getColor(color));

        item.setIcon(wrapDrawable);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.action_settings -> {
                var dataEvt: DataEvent = DataEvent()
                dataEvt.setStringMessage("settings")
                GlobalEventBus.instance.post(dataEvt)

                return true
            }
            R.id.action_show_header -> {
                appBarLayout.setExpanded(true)
                return true
            }
            R.id.action_toggle_night_mode -> {

                var mPrefs =  PreferenceManager.getDefaultSharedPreferences(this)
                var nightMode = mPrefs.getBoolean("NIGHT_THEME_MODE", false)
                mPrefs.edit().putBoolean("NIGHT_THEME_MODE", (!nightMode)).commit()

                if (!nightMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                recreate()
//                finish()
//                startActivity(intent)
                true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun changeFragment(f: Fragment, cleanStack: Boolean = false) {
        val ft = supportFragmentManager.beginTransaction();
        if (cleanStack) {
            clearBackStack();
        }
        ft.setCustomAnimations(
                R.anim.abc_fade_in, R.anim.abc_fade_out, R.anim.abc_popup_enter, R.anim.abc_popup_exit);
        ft.replace(R.id.activity_base_content, f);
        ft.addToBackStack(null);
        ft.commit();
    }

    fun clearBackStack() {
        val manager = supportFragmentManager;
        if (manager.backStackEntryCount > 0) {
            val first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.id, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            //DataManager.instance!!.resetDtabase()

        }
    }

    /**
     * Finish activity when reaching the last fragment.
     */
    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager;
        if (fragmentManager.backStackEntryCount > 1) {
            fragmentManager.popBackStack();
        } else {
            finish();
        }
    }
}
