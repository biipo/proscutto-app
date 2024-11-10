package com.ingegneria.app

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.ingegneria.app.databinding.ActivityStatsBinding

class StatsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStatsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_social)

        binding = ActivityStatsBinding.inflate(layoutInflater)

        // Set behaviour back button
        val back: ImageButton? = findViewById(R.id.stats_back_button) as? ImageButton
        back?.setOnClickListener{
            finish()
        }
    }
}