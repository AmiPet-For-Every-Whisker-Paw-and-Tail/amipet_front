package com.amipet.app

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.amipet.app.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Animação de pop-in
        val loginCard: CardView? = binding.loginCard
        loginCard?.startAnimation(AnimationUtils.loadAnimation(this, R.anim.card_pop_in))

        // Botão de login
        binding.loginButton.setOnClickListener {
            it.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).withEndAction {
                it.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
                try {
                    showToast("Login bem-sucedido!")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Finaliza LoginActivity
                } catch (e: Exception) {
                    showToast("Erro ao realizar login: ${e.message}")
                }
            }.start()
        }

        // Navegação para Forgot Password
        binding.forgotPasswordText.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        // Navegação para Register
        binding.registerText.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
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