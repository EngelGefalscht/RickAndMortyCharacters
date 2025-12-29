package com.cda.presentation.characters.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import com.cda.domain.model.Character
import com.cda.domain.model.LocationRef

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersListScreen(
    modifier: Modifier = Modifier,
    onCharacterClick: (Character) -> Unit,
    viewModel: CharactersListViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsState().value
    val items = viewModel.characters.collectAsLazyPagingItems()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    CharactersSearchBar(
                        query = uiState.query,
                        onQueryChange = { viewModel.accept(CharactersListIntent.QueryChanged(it)) },
                        onClear = { viewModel.accept(CharactersListIntent.ClearQuery) },
                    )
                },
            )
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            CharactersListContent(
                characters = List(items.itemCount) { idx -> items[idx] }.filterNotNull(),
                isRefreshing = items.loadState.refresh is LoadState.Loading,
                refreshError = (items.loadState.refresh as? LoadState.Error)?.error?.message,
                isAppending = items.loadState.append is LoadState.Loading,
                appendError = (items.loadState.append as? LoadState.Error)?.error?.message,
                onCharacterClick = onCharacterClick,
            )
        }
    }
}

@Composable
private fun CharactersSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp),
        value = query,
        onValueChange = onQueryChange,
        singleLine = true,
        placeholder = { Text("Search") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            if (query.isNotBlank()) {
                IconButton(onClick = onClear) {
                    Icon(Icons.Default.Close, contentDescription = "Clear")
                }
            }
        },
    )
}

@Composable
internal fun CharactersListContent(
    characters: List<Character>,
    isRefreshing: Boolean,
    refreshError: String?,
    isAppending: Boolean,
    appendError: String?,
    onCharacterClick: (Character) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(
                count = characters.size,
                key = { idx -> characters[idx].id },
            ) { idx ->
                CharacterListItem(
                    character = characters[idx],
                    onClick = { onCharacterClick(characters[idx]) },
                )
            }

            item {
                when {
                    isAppending -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    appendError != null -> {
                        Text(
                            text = appendError,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(8.dp),
                        )
                    }

                    else -> Spacer(modifier = Modifier.padding(0.dp))
                }
            }
        }

        when {
            isRefreshing -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            refreshError != null -> {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = refreshError,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                    )
                    Text(
                        text = "Pull to refresh or retry later",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun PreviewCharactersListFilled() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    CharactersSearchBar(
                        query = "rick",
                        onQueryChange = {},
                        onClear = {},
                    )
                },
            )
        },
    ) { padding ->
        CharactersListContent(
            modifier = Modifier.padding(padding),
            characters = listOf(
                Character(
                    id = 1,
                    name = "Rick Sanchez",
                    status = "Alive",
                    species = "Human",
                    type = "",
                    gender = "Male",
                    origin = LocationRef(id = 1, name = "Earth (C-137)"),
                    lastKnownLocation = LocationRef(id = 3, name = "Citadel of Ricks"),
                    image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                    episodeIds = emptyList(),
                    created = java.time.Instant.parse("2017-11-04T18:48:46.250Z"),
                ),
                Character(
                    id = 2,
                    name = "Morty Smith",
                    status = "Alive",
                    species = "Human",
                    type = "",
                    gender = "Male",
                    origin = LocationRef(id = 1, name = "Earth (C-137)"),
                    lastKnownLocation = LocationRef(
                        id = 20,
                        name = "Earth (Replacement Dimension)"
                    ),
                    image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
                    episodeIds = emptyList(),
                    created = java.time.Instant.parse("2017-11-04T18:48:46.250Z"),
                ),
            ),
            isRefreshing = false,
            refreshError = null,
            isAppending = false,
            appendError = null,
            onCharacterClick = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun PreviewCharactersListLoading() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    CharactersSearchBar(
                        query = "",
                        onQueryChange = {},
                        onClear = {},
                    )
                },
            )
        },
    ) { padding ->
        CharactersListContent(
            modifier = Modifier.padding(padding),
            characters = emptyList(),
            isRefreshing = true,
            refreshError = null,
            isAppending = false,
            appendError = null,
            onCharacterClick = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun PreviewCharactersListError() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    CharactersSearchBar(
                        query = "morty",
                        onQueryChange = {},
                        onClear = {},
                    )
                },
            )
        },
    ) { padding ->
        CharactersListContent(
            modifier = Modifier.padding(padding),
            characters = emptyList(),
            isRefreshing = false,
            refreshError = "Network error",
            isAppending = false,
            appendError = null,
            onCharacterClick = {}
        )
    }
}


