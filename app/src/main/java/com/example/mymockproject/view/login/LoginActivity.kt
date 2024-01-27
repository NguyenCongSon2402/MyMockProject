package com.example.mymockproject.view.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mymockproject.R
import com.example.mymockproject.utility.DialogUtil
import com.example.mymockproject.utility.Utility
import com.example.mymockproject.databinding.ActivityLoginBinding
import com.example.mymockproject.view.home.HomeActivity
import com.example.mymockproject.view.register.RegisterActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var mAuth : FirebaseAuth
    private lateinit var gsc : GoogleSignInClient
    private var isShowPass = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        gsc = GoogleSignIn.getClient(this, gso)

        initEvent()
    }

    private fun initEvent(){
        binding.showPass.setOnClickListener {
            isShowPass = !isShowPass
            if(isShowPass){
                binding.showPass.setImageResource(R.drawable.ic_show_pass)
                binding.edtPass.transformationMethod = null
            }
            else{
                binding.showPass.setImageResource(R.drawable.ic_not_show_pass)
                binding.edtPass.transformationMethod = PasswordTransformationMethod()
            }
        }

        binding.tvRegister.setOnClickListener{
            Intent(this@LoginActivity, RegisterActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.edtEmail.setOnEditorActionListener { textView, idAction, keyEvent ->
            if(idAction == EditorInfo.IME_ACTION_NEXT){
                binding.edtPass.requestFocus()
                true
            } else
                false
        }

        binding.edtPass.setOnEditorActionListener { textView, idAction, keyEvent ->
            if(idAction == EditorInfo.IME_ACTION_DONE){
                binding.btnLogin.performClick()
                true
            } else
                false
        }

        binding.btnLoginFb.setOnClickListener {
            Utility.toast(this, "Chức năng đang phát triển")
        }
        binding.btnLoginTiktok.setOnClickListener {
            binding.btnLoginFb.performClick()
        }

        binding.btnLoginGg.setOnClickListener {
            val intent = gsc.signInIntent
            launcherGg.launch(intent)
            DialogUtil.showLoadingDialog(this)
        }
        binding.tvSkip.setOnClickListener {
            Intent(this@LoginActivity, HomeActivity::class.java).also {
                startActivity(it)
            }
            finish()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val pass = binding.edtPass.text.toString()
            if(email.isEmpty()){
                Utility.toast(this, "Bạn chưa nhập tài khoản")
            }
            else if(pass.isEmpty()){
                Utility.toast(this, "Bạn chưa nhập mật khẩu")
            }
            else{
                DialogUtil.showLoadingDialog(this)
                loginWithAccount(email, pass)
            }
        }
    }

    private var launcherGg = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK){
            val data = it.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener {
                    DialogUtil.hideLoading()
                    if(it.isSuccessful){
                        Intent(this@LoginActivity, HomeActivity::class.java).also {
                            startActivity(it)
                        }
                        finish()
                    }
                    else{
                        Utility.toast(this, "Login Failed: ${it.exception}")
                    }
                }

        } catch (e: ApiException) {
            Log.e("check", "signInResult:failed code=" + e.statusCode)
        }
    }

    private fun loginWithAccount(email: String, pass: String){
        mAuth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener{
                DialogUtil.hideLoading()
                if(it.isSuccessful){
                    Intent(this@LoginActivity, HomeActivity::class.java).also {
                        startActivity(it)
                    }
                }
                else{
                    Utility.toast(this, it.exception.toString())
                }
            }
    }
}