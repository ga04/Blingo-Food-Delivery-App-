package com.example.blingo
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper


class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
            android.os.Handler(Looper.getMainLooper()).postDelayed({

                val intent= Intent(this,LoginActivity::class.java)
                startActivity(intent)
                finish()
        },2500)
    }
}