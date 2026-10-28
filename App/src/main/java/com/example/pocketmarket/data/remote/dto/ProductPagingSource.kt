package com.example.pocketmarket.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pocketmarket.domain.model.Product
import com.example.pocketmarket.mapper.toDomain
import retrofit2.HttpException
import java.io.IOException

class ProductPagingSource(
    private val api: DummyJsonApi
) : PagingSource<Int, Product>() {

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(PAGE_SIZE) ?: anchorPage?.nextKey?.minus(PAGE_SIZE)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val position = params.key ?: 0
        return try {
            val response = api.getProducts(limit = params.loadSize, skip = position)
            val products = response.products.toDomain()

            val nextKey = if (products.isEmpty() || position + products.size >= response.total) {
                null
            } else {
                position + products.size
            }

            LoadResult.Page(
                data = products,
                prevKey = if (position == 0) null else position - params.loadSize,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}