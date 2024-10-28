package com.ingegneria.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ingegneria.app.databinding.ActivitySocialBinding

class SocialActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySocialBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_social)

        binding = ActivitySocialBinding.inflate(layoutInflater)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}