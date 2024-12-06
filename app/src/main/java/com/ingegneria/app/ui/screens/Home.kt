package com.ingegneria.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun Home(navController: NavController) {
    Surface(modifier = Modifier.fillMaxSize()) {
        TopButtons()
        CharacterStats()
    }
}

@Composable
fun TopButtons() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray),
            onClick = {}
        ) {
            Icon(
                imageVector = Icons.Rounded.ShoppingCart,
                contentDescription = "Shop button",
                modifier = Modifier.size(48.dp)
            )
        }
        IconButton(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray),
            onClick = {}
        ) {
            Icon(
                imageVector = Icons.Rounded.Person,
                contentDescription = "Social button",
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Composable
fun CharacterStats() {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 130.dp, start = 15.dp, end = 15.dp)
    ) {
        Box (
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Red),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Livello"
            )
        }
        Column (
            modifier = Modifier
                .padding(start = 10.dp)
        ) {
            // TODO: show text over the right corner of the bar
            Box (
                modifier = Modifier
                    .size(width = 300.dp, height = 30.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Green)
            )
            Box (
                modifier = Modifier
                    .padding(top = 20.dp)
                    .size(width = 300.dp, height = 30.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Blue)
            )

        }

    }
}


@Preview(showBackground = true)
@Composable
fun PreviewHome(navController: NavController = rememberNavController()){
    Home(navController = navController)
}

/*
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.ingegneria.app.SocialActivity
import com.ingegneria.app.StatsActivity
import com.ingegneria.app.StoreActivity
import com.ingegneria.app.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val level: TextView = binding.characterStats.levelText
        level.text = "Lvl bubu"

        val exp: TextView = binding.characterStats.expText
        exp.text = "gugu"

        val life: TextView = binding.characterStats.lifeText
        life.text = "gaga"

        binding.socialButton.setOnClickListener {
                startActivity(Intent(context, SocialActivity::class.java))
        }
        binding.storeButton.setOnClickListener {
            startActivity(Intent(context, StoreActivity::class.java))
        }
        binding.characterStats.statsAreaButton.setOnClickListener {
            startActivity(Intent(context, StatsActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}*/