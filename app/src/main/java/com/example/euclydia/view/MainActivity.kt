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
        val fragMan = supportFragmentManager
        val id = intent.getSerializableExtra<UUID>("ID", UUID::class.java) as UUID
        if (id != null) {
            val fragment = FollowFragment(id)

        }
        else {
            val fragment = ControlFragment()
            fragMan.beginTransaction().replace(binding.bottom.id,fragment)
        }
    }

    override fun onSelect(uuid: UUID) {
        supportFragmentManager
    }

    companion object {
        fun createIntent(context : Context, shapeID : UUID?): Intent {
            var intent = Intent(context, MainActivity::class.java).apply {
                putExtra("ID",shapeID)
            }
            return intent
        }
    }
}
