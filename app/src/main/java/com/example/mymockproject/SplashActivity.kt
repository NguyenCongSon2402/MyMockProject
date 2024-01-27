package com.example.mymockproject

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.mymockproject.databinding.ActivityMainBinding
import com.example.mymockproject.view.home.HomeActivity
import com.example.mymockproject.view.login.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth


class SplashActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var mFirebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mFirebaseAuth = FirebaseAuth.getInstance()
        val currentUser = mFirebaseAuth.currentUser
        val account = GoogleSignIn.getLastSignedInAccount(this)
        Handler().postDelayed({
            if(account == null && currentUser == null){
                Intent(this, LoginActivity::class.java).also {
                    startActivity(it)
                }
            }
            else{
                Intent(this, HomeActivity::class.java).also {
                    startActivity(it)
                }
            }
            finish()
        }, 500)

    }
}