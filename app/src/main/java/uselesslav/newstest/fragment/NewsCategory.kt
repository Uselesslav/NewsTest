package uselesslav.newstest.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uselesslav.newstest.R
import uselesslav.newstest.adapters.NewsCategoriesAdapter
import uselesslav.newstest.adapters.SimpleDividerItemDecoration
import uselesslav.newstest.model.NewsCategory
import uselesslav.newstest.network.BodyResponse
import uselesslav.newstest.network.NewsService

/**
 * Фрагмент с списком категорий
 */
class NewsCategory : Fragment() {
    /**
     * Массив новостных категорий
     */
    private lateinit var newsCategory: List<NewsCategory>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)

        // Разметка фрагмента
        val rootView = inflater.inflate(R.layout.fragment_news_category, container, false)

        // TODO сделать нормальную инициализацию
        newsCategory = listOf()

        // Список
        val rv = rootView.findViewById<RecyclerView>(R.id.rv_news_category)
        rv.layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)
        rv.addItemDecoration(SimpleDividerItemDecoration(context!!, false))

        // Запрос списка новостных категорий с сервера
        val apiService = NewsService.create()
        val call = apiService.getListNewsCategories()

        call.enqueue(object : Callback<BodyResponse<NewsCategory>> {
            override fun onFailure(call: Call<BodyResponse<NewsCategory>>?, t: Throwable?) {
                t?.printStackTrace()
            }

            override fun onResponse(call: Call<BodyResponse<NewsCategory>>?, response: Response<BodyResponse<NewsCategory>>?) {
                if (response != null) {
                    newsCategory = response.body()!!.list.toMutableList()

                    // TODO убрать колхоз
                    val adapter = NewsCategoriesAdapter(newsCategory)
                    rv.adapter = adapter
                }
            }
        })

        return rootView
    }
}