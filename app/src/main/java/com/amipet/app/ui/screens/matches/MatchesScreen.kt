package com.amipet.app.ui.screens.matches

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amipet.app.R
import com.amipet.app.api.ApiClient
import com.amipet.app.model.Animal
import com.amipet.app.model.ApiResponse
import com.amipet.app.repository.PetRepository
import com.amipet.app.ui.components.AmiPetButton
import com.amipet.app.ui.components.AmiPetCard
import com.amipet.app.ui.components.EmptyStateAnimation
import com.amipet.app.util.SessionManager
import com.amipet.app.viewmodel.MatchViewModel
import com.amipet.app.viewmodel.MatchViewModelFactory
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchesScreen() {
    val sessionManager = SessionManager(LocalContext.current)
    val apiClient = ApiClient(sessionManager)
    val petRepository = PetRepository(apiClient)
    val factory = MatchViewModelFactory(petRepository)
    val viewModel: MatchViewModel = viewModel(factory = factory)

    val matchesState by viewModel.matchesState.collectAsState()
    var matchedAnimals by remember { mutableStateOf<List<Animal>?>(null) }

    // Fetch animals only for the matched animal IDs
    LaunchedEffect(matchesState) {
        if (matchesState is MatchViewModel.MatchesState.Success) {
            val matches = (matchesState as MatchViewModel.MatchesState.Success).matches
            if (matches.isNotEmpty()) {
                matchedAnimals = coroutineScope {
                    matches.map { match ->
                        async {
                            val response = petRepository.getPetById(match.animalId)
                            if (response.success && response.data != null) response.data else null
                        }
                    }.awaitAll().filterNotNull()
                }
            } else {
                matchedAnimals = emptyList() // Ensure empty list if no matches
            }
        }
    }

    // Load matches when the screen is first displayed
    LaunchedEffect(Unit) {
        viewModel.loadMatches()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.matches_screen_title)) }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when {
                matchesState is MatchViewModel.MatchesState.Loading -> {
                    CircularProgressIndicator()
                }
                matchesState is MatchViewModel.MatchesState.Empty || (matchesState is MatchViewModel.MatchesState.Success && matchedAnimals?.isEmpty() == true) -> {
                    EmptyStateAnimation(
                        title = stringResource(id = R.string.empty_matches_title),
                        description = stringResource(id = R.string.empty_matches_description)
                    )
                }
                matchesState is MatchViewModel.MatchesState.Success && matchedAnimals != null -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = paddingValues,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(matchedAnimals!!) { animal ->
                            AmiPetCard(
                                animal = animal,
                                onClick = {}
                            )
                        }
                    }
                }
                matchesState is MatchViewModel.MatchesState.Error -> {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = (matchesState as MatchViewModel.MatchesState.Error).message,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        AmiPetButton(
                            text = stringResource(id = R.string.try_again),
                            onClick = { viewModel.loadMatches() },
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }
            }
        }
    }
}