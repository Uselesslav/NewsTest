package uselesslav.newstest.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import uselesslav.newstest.model.FullNews
import uselesslav.newstest.model.NewsCategory
import uselesslav.newstest.model.ShortNews

/**
 * Интерфейс серверных запросов
 */
interface NewsService {

    /**
     * Получение списка новостных категорий
     */
    @GET("categories")
    fun getListNewsCategories(): Call<BodyResponse<NewsCategory>>

    /**
     * Получение списка новостей с пэйджингом
     */
    @GET("categories/{id}/news")
    fun getListShortNews(@Path("id") categoryId: Int, @Query("page") page: Int): Call<BodyResponse<ShortNews>>

    /**
     * Получение полной информации о новости
     */
    @GET("details")
    fun getFullNews(@Query("id") newsId: Int): Call<FullNews>

    /**
     * Companion object to create the NewsService
     */
    companion object Factory {
        /**
         * URL
         */
        private val baseURL: String = "http://testtask.sebbia.com/v1/news/"

        /**
         * Фабричный метод
         */
        fun create(): NewsService = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseURL)
                .build().create(NewsService::class.java)
    }
}