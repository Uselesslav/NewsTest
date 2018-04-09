package uselesslav.newstest.network

import android.content.Context
import android.support.v4.content.Loader
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uselesslav.newstest.model.News

/**
 * Реализация загрузчика данных списка новостей категории
 */
class ListNewsLoader(
        context: Context,
        private val idCategory: Int)
    : Loader<List<News>>(context) {

    /**
     * Запрос
     */
    private lateinit var call: Call<BodyResponseList<News>>

    /**
     * Массив новостей
     */
    private var shortNewsList: MutableList<News> = mutableListOf()

    /**
     * Флаг последней страницы
     */
    private var isLastPage = false

    /**
     * Номер страницы
     */
    private var page = 0

    override fun onStartLoading() {
        super.onStartLoading()
        if (isLastPage) {
            deliverResult(shortNewsList)
        } else {
            forceLoad()
        }
    }

    override fun onForceLoad() {
        super.onForceLoad()

        call = NewsService.Factory.create().getListShortNews(idCategory, page)
        call.enqueue(object : Callback<BodyResponseList<News>> {
            override fun onResponse(call: Call<BodyResponseList<News>>, response: Response<BodyResponseList<News>>) {
                if (response.body() != null) {
                    if (!response.body()!!.list.isEmpty()) {
                        // Если получаемый с сервера список не пуст, изменение номера
                        // запрашиваемой страницы и добавление новых элементов к списку
                        shortNewsList.addAll(response.body()!!.list)
                        page += 1
                    } else {
                        // Изменение состояния флага, если полученный список пуст
                        isLastPage = true
                    }
                    deliverResult(shortNewsList)
                }
            }

            override fun onFailure(call: Call<BodyResponseList<News>>, t: Throwable) {
                deliverResult(null)
            }
        })

    }

    override fun onStopLoading() {
        call.cancel()
        super.onStopLoading()
    }
}