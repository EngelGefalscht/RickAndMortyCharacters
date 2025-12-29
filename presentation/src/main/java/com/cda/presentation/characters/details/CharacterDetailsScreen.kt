package com.cda.presentation.characters.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.cda.domain.model.Character
import com.cda.domain.model.Episode
import com.cda.domain.model.Location
import com.cda.domain.model.LocationRef
import com.cda.presentation.common.formatInstant
import java.time.Instant
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailsScreen(
    character: Character,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val vm: CharactersDetailsViewModel =
        hiltViewModel<CharactersDetailsViewModel, CharactersDetailsViewModel.Factory>(
            key = "CharactersDetailsViewModel_${character.id}",
            creationCallback = { factory -> factory.create(character) },
        )

    val state by vm.state.collectAsState()

    CharacterDetailsContent(
        state = state,
        onBack = onBack,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CharacterDetailsContent(
    state: CharactersDetailsState,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val character = state.character

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = character?.name ?: "Character",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        if (character == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = state.error ?: "No character provided",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                )
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item {
                HeaderCard(character = character)
            }

            item {
                InfoCard(
                    character = character,
                    origin = state.origin,
                    lastKnown = state.lastKnown,
                )
            }

            item {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Episodes",
                        style = MaterialTheme.typography.titleMedium,
                    )
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .height(18.dp),
                            strokeWidth = 2.dp,
                        )
                    }
                }
            }

            if (state.error != null) {
                item {
                    Text(
                        text = state.error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }

            items(
                items = state.episodes.ifEmpty { character.episodeIds.map { null } },
                key = { ep -> ep?.id ?: -1  },
            ) { episode ->
                if (episode != null) {
                    EpisodeRow(episode = episode)
                } else {
                    EpisodeIdRow()
                }
            }
        }
    }
}

@Composable
private fun HeaderCard(character: Character) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = CardDefaults.outlinedCardBorder(true),
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = character.image,
                contentDescription = character.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
            )

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(14.dp),
                shape = RoundedCornerShape(999.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                tonalElevation = 2.dp,
            ) {
                Text(
                    text = "${character.status} • ${character.species}",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                )
            }
        }
    }
}

@Composable
private fun InfoCard(
    character: Character,
    origin: Location?,
    lastKnown: Location?,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = CardDefaults.outlinedCardBorder(true),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "About",
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(10.dp))

            InfoRow(label = "Name", value = character.name)
            InfoRow(label = "Gender", value = character.gender)
            InfoRow(label = "Status", value = character.status)
            InfoRow(label = "Species", value = character.species)
            InfoRow(label = "Type", value = character.type.ifBlank { "—" })
            InfoRow(label = "Origin", value = origin?.name ?: character.origin.name)
            InfoRow(label = "Location", value = lastKnown?.name ?: character.lastKnownLocation.name)
            InfoRow(label = "Created", value = formatInstant(character.created))
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(0.35f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(0.65f),
        )
    }
}

@Composable
private fun EpisodeRow(episode: Episode) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)) {
            Text(text = episode.episode, style = MaterialTheme.typography.labelLarge)
            Text(text = episode.name, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun EpisodeIdRow() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Text(
            text = "Loading…",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun PreviewCharacterDetailsContentLoaded() {
    CharacterDetailsContent(
        state = CharactersDetailsState(
            character = Character(
                id = 1,
                name = "Rick Sanchez",
                status = "Alive",
                species = "Human",
                type = "",
                gender = "Male",
                origin = LocationRef(id = 1, name = "Earth (C-137)"),
                lastKnownLocation = LocationRef(id = 3, name = "Citadel of Ricks"),
                image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                episodeIds = listOf(1, 2, 3),
                created = Instant.parse("2017-11-04T18:48:46.250Z"),
            ),
            origin = Location(1, "Earth (C-137)", "Planet", "Dimension C-137"),
            lastKnown = Location(3, "Citadel of Ricks", "Space station", "unknown"),
            episodes = listOf(
                Episode(1, "Pilot", "S01E01", Instant.parse("2017-11-10T12:56:33.798Z")),
                Episode(2, "Lawnmower Dog", "S01E02", Instant.parse("2017-11-10T12:56:33.798Z")),
            ),
            isLoading = false,
            error = null,
        ),
        onBack = {},
    )
}


