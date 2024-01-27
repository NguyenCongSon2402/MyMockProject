package com.example.mymockproject.view.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import android.widget.ImageView
import com.example.mymockproject.R
import com.example.mymockproject.utility.Utility
import com.example.mymockproject.databinding.ActivityRegisterBinding
import com.example.mymockproject.utility.DialogUtil
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private var isShowPass = false
    private var isShowPassAgain = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initEvent()
    }

    private fun initEvent(){
        binding.icBack.setOnClickListener {
            finish()
        }

        binding.showPass.setOnClickListener {
            isShowPass = !isShowPass
            showPass(isShowPass, binding.edtPass, binding.showPass)
        }

        binding.showPassAgain.setOnClickListener {
            isShowPassAgain = !isShowPassAgain
            showPass(isShowPassAgain, binding.edtPassAgain, binding.showPassAgain)
        }

        binding.btnRegister.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val pass = binding.edtPass.text.toString()
            val passAgain = binding.edtPassAgain.text.toString()
            if(email.isEmpty()){
                Utility.toast(this, "Bạn chưa nhập email")
            }
            else if(pass.isEmpty() || pass.isEmpty()){
                Utility.toast(this, "Bạn chưa nhập mật khẩu")
            }
            else if(pass != passAgain){
                Utility.toast(this, "Mật khẩu chưa khớp")
            }
            else if(pass.length < 6){
                Utility.toast(this, "Mật khẩu tối thiểu gồm 6 kí tự")
            }
            else if(!binding.tvTerm.isChecked){
                Utility.toast(this, "Bạn phải đồng ý với điều khoản của chúng tôi")
            }
            else{
                DialogUtil.showLoadingDialog(this)
                registerAccount(email, pass)
            }
        }
    }

    private fun showPass(isShow:Boolean, edt: EditText, imageView: ImageView){
        if(isShow){
            edt.transformationMethod = null
            imageView.setImageResource(R.drawable.ic_show_pass)
        }
        else{
            edt.transformationMethod = PasswordTransformationMethod()
            imageView.setImageResource(R.drawable.ic_not_show_pass)
        }
    }

    private fun registerAccount(email: String, pass: String){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener{
                DialogUtil.hideLoading()
                if(it.isSuccessful){
                    Utility.toast(this, "Đăng ký thành công")
                    finish()
                }
                else{
                    Utility.toast(this, it.exception.toString())
                }
            }
    }
}