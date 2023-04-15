package com.bayutb.mystoryapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.bayutb.mystoryapp.api.ApiConfig
import com.bayutb.mystoryapp.api.LoginResponse
import com.bayutb.mystoryapp.api.SessionManager
import com.bayutb.mystoryapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager :SessionManager
    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        castButtons()
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

    private fun castButtons() {
        binding.apply {
            goRegister.setOnClickListener {
                Intent(this@MainActivity, RegisterActivity::class.java).also {
                    startActivity(it)
                }
                finish()
            }
            goLogin.setOnClickListener {
                isLoading(true)
                val client = ApiConfig.getApiService().authLogin(
                    binding.etLoginEmail.text.toString(),
                    binding.etLoginPassword.text.toString()
                )
                client.enqueue(object : Callback<LoginResponse>{
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        isLoading(false)
                        val responseBody = response.body()
                        if (response.isSuccessful && responseBody != null) {
                            sessionManager = SessionManager(this@MainActivity)
                            sessionManager.saveAuth(responseBody.loginResult.token, responseBody.loginResult.userId)
                            Intent(this@MainActivity, HomeActivity::class.java).also {
                                startActivity(it)
                                finish()
                            }
                        } else {
                            AlertDialog.Builder(this@MainActivity)
                                .setTitle("Login Failed")
                                .setMessage("Email or Password incorrect")
                                .show()
                            Log.e(TAG, "onFailure: ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Log.e(TAG, "onFailure: $t")
                    }

                })

            }
        }
    }
}