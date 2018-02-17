package com.dvinfosys.loginregistrationforkotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.content.Intent
import android.os.Handler


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
        getSupportActionBar()!!.hide();

        Handler().postDelayed(Runnable {
            val i = Intent(this@MainActivity, Login::class.java)
            startActivity(i)
            finish()
        }, 3000)
    }
}
