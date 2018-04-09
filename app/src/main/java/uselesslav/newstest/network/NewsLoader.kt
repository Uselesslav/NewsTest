package uselesslav.newstest.network

import android.content.Context
import android.support.v4.content.Loader
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uselesslav.newstest.model.News

/**
 * Реализация загрузчика новости
 */
class NewsLoader(context: Context, id: Int) : Loader<News>(context) {
    /**
     * Запрос
     */
    private var call: Call<BodyResponseObject<News>> = NewsService.Factory.create().getFullNews(id)

    /**
     * Новость
     */
    private var fullNews: News? = null

    override fun onStartLoading() {
        super.onStartLoading()
        if (fullNews != null) {
            deliverResult(fullNews)
        } else {
            forceLoad()
        }
    }

    override fun onForceLoad() {
        super.onForceLoad()
        call.clone().enqueue(object : Callback<BodyResponseObject<News>> {
            override fun onResponse(call: Call<BodyResponseObject<News>>, response: Response<BodyResponseObject<News>>) {
                if (response.body() != null) {
                    fullNews = response.body()!!.response
                    deliverResult(fullNews)
                }
            }

            override fun onFailure(call: Call<BodyResponseObject<News>>, t: Throwable) {
                deliverResult(null)
            }
        })
    }

    override fun onStopLoading() {
        call.cancel()
        super.onStopLoading()
    }
}