package uselesslav.newstest.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uselesslav.newstest.R

/**
 * Окно со списком новостей
 */
class News : Fragment() {
    companion object {
        const val id: String = "id"
        const val title: String = "title"
        const val date: String = "date"
        const val shortDescription: String = "shortDescription"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)

        // Разметка фрагмента
        val rootView = inflater.inflate(R.layout.fragment_news, container, false)

        if (this.arguments != null) {
            // Заголовок окна
            activity!!.title = arguments!![title].toString()
        }

        return rootView
    }
}