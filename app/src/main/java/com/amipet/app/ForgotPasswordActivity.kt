package com.amipet.app

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.amipet.app.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Animação de pop-in
        val forgotPasswordCard: CardView = binding.forgotPasswordCard
        forgotPasswordCard.startAnimation(AnimationUtils.loadAnimation(this, R.anim.card_pop_in))

        // Botão de reset
        binding.resetButton.setOnClickListener {
            it.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).withEndAction {
                it.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
                showToast("Email enviado!")
                startActivity(Intent(this, LoginActivity::class.java))
            }.start()
        }

        // Navegação para Login
        binding.backToLoginText.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
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