package com.ingegneria.app.tabfragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
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
}