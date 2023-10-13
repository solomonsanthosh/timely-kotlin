package com.example.timelynew.view


import android.content.Intent
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.timelynew.MainActivity
import com.example.timelynew.R
import com.github.paolorotolo.appintro.AppIntro2
import com.github.paolorotolo.appintro.AppIntro2Fragment
import com.github.paolorotolo.appintro.model.SliderPagerBuilder

class AppIntro : AppIntro2() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        showIntroSlides()
    }
    private fun showIntroSlides() {
        val customTypefaceTitle = ResourcesCompat.getFont(this, R.font.poppins_semibold)
        val customTypefaceDes = ResourcesCompat.getFont(this, R.font.poppins)
        val pageOne = SliderPagerBuilder()
            .title("Welcome to Timely")

            .titleTypeface(customTypefaceTitle.toString())
            .description("If you want to manage your activities, complete your tasks on schedule, and delegate tasks to others, you've come to the perfect place.")
            .imageDrawable(R.mipmap.logo)
            .bgColor(getColor(R.color.black))
            .build()

        val pageTwo = SliderPagerBuilder()
            .title("Tutorial 1")
            .description("This page lists all your activities. You can add new activity by clicking the + button. Yellow cards represents activities shared with you and purple represents activities assigned to you to complete")
            .imageDrawable(R.drawable.ss1)
            .bgColor(getColor(R.color.black))

            .build()
        val pageThree = SliderPagerBuilder()
            .title("Tutorial 2")
            .description("This page lists all the activities assigned by you to others and their status of completion. You can add new team activity by clicking the + button.")
            .imageDrawable(R.drawable.ss2)
            .bgColor(getColor(R.color.black))

            .build()

        val pageFour = SliderPagerBuilder()
            .title("Tutorial 3")
            .description("You can perform a lot of actions on the activities. On Long press you can open the options menu where you can share your activity with others, add remainders and pin upto 3 activities on top. You can swipe the activity to the right to complete or remove it")
            .imageDrawable(R.drawable.ss3)
            .bgColor(getColor(R.color.black))

            .build()

        addSlide(AppIntro2Fragment.newInstance(pageOne))
        addSlide(AppIntro2Fragment.newInstance(pageTwo))
        addSlide(AppIntro2Fragment.newInstance(pageThree))
        addSlide(AppIntro2Fragment.newInstance(pageFour))
    }
    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        goToMain()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        goToMain()
    }
}