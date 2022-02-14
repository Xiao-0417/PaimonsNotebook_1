package com.lianyi.paimonsnotebook.ui

import android.os.Bundle
import androidx.core.view.forEach
import androidx.core.view.get
import com.google.android.material.navigation.NavigationView
import com.lianyi.paimonsnotebook.bean.config.SideBarButtonSettings
import com.lianyi.paimonsnotebook.lib.adapter.MainViewPager2Adapter
import com.lianyi.paimonsnotebook.lib.adapter.NavigationViewSetupWithViewPager2
import com.lianyi.paimonsnotebook.lib.base.BaseActivity
import com.lianyi.paimonsnotebook.config.AppConfig
import com.lianyi.paimonsnotebook.databinding.ActivityMainBinding
import com.lianyi.paimonsnotebook.lib.information.PagerIndex
import com.lianyi.paimonsnotebook.ui.activity.options.OptionsActivity
import com.lianyi.paimonsnotebook.ui.home.*
import com.lianyi.paimonsnotebook.util.*

class MainActivity : BaseActivity(){
    private var pressBackTime = 0L

    private val menuTitle = mutableListOf<String>()
    private var menuDetailIsOpen = true

    companion object{
        lateinit var bind: ActivityMainBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        initView()
        initClickEvent()
        initConfig()
        //页面切换渐变
//        bind.viewPager2.setPageTransformer(ViewPager2SwitchFadeInFadeOut())
    }

    private fun initView() {
        bind.root.alpha = 0f

        //取消菜单滑动边界
        bind.navView.forEach {
            it.overScrollMode = NavigationView.OVER_SCROLL_NEVER
        }

        //初始化点击退出的时间
        pressBackTime = System.currentTimeMillis()/1000

        //设置需要加载的界面
        val fragments = mapOf(
            PagerIndex.HOME_PAGE to HomeFragment(),
            PagerIndex.DAILY_MATERIALS_PAGE to DailyMaterialsFragment(),
            PagerIndex.WEEK_MATERIALS_PAGE to WeekMaterialsFragment(),
            PagerIndex.CHARACTER_PAGE to CharacterFragment(),
            PagerIndex.WEAPON_PAGE to WeaponFragment(),
            PagerIndex.MAP_PAGE to MapFragment(),
            PagerIndex.WISH_PAGE to WishFragment(),
            PagerIndex.SEARCH_PAGE to SearchFragment(),
            PagerIndex.HUTAO_DATABASE to HutaoDatabaseFragment()
        )

        //设置页面缓存数
        bind.viewPager2.offscreenPageLimit = AppConfig.OFF_SCREEN_PAGE_LIMIT.toInt()
        bind.viewPager2.adapter = MainViewPager2Adapter(fragments,supportFragmentManager,lifecycle)
        NavigationViewSetupWithViewPager2(bind.viewPager2,bind.navView){
                navigationView, viewPager2 ->
            //关闭ViewPager2滑动
            viewPager2.isUserInputEnabled = false
        }.attach()

        bind.navView.menu.forEach {
            menuTitle += it.title.toString()
        }

        setViewMarginBottomByNavigationBarHeight(bind.options)
        setContentMargin(bind.root)
    }

    //初始化各种点击事件
    private fun initClickEvent() {

        //开启侧边栏
        bind.menuSwitch.setOnClickListener {
            bind.container.openDrawer(bind.navViewSpan)
        }
        bind.menuAreaSwitch.setOnClickListener {
            bind.container.openDrawer(bind.navViewSpan)
        }

        //菜单详情切换
        bind.menuDetail.setOnClickListener {
            if(menuDetailIsOpen){
                bind.navView.menu.forEach {
                    it.title = ""
                }
                openAndCloseAnimationHor(bind.navView,AppConfig.MENU_START_WIDTH,AppConfig.MENU_END_WIDTH,200)
            }else{
                menuTitle.forEachIndexed { index, s ->
                    bind.navView.menu[index].title = s
                }
                openAndCloseAnimationHor(bind.navView,AppConfig.MENU_END_WIDTH,AppConfig.MENU_START_WIDTH,200)
            }
            menuDetailIsOpen = !menuDetailIsOpen
        }
        //前往设置
        bind.options.setOnClickListener {
            goA<OptionsActivity>()
        }
    }

    private fun initConfig() {
        if(SideBarButtonSettings.instance.enabled){
            bind.menuAreaSwitch.layoutParams.apply {
                width = SideBarButtonSettings.instance.sideBarAreaWidth.dp.toInt()
            }

            bind.viewPager2.onPageChange {
                when(it){
                    PagerIndex.CHARACTER_PAGE->{
                        bind.menuAreaSwitch.gone()
                    }
                    PagerIndex.WEAPON_PAGE->{
                        bind.menuAreaSwitch.gone()
                    }
                    PagerIndex.MAP_PAGE->{
                        bind.menuAreaSwitch.gone()
                        bind.menuSwitch.show()
                    }
                    else->{
                        bind.menuAreaSwitch.show()
                        if(SideBarButtonSettings.instance.enabled && SideBarButtonSettings.instance.hideDefaultSideBarButton){
                            bind.menuSwitch.gone()
                        }
                    }
                }
            }

            if(SideBarButtonSettings.instance.hideDefaultSideBarButton){
                bind.menuSwitch.gone()
            }else{
                bind.menuSwitch.show()
            }
        }else{
            bind.menuAreaSwitch.gone()
        }
    }

    //当点击返回时
    override fun onBackPressed() {
        val back = System.currentTimeMillis() - pressBackTime
        if(back<AppConfig.PRESS_BACK_INTERVAL){
            finish()
        }else{
            pressBackTime = System.currentTimeMillis()
            "再次点击返回键退出".show()
        }
    }

    //淡入MainActivity
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        bind.root.animate().alpha(1f).duration = 1000
    }

}