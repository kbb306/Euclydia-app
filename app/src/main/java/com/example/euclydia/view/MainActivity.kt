package com.example.euclydia.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import com.example.euclydia.R
import com.example.euclydia.databinding.ActivityMainBinding
import java.util.UUID

class MainActivity : AppCompatActivity(), Plane.Tracker {
    private lateinit var binding: ActivityMainBinding
    val fragMan = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fragMan.beginTransaction()
            .replace(binding.plane.id, PlaneFragment())
            .commit()
        val id = intent.getSerializableExtra("ID", UUID::class.java)
        if (id != null) { // This should be null when activity is first started.
            val fragment = FollowFragment(id)
            fragMan.beginTransaction().add(binding.bottom.id,fragment).commit()
        }
        else {
            val fragment = ControlFragment()
            fragMan.beginTransaction().replace(binding.bottom.id,fragment).commit()
        }
    }

    override fun onSelect(uuid: UUID) {

    }

    companion object {
        fun createIntent(context : Context, shapeID : UUID?): Intent {
            val intent = Intent(context, MainActivity::class.java).apply {
                putExtra("ID",shapeID)
            }
            return intent
        }
    }
}
