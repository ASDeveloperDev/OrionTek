package com.example.oriontek

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import android.view.View

/**
OrionTek.
 *created by ASDeveloper on 27/09/2022 at 07:42.
 */

class SplashActivity : AppCompatActivity() {

    private  var actionBar: ActionBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //requestWindowFeature(Window.FEATURE_NO_TITLE)  //Ocultar BarrasDesactivar
        window.requestFeature(Window.FEATURE_NO_TITLE) //Ocultar BarrasDesactivar
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)

        actionBar = supportActionBar
        actionBar!!.hide()


        Handler().postDelayed({
            //start Autentication
            startActivity(Intent(this, MainActivity::class.java))

            finish()           //Finalizar MainActivity
        }, 400)    // 500 = 1/2 Segundos
        // }, 2000)    //2 Segundos
    }


}