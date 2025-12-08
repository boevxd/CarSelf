package dm.com.carlog.data

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

@Singleton
class AppPreferenceManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        private const val KEY_LOCALE = "key_locale"

    }

    fun getLocale(): String? {
        return sharedPreferences.getString(KEY_LOCALE, null)
    }

    fun setLocale(locale: String) {
        sharedPreferences.edit { putString(KEY_LOCALE, locale) }
    }
}