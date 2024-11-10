package com.ingegneria.app

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.ingegneria.app.databinding.ActivitySocialBinding

class SocialActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySocialBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_social)

        binding = ActivitySocialBinding.inflate(layoutInflater)

        // Set behaviour back button
        val back: ImageButton? = findViewById(R.id.social_back_button) as? ImageButton
        back?.setOnClickListener{
            finish()
        }
    }
}