package uselesslav.newstest.fragment

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import uselesslav.newstest.R
import uselesslav.newstest.model.News
import uselesslav.newstest.network.CallBacks
import uselesslav.newstest.network.NewsLoader
import uselesslav.newstest.setDate
import uselesslav.newstest.setTime


@Suppress("DEPRECATION")
/**
 * Окно со списком новостей
 */
class News : BaseFragment() {
    companion object {
        /**
         * Ключи для передачи информации в этот фрагмент
         */
        const val idKey: String = "id"
        const val titleKey: String = "title"
        const val dateKey: String = "date"
        const val shortDescriptionKey: String = "shortDescription"
    }

    /**
     * Новость
     */
    private var news: News? = null

    /**
     * Текстовое поле с выжимкой новости
     */
    private lateinit var textViewShortNews: TextView

    /**
     * Текстовое поле с полной новостью
     */
    private lateinit var textViewFullNews: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)

        // Разметка фрагмента
        val rootView = inflater.inflate(R.layout.fragment_news, container, false)

        val textViewDate = rootView.findViewById<TextView>(R.id.tv_date)
        val textViewTime = rootView.findViewById<TextView>(R.id.tv_time)
        progressBar = rootView.findViewById(R.id.pb_load)
        textViewShortNews = rootView.findViewById(R.id.tv_short_news)
        textViewFullNews = rootView.findViewById(R.id.tv_full_news)

        if (this.arguments != null) {
            // Создание объекта новости из полученных данных
            news = uselesslav.newstest.model.News(
                    arguments!![idKey] as Int,
                    arguments!![titleKey].toString(),
                    arguments!![dateKey].toString(),
                    arguments!![shortDescriptionKey].toString()
            )

            // Заголовок окна
            activity!!.title = news!!.title
            textViewShortNews.text = resources.getString(R.string.short_news, news!!.shortDescription)
            textViewDate.setDate(news!!.date)
            textViewTime.setTime(news!!.date)
        }

        load(false)

        return rootView
    }

    /**
     * Загрузка новости
     */
    override fun load(restart: Boolean) {

        val callbacks = CallBacks(
                NewsLoader(context!!, news!!.id),
                { news ->
                    when (news) {
                        null -> showError(getString(R.string.error_load))
                        else -> {
                            textViewFullNews.visibility = View.VISIBLE
                            if (Build.VERSION.SDK_INT >= 24) {
                                textViewFullNews.text = Html.fromHtml(news.fullDescription, Html.FROM_HTML_MODE_LEGACY)
                            } else {
                                textViewFullNews.text = Html.fromHtml(news.fullDescription)
                            }
                        }
                    }
                    progressBar.visibility = View.GONE
                })

        progressBar.visibility = View.VISIBLE

        // Загрузка или перезагрузка данных с сервера
        if (restart) {
            loaderManager.restartLoader<News>(id, Bundle.EMPTY, callbacks)
        } else {
            loaderManager.initLoader<News>(id, Bundle.EMPTY, callbacks)
        }
    }
}