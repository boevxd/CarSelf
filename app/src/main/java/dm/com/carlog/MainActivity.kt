package dm.com.carlog

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import dagger.hilt.android.AndroidEntryPoint
import dm.com.carlog.data.AppPreferenceManager
import dm.com.carlog.ui.CarLog
import dm.com.carlog.ui.theme.CarLogTheme
import dm.com.carlog.util.LocaleHelper
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var preferenceManager: AppPreferenceManager

    override fun attachBaseContext(base: Context) {
        val sharedPreferences =
            base.getSharedPreferences("car_log_prefs", MODE_PRIVATE)
        val localeTag =
            sharedPreferences.getString("key_locale", null) ?: Locale.getDefault().toLanguageTag()
        val locale = Locale.forLanguageTag(localeTag)
        val context = LocaleHelper.updateLocale(base, locale)
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CarLogTheme {
                CarLog()
            }
        }
    }
}