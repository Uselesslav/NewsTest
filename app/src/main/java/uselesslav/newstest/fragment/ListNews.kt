package uselesslav.newstest.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uselesslav.newstest.R

/**
 * Список новостей конкретной категории
 */
class ListNews : Fragment() {
    companion object {
        /**
         * Ключи для отправки/получения данных из другого фрагмента
         */
        val id: String = "id"
        val name: String = "name"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)

        // Разметка фрагмента
        val rootView = inflater.inflate(R.layout.fragment_list_news, container, false)

        if (this.arguments != null) {
            // Заголовок окна
            activity!!.title = arguments!![name].toString()
        }

        return rootView
    }
}