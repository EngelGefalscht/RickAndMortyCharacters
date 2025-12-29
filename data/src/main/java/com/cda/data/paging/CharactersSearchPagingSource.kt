package com.cda.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.cda.data.mapper.toDomain
import com.cda.data.remote.api.RickAndMortyApi
import com.cda.domain.model.Character
import retrofit2.HttpException
import java.io.IOException

class CharactersSearchPagingSource(
    private val api: RickAndMortyApi,
    private val name: String,
) : PagingSource<Int, Character>() {

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? =
        state.anchorPosition?.let { anchor ->
            val page = state.closestPageToPosition(anchor)
            page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        val page = params.key ?: 1
        return try {
            val response = api.getCharacters(page = page, name = name)
            val items = response.characters.map { it.toDomain() }
            val endReached = response.info.next == null

            LoadResult.Page(
                data = items,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (endReached) null else page + 1,
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}
