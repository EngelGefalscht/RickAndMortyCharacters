package com.cda.presentation.characters.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.cda.presentation.characters.details.CharacterDetailsScreen
import com.cda.presentation.characters.list.CharactersListScreen

@Composable
fun CharactersNav(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = CharactersDestination.List,
        modifier = modifier,
    ) {
        composable<CharactersDestination.List> {
            CharactersListScreen(
                onCharacterClick = { character ->
                    navController.navigate(CharactersDestination.Details(character.toNavArg()))
                },
            )
        }

        composable<CharactersDestination.Details> { entry ->
            val dest = entry.toRoute<CharactersDestination.Details>()
            CharacterDetailsScreen(
                character = dest.character.toDomain(),
                onBack = { navController.popBackStack() },
            )
        }
    }
}
