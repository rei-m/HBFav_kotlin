package me.rei_m.hbfavmaterial

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.squareup.leakcanary.LeakCanary
import com.twitter.sdk.android.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import io.fabric.sdk.android.Fabric
import me.rei_m.hbfavmaterial.di.*
import me.rei_m.hbfavmaterial.models.BookmarkFavoriteModel
import me.rei_m.hbfavmaterial.models.BookmarkUserModel
import me.rei_m.hbfavmaterial.models.HotEntryModel
import me.rei_m.hbfavmaterial.models.NewEntryModel
import javax.inject.Inject
import javax.inject.Named

class App : Application() {

    @Inject
    lateinit var bookmarkFavoriteModel: BookmarkFavoriteModel

    @field:[Inject Named("bookmarkUserModelForSelf")]
    lateinit var bookmarkUserModel: BookmarkUserModel

    @Inject
    lateinit var hotEntryModel: HotEntryModel

    @Inject
    lateinit var newEntryModel: NewEntryModel

    companion object {
        // platformStatic allow access it from java code
        @JvmStatic lateinit public var graph: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()

        // Application起動時に実行される。アプリの初期処理など

        // Dagger2
        graph = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .appLayerModule(AppLayerModule())
                .infraLayerModule(InfraLayerModule())
                .build()
        graph.inject(this)

        // LeakCanaryの設定
        if (BuildConfig.DEBUG) {
            LeakCanary.install(this);
        }

        // Set up Fabric
        val crashlyticsCore = CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build();
        val authConfig = TwitterAuthConfig(getString(R.string.api_key_twitter_consumer_key), getString(R.string.api_key_twitter_consumer_secret))
        Fabric.with(this, Crashlytics.Builder().core(crashlyticsCore).build(), Twitter(authConfig))
    }

    fun resetBookmarks() {
        bookmarkFavoriteModel.bookmarkList.clear()
        bookmarkUserModel.bookmarkList.clear()
        hotEntryModel.entryList.clear()
        newEntryModel.entryList.clear()
    }
}
