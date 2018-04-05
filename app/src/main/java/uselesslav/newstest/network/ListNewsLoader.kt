package uselesslav.newstest.network

import android.content.Context
import android.support.v4.content.Loader
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uselesslav.newstest.model.ShortNews

/**
 * Реализация загрузчика данных списка новостей категории
 */
class ListNewsLoader(context: Context, id: Int, page: Int) : Loader<List<ShortNews>>(context) {
    /**
     * Запрос
     */
    private var call: Call<BodyResponseList<ShortNews>> = NewsService.Factory.create().getListShortNews(id, page)

    /**
     * Список новостей
     */
    private var shortNewsList: List<ShortNews> = listOf()

    override fun onStartLoading() {
        super.onStartLoading()
        if (shortNewsList.size != 0) {
            deliverResult(shortNewsList)
        } else {
            forceLoad()
        }
    }

    override fun onForceLoad() {
        super.onForceLoad()
        call.clone().enqueue(object : Callback<BodyResponseList<ShortNews>> {
            override fun onResponse(call: Call<BodyResponseList<ShortNews>>, response: Response<BodyResponseList<ShortNews>>) {
                if (response.body() != null) {
                    if (!response.body()!!.list.isEmpty()) {
                        shortNewsList = (response.body()!!.list)
                        deliverResult(shortNewsList)
                    } else {
                        deliverResult(listOf())
                    }
                }
            }

            override fun onFailure(call: Call<BodyResponseList<ShortNews>>, t: Throwable) {
                deliverResult(null)
            }
        })
    }

    override fun onStopLoading() {
        call.cancel()
        super.onStopLoading()
    }
}