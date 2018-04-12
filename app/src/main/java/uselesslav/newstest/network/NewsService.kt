package uselesslav.newstest.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import uselesslav.newstest.models.News
import uselesslav.newstest.models.NewsCategory

/**
 * Интерфейс серверных запросов
 */
interface NewsService {

    /**
     * Получение списка новостных категорий
     */
    @GET("categories")
    fun getListNewsCategories(): Call<BodyResponseList<NewsCategory>>

    /**
     * Получение списка новостей с пэйджингом
     */
    @GET("categories/{id}/news")
    fun getListShortNews(@Path("id") categoryId: Int, @Query("page") page: Int): Call<BodyResponseList<News>>

    /**
     * Получение полной информации о новости
     */
    @GET("details")
    fun getFullNews(@Query("id") newsId: Int): Call<BodyResponseObject<News>>

    /**
     * Companion object to create the NewsService
     */
    companion object Factory {
        /**
         * URL
         */
        private const val baseURL: String = "http://testtask.sebbia.com/v1/news/"

        /**
         * Фабричный метод
         */
        fun create(): NewsService = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(this.baseURL)
                .build().create(NewsService::class.java)
    }
}