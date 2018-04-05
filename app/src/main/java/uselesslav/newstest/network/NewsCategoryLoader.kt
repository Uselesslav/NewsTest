package uselesslav.newstest.network

import android.content.Context
import android.support.v4.content.Loader
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uselesslav.newstest.model.NewsCategory

/**
 * Реализация загрузчика данных о списке новостных категорий
 */
class NewsCategoryLoader(context: Context) : Loader<List<NewsCategory>>(context) {

    /**
     * Запрос
     */
    private var call: Call<BodyResponseList<NewsCategory>> = NewsService.Factory.create().getListNewsCategories()

    /**
     * Список новостей
     */
    private var newsCategories: List<NewsCategory> = listOf()

    override fun onStartLoading() {
        super.onStartLoading()
        if (newsCategories.size != 0) {
            deliverResult(newsCategories)
        } else {
            forceLoad()
        }
    }

    override fun onForceLoad() {
        super.onForceLoad()
        call.clone().enqueue(object : Callback<BodyResponseList<NewsCategory>> {
            override fun onResponse(call: Call<BodyResponseList<NewsCategory>>, response: Response<BodyResponseList<NewsCategory>>) {
                if (response.body() != null) {
                    if (!response.body()!!.list.isEmpty()) {
                        newsCategories = (response.body()!!.list)
                        deliverResult(newsCategories)
                    }
                }
            }

            override fun onFailure(call: Call<BodyResponseList<NewsCategory>>, t: Throwable) {
                deliverResult(null)
            }
        })
    }

    override fun onStopLoading() {
        call.cancel()
        super.onStopLoading()
    }
}