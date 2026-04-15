package com.example.euclydia.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import com.example.euclydia.R
import com.example.euclydia.databinding.ActivityMainBinding
import com.example.euclydia.viewmodel.EuclydiaViewModel
import com.example.euclydia.viewmodel.SoundOption
import java.util.UUID

class MainActivity : AppCompatActivity(), Plane.Tracker {
    private lateinit var binding: ActivityMainBinding
    val fragMan = supportFragmentManager
    private val viewModel : EuclydiaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (savedInstanceState == null) {
            val frag = PlaneFragment()
            frag.setListener(this)
            viewModel.load()
            supportFragmentManager.beginTransaction()
                .replace(binding.plane.id, frag)
                .commit()

            val id = intent.getSerializableExtra("ID", UUID::class.java)
            if (id != null) {
                supportFragmentManager.beginTransaction()
                    .replace(binding.bottom.id, FollowFragment.newInstance(id))
                    .addToBackStack(null)
                    .commit()
            } else {
                supportFragmentManager.beginTransaction()
                    .replace(binding.bottom.id, ControlFragment())
                    .commit()
            }
        }
        
        binding.mute.setOnCheckedChangeListener { _, ismuted ->
            when(ismuted) {
                true -> SoundOption.on()
                else -> SoundOption.off()
            }
        }
    }

    override fun onSelect(uuid: UUID) {
        fragMan.beginTransaction().replace(binding.bottom.id,
            FollowFragment.newInstance(uuid))
            .commit()
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
