package com.amipet.app

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.amipet.app.databinding.ActivityAnimalProfileBinding

class AnimalProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnimalProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnimalProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Animação de pop-in
        val animalCard: CardView = binding.animalCard
        animalCard.startAnimation(AnimationUtils.loadAnimation(this, R.anim.card_pop_in))

        // Botão de ação
        binding.actionButton.setOnClickListener {
            it.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).withEndAction {
                it.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
                showToast("Ação realizada!")
            }.start()
        }

        // Navegação inferior
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, UserProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun showToast(message: String) {
        val toastView = layoutInflater.inflate(R.layout.custom_toast, null)
        toastView.findViewById<android.widget.TextView>(R.id.toast_message).text = message
        Toast(this).apply {
            duration = Toast.LENGTH_SHORT
            setView(toastView)
            show()
        }
    }
}