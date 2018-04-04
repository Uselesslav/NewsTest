package uselesslav.newstest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * Главный экран. Точка входа в приложение
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fl_container, uselesslav.newstest.fragment.NewsCategory())
                    .commit()
        }
    }
}
