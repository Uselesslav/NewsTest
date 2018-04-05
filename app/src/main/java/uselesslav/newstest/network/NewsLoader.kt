package uselesslav.newstest.network

import android.content.Context
import android.support.v4.content.Loader
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uselesslav.newstest.model.FullNews

/**
 * Реализация загрузчика новости
 */
class NewsLoader(context: Context, id: Int) : Loader<FullNews>(context) {
    /**
     * Запрос
     */
    private var call: Call<BodyResponseObject<FullNews>> = NewsService.Factory.create().getFullNews(15)

    /**
     * Новость
     */
    private var fullNews: FullNews? = null

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
        call.clone().enqueue(object : Callback<BodyResponseObject<FullNews>> {
            override fun onResponse(call: Call<BodyResponseObject<FullNews>>, response: Response<BodyResponseObject<FullNews>>) {
                if (response.body() != null) {
                    fullNews = response.body()!!.response
                    deliverResult(fullNews)
                }
            }

            override fun onFailure(call: Call<BodyResponseObject<FullNews>>, t: Throwable) {
                deliverResult(null)
            }
        })
    }

    override fun onStopLoading() {
        call.cancel()
        super.onStopLoading()
    }
}