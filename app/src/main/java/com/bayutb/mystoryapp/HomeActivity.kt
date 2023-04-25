package com.bayutb.mystoryapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.bayutb.mystoryapp.api.SessionManager
import com.bayutb.mystoryapp.data.Factory
import com.bayutb.mystoryapp.data.StoryList
import com.bayutb.mystoryapp.data.StoryListAdapter
import com.bayutb.mystoryapp.data.StoryListViewModel
import com.bayutb.mystoryapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var adapter: StoryListAdapter
    private val viewModel: StoryListViewModel by viewModels {
        Factory(sessionManager.checkAuth().toString())
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkAuthUser()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttons()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        sessionManager = SessionManager(this)
        val token = sessionManager.checkAuth()
        if (token != null) {

            adapter = StoryListAdapter()
            viewModel.storyList.observe(this) {
                if (it != null) {
                    adapter.submitData(this.lifecycle, it)
                }
            }


            adapter.setOnItemClickCallback(object : StoryListAdapter.OnItemClickCallBack {
                override fun onItemClicked(data: StoryList) {
                    Intent(this@HomeActivity, StoryDetailActivity::class.java)
                        .also {
                            it.putExtra("name", data.name)
                            it.putExtra("description", data.description)
                            it.putExtra("photoUrl", data.photoUrl)
                            startActivity(it)
                        }
                }
            })

            binding.rvStory.setHasFixedSize(true)
            binding.rvStory.layoutManager = LinearLayoutManager(this@HomeActivity)
            binding.rvStory.adapter = adapter
        }
    }

    private fun buttons(){
        binding.apply {
            goPost.setOnClickListener {
                Intent(this@HomeActivity, PostActivity::class.java).also {
                    startActivity(it)
                }
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.go_logout -> {
                val builder = AlertDialog.Builder(this@HomeActivity)
                builder.setMessage("Are you sure you want to logout?")
                    .setCancelable(false)
                    .setPositiveButton("Logout") { _, _ ->
                        sessionManager.clearAuth()
                        Intent(this@HomeActivity, MainActivity::class.java).also {
                            startActivity(it)
                            finish()
                        }
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
                true
            }
            R.id.go_maps -> {
                val intent = Intent(this@HomeActivity, MapsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun checkAuthUser() {
        sessionManager = SessionManager(this@HomeActivity)
        val token = sessionManager.checkAuth()
        val userId = sessionManager.getUserId()

        if (token == null || userId == null) {
            Intent(this@HomeActivity, MainActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }
    }

}