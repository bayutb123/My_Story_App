package com.bayutb.mystoryapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bayutb.mystoryapp.api.ApiConfig
import com.bayutb.mystoryapp.api.RegisterResponse
import com.bayutb.mystoryapp.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    companion object {
        private const val TAG = "RegisterActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        buttons()
    }

    private fun buttons() {
        binding.apply {
            goRegister.setOnClickListener {
                if (!isEmailValid(etEmail.text.toString())) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Invalid Email Address",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (etPass.length() < 8) {
                    // PASSWORD < 8 DIGIT
                    Toast.makeText(
                        this@RegisterActivity,
                        "Password must be 8 digits or more",
                        Toast.LENGTH_SHORT
                    ).show()
                    etPass.text?.clear()
                } else {
                    isLoading(true)
                    val client = ApiConfig.getApiService().registerAccount(
                        etName.text.toString(),
                        etEmail.text.toString(),
                        etPass.text.toString()
                    )
                    client.enqueue(object : Callback<RegisterResponse> {
                        override fun onResponse(
                            call: Call<RegisterResponse>,
                            response: Response<RegisterResponse>
                        ) {
                            isLoading(false)
                            val responseBody = response.body()
                            if (response.isSuccessful && responseBody != null) {
                                val message = responseBody.message
                                AlertDialog.Builder(this@RegisterActivity)
                                    .setTitle("Register Successfully")
                                    .setMessage(message)
                                    .show()
                            } else {
                                AlertDialog.Builder(this@RegisterActivity)
                                    .setTitle("Register Failed")
                                    .setMessage("Email already registered")
                                    .show()
                                Log.e(TAG, "onFailure: ${response.message()}")
                            }
                        }

                        override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {

                            Log.e(TAG, "onFailure: ${t.message}")
                        }

                    })
                }
            }
            goLogin.setOnClickListener {
                Intent(this@RegisterActivity, MainActivity::class.java).also {
                    startActivity(it)
                }
                finish()
            }
        }
    }

    fun isLoading(status: Boolean) {
        if (status) {
            binding.progressBar.visibility = View.VISIBLE
            binding.goLogin.isEnabled = false
            binding.goRegister.isEnabled = false
        } else {
            binding.progressBar.visibility = View.GONE
            binding.goLogin.isEnabled = true
            binding.goRegister.isEnabled = true
        }

    }
}

