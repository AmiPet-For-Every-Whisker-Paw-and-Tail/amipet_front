package com.amipet.app.viewmodel

import com.amipet.app.model.Animal
import com.amipet.app.model.Match

data class MatchWithAnimal(
    val match: Match,
    val animal: Animal
)