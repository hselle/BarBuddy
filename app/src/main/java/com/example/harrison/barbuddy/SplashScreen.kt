package com.example.harrison.barbuddy

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_splash_screen)

            val anim = AnimationUtils.loadAnimation(this, R.anim.splash_screen_anim)
            anim.setAnimationListener(object : Animation.AnimationListener {

                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    val mainIntent = Intent(this@SplashScreen, MainActivity::class.java)
                    this@SplashScreen.startActivity(mainIntent)
                    this@SplashScreen.finish()
                }

                override fun onAnimationStart(animation: Animation?) {
                }
            })
            ivSplashScreen.startAnimation(anim)
        }
}
