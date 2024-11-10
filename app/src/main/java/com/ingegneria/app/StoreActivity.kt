package com.ingegneria.app

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
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

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if(item.itemId == R.id.store_back_button) {
//            finish()
//            return true
//        }
//        return super.onOptionsItemSelected(item)
//    }
}