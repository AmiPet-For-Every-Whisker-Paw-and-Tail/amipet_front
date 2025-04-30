package com.amipet.app

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.amipet.app.databinding.ActivityUserProfileBinding

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityUserProfileBinding.inflate(layoutInflater)
            setContentView(binding.root)

            // Animação de pop-in
            val profileCard: CardView? = binding.profileCard
            profileCard?.startAnimation(AnimationUtils.loadAnimation(this, R.anim.card_pop_in))

            // Dados fictícios para o perfil
            binding.nameEditText.setText("Usuário Exemplo")
            binding.emailEditText.setText("exemplo@email.com")

            // Botão de salvar
            binding.saveButton.setOnClickListener {
                it.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).withEndAction {
                    it.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
                    showToast("Perfil salvo!")
                }.start()
            }

            // Navegação inferior
            binding.bottomNavigation.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_home -> {
                        startActivity(Intent(this, MainActivity::class.java))
                        true
                    }
                    R.id.nav_profile -> true
                    else -> false
                }
            }
        } catch (e: Exception) {
            showToast("Erro ao carregar perfil: ${e.message}")
        }
    }

    private fun showToast(message: String) {
        try {
            val toastView = layoutInflater.inflate(R.layout.custom_toast, null)
            toastView.findViewById<android.widget.TextView>(R.id.toast_message).text = message
            Toast(this).apply {
                duration = Toast.LENGTH_SHORT
                setView(toastView)
                show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}