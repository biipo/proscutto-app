package com.ingegneria.app

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.ingegneria.app.databinding.ActivitySocialBinding

class StoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySocialBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_store)

        binding = ActivitySocialBinding.inflate(layoutInflater)

        // Set behaviour back button
        val back: ImageButton? = findViewById(R.id.store_back_button) as? ImageButton
        back?.setOnClickListener{
            finish()
        }
    }
}