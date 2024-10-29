package com.ingegneria.app

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.ingegneria.app.databinding.ActivitySocialBinding

class StatsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySocialBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_social)

        binding = ActivitySocialBinding.inflate(layoutInflater)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) {// make back button in title bar go back
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}