package com.amipet.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.amipet.app.R
import com.bumptech.glide.Glide

class AnimalAdapter(context: Context, private val animals: MutableList<Map<String, String>>) :
    ArrayAdapter<Map<String, String>>(context, 0, animals) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_animal_card, parent, false)

        val animal = getItem(position)
        val nameView = view.findViewById<TextView>(R.id.animal_name)
        val typeView = view.findViewById<TextView>(R.id.animal_type)
        val imageView = view.findViewById<ImageView>(R.id.animal_image)

        nameView.text = animal?.get("name") ?: "Nome do Animal"
        typeView.text = animal?.get("type") ?: "Tipo, Idade"
        Glide.with(context)
            .load("")
            .placeholder(R.drawable.placeholder)
            .into(imageView)

        return view
    }

    fun getList(): MutableList<Map<String, String>> {
        return animals
    }
}