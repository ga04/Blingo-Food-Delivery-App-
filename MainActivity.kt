package com.example.blingo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.blingo.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        var NavController:NavController = findNavController(R.id.fragmentContainerView)
        var bottomnav=findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomnav.setupWithNavController(NavController)
        binding.notificationButton.setOnClickListener{
            val bottomSheetDialog = Notification_Bottom_Fragment()
            bottomSheetDialog.show(supportFragmentManager,"Test")
        }
        binding.supportButton.setOnClickListener{
            val intent= Intent(this,ChatActivity::class.java)

            intent.putExtra("name","Admin")
            intent.putExtra("uid","R9Hjg2ylbONPwniQKM3ZZMWo2bN2")

            startActivity(intent)
        }
        binding.logOutButton.setOnClickListener{
            try {
                auth.signOut()
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                finish()
                startActivity(intent)
            } catch (e: Exception) {
                Log.e("LogoutError", "Error logging out", e)
            }

        }
    }
}