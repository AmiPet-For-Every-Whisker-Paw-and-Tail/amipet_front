package com.amipet.app.ui.screens.home

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.amipet.app.R
import com.amipet.app.api.ApiClient
import com.amipet.app.model.Animal
import com.amipet.app.repository.PetRepository
import com.amipet.app.ui.components.AmiPetButton
import com.amipet.app.ui.components.EmptyStateAnimation
import com.amipet.app.ui.theme.DislikeAction
import com.amipet.app.ui.theme.LikeAction
import com.amipet.app.util.SessionManager
import com.amipet.app.viewmodel.PetViewModel
import com.amipet.app.viewmodel.PetViewModelFactory
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val apiClient = ApiClient(sessionManager)
    val repository = PetRepository(apiClient)
    val factory = PetViewModelFactory(repository)
    val viewModel: PetViewModel = viewModel(factory = factory)

    val petsState by viewModel.petsState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.home_screen_title)) }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (petsState) {
                is PetViewModel.PetsState.Loading -> {
                    CircularProgressIndicator()
                }
                is PetViewModel.PetsState.Empty -> {
                    EmptyStateAnimation(
                        title = stringResource(id = R.string.empty_pets_title),
                        description = stringResource(id = R.string.empty_pets_description)
                    )
                }
                is PetViewModel.PetsState.Success -> {
                    val pets = (petsState as PetViewModel.PetsState.Success).pets
                    if (pets.isNotEmpty()) {
                        PetCardStack(
                            pets = pets,
                            viewModel = viewModel
                        )
                    }
                }
                is PetViewModel.PetsState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = (petsState as PetViewModel.PetsState.Error).message,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        AmiPetButton(
                            text = stringResource(id = R.string.try_again),
                            onClick = { viewModel.loadPets() },
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PetCardStack(
    pets: List<Animal>,
    viewModel: PetViewModel
) {
    var currentIndex by remember { mutableStateOf(0) }
    val currentPet = pets.getOrNull(currentIndex)

    if (currentPet != null) {
        AnimatedVisibility(
            visible = true,
            exit = slideOutHorizontally(
                targetOffsetX = { if (currentIndex < pets.size - 1) 0 else 1000 },
                animationSpec = tween(durationMillis = 300)
            ) + fadeOut(animationSpec = tween(300))
        ) {
            PetSwipeCard(
                pet = currentPet,
                onLike = {
                    viewModel.likePet(currentPet.id)
                    if (currentIndex < pets.size - 1) {
                        currentIndex++
                    } else {
                        viewModel.loadPets() // Reload if no more pets
                    }
                },
                onDislike = {
                    viewModel.dislikePet(currentPet.id)
                    if (currentIndex < pets.size - 1) {
                        currentIndex++
                    } else {
                        viewModel.loadPets() // Reload if no more pets
                    }
                }
            )
        }
    } else {
        EmptyStateAnimation(
            title = stringResource(id = R.string.empty_pets_title),
            description = stringResource(id = R.string.empty_pets_description)
        )
    }
}

@Composable
fun PetSwipeCard(
    pet: Animal,
    onLike: () -> Unit,
    onDislike: () -> Unit
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    // Animate offsetX and offsetY back to zero when drag ends and no action is triggered
    val animatedOffsetX by animateFloatAsState(
        targetValue = if (offsetX in -150f..150f) 0f else offsetX,
        animationSpec = tween(durationMillis = 200),
        label = "offset_x_animation"
    )
    val animatedOffsetY by animateFloatAsState(
        targetValue = if (offsetX in -150f..150f) 0f else offsetY,
        animationSpec = tween(durationMillis = 200),
        label = "offset_y_animation"
    )

    val rotation by animateFloatAsState(
        targetValue = (animatedOffsetX / 60).coerceIn(-10f, 10f),
        label = "card_rotation"
    )

    val scale by animateFloatAsState(
        targetValue = if (animatedOffsetX != 0f || animatedOffsetY != 0f) 0.95f else 1f,
        label = "card_scale"
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 32.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                FloatingActionButton(
                    onClick = onDislike,
                    modifier = Modifier.padding(end = 64.dp),
                    containerColor = Color.White
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Descartar",
                        tint = DislikeAction
                    )
                }

                FloatingActionButton(
                    onClick = onLike,
                    modifier = Modifier.padding(start = 64.dp),
                    containerColor = Color.White
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Curtir",
                        tint = LikeAction
                    )
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .offset { IntOffset(animatedOffsetX.roundToInt(), animatedOffsetY.roundToInt()) }
                .rotate(rotation)
                .scale(scale)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = {
                            when {
                                offsetX > 150 -> onLike()
                                offsetX < -150 -> onDislike()
                                else -> {
                                    // No action, let animation handle return
                                }
                            }
                            // Reset manual offset for next drag
                            offsetX = 0f
                            offsetY = 0f
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            offsetX += dragAmount.x
                            offsetY += dragAmount.y
                        }
                    )
                },
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box {
                AsyncImage(
                    model = pet.imageUrl,
                    contentDescription = "Foto de ${pet.name}",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )

                when {
                    offsetX > 50 -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp)
                                .background(LikeAction.copy(alpha = 0.3f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Like",
                                tint = Color.White,
                                modifier = Modifier.scale(2f)
                            )
                        }
                    }
                    offsetX < -50 -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp)
                                .background(DislikeAction.copy(alpha = 0.3f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Dislike",
                                tint = Color.White,
                                modifier = Modifier.scale(2f)
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
                        .padding(16.dp)
                ) {
                    Text(
                        text = pet.name,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = "${pet.breed ?: pet.species} • ${pet.age} • ${pet.gender}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}