package com.amipet.app

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.amipet.app.adapter.AnimalAdapter
import com.amipet.app.databinding.ActivityMainBinding
import com.lorentzos.flingswipe.SwipeFlingAdapterView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var animalAdapter: AnimalAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            // Animação de pop-in
            binding.swipeView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.card_pop_in))

            // Dados fictícios para cartões
            val animals = mutableListOf(
                mapOf("name" to "Max", "type" to "Cachorro, 2 anos"),
                mapOf("name" to "Luna", "type" to "Gato, 1 ano")
            )
            animalAdapter = AnimalAdapter(this, animals)
            binding.swipeView.adapter = animalAdapter

            // Listeners de swipe
            binding.swipeView.setFlingListener(object : SwipeFlingAdapterView.onFlingListener {
                override fun removeFirstObjectInAdapter() {
                    val animalsList = animalAdapter.getList()
                    if (animalsList.isNotEmpty()) {
                        animalsList.removeAt(0)
                        animalAdapter.notifyDataSetChanged()
                    }
                }

                override fun onLeftCardExit(dataObject: Any?) {
                    showToast("Deslizado para esquerda!")
                }

                override fun onRightCardExit(dataObject: Any?) {
                    showToast("Deslizado para direita!")
                    startActivity(Intent(this@MainActivity, AnimalProfileActivity::class.java))
                }

                override fun onAdapterAboutToEmpty(itemsInAdapter: Int) {
                    if (itemsInAdapter == 0) {
                        showToast("Sem mais cartões! Adicionando novos...")
                        // Adiciona novos cartões para evitar crash
                        val animalsList = animalAdapter.getList()
                        animalsList.add(mapOf("name" to "Bela", "type" to "Cachorro, 3 anos"))
                        animalsList.add(mapOf("name" to "Miau", "type" to "Gato, 2 anos"))
                        animalAdapter.notifyDataSetChanged()
                    }
                }

                override fun onScroll(scrollProgressPercent: Float) {}
            })

            // Navegação inferior
            binding.bottomNavigation.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_home -> true
                    R.id.nav_profile -> {
                        startActivity(Intent(this, UserProfileActivity::class.java))
                        true
                    }
                    else -> false
                }
            }
        } catch (e: Exception) {
            showToast("Erro ao carregar tela principal: ${e.message}")
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