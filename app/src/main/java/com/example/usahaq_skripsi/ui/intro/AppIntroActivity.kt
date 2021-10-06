package com.example.usahaq_skripsi.ui.intro

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.usahaq_skripsi.databinding.ActivityAppIntroBinding
import com.example.usahaq_skripsi.ui.intro.fragmentAppIntro.EasyRegister
import com.example.usahaq_skripsi.ui.intro.fragmentAppIntro.LetsGo
import com.example.usahaq_skripsi.ui.intro.fragmentAppIntro.StockEverything
import com.example.usahaq_skripsi.ui.login.Login

class AppIntroActivity : AppCompatActivity() {

    val fragment1 = EasyRegister()
    val fragment2 = StockEverything()
    val fragment3 = LetsGo()

    lateinit var adapter : IntroAdapter

    private lateinit var binding : ActivityAppIntroBinding

    lateinit var prefrence : SharedPreferences
    val pref_show_intro = "Intro"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefrence = getSharedPreferences("IntroSlider", Context.MODE_PRIVATE)

        if(!prefrence.getBoolean(pref_show_intro, true)){
            startActivity(Intent(this@AppIntroActivity, Login::class.java ))
            finish()
        }
        adapter = IntroAdapter(supportFragmentManager)
        adapter.list.add(fragment1)
        adapter.list.add(fragment2)
        adapter.list.add(fragment3)

        binding.apply{
            viewpager.adapter = adapter
            btnBack.visibility = View.GONE

            btnContinue.setOnClickListener{ viewpager.currentItem++}

            viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    if(position == 0){
                        btnBack.visibility = View.GONE
                    }
                    else{
                        btnBack.visibility = View.VISIBLE
                    }
                    if(position==adapter.list.size-1){
                        btnBack.setOnClickListener{viewpager.currentItem--}
                        btnContinue.setOnClickListener { startActivity(Intent(this@AppIntroActivity, Login::class.java ))
                            finish()
                            val editor = prefrence.edit()
                            editor.putBoolean(pref_show_intro, false)
                            editor.apply()}
                    }
                    else{
                        btnContinue.setOnClickListener{ viewpager.currentItem++}
                        btnBack.setOnClickListener{viewpager.currentItem--}
                    }

                    when(viewpager.currentItem){
                        0->{
                            indicator1.setTextColor(Color.BLUE)
                            indicator2.setTextColor(Color.BLACK)
                            indicator3.setTextColor(Color.BLACK)
                        }
                        1->{
                            indicator1.setTextColor(Color.BLACK)
                            indicator2.setTextColor(Color.BLUE)
                            indicator3.setTextColor(Color.BLACK)
                        }
                        2->{
                            indicator1.setTextColor(Color.BLACK)
                            indicator2.setTextColor(Color.BLACK)
                            indicator3.setTextColor(Color.BLUE)
                        }
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {
                }

            })
        }
    }

    class IntroAdapter(manager : FragmentManager) : FragmentPagerAdapter(manager){

        val list : MutableList<Fragment> = ArrayList()

        override fun getCount(): Int = list.size

        override fun getItem(position: Int): Fragment = list[position]

    }
}