package com.nicksnacs.smashultimateframedata.application

import android.app.Application
import androidx.room.Room
import com.nicksnacs.smashultimateframedata.db.AppDatabase
import com.nicksnacs.smashultimateframedata.net.SUFDApiService
import dagger.Module
import dagger.Provides
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.Type
import javax.inject.Singleton


@Module
class ApplicationModule(val application: Application) {
    @Provides
    @Singleton
    fun provideRoomDB(): AppDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            AppDatabase::class.java, "database-name").build()
    }

    @Provides
    @Singleton
    fun provideSmashUltimateFrameDataService(): SUFDApiService {
        return Retrofit.Builder()
            .baseUrl("https://ultimateframedata.com/")
            .addConverterFactory(HtmlConverterFactory())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(SUFDApiService::class.java)
    }

    private class HtmlConverterFactory : Converter.Factory() {

        private class HtmlConverter : Converter<ResponseBody, Document> {
            @Throws(IOException::class)
            override fun convert(value: ResponseBody): Document? {
                return Jsoup.parse(value.string())
            }
        }

        /**
         * Returns a [Converter] for converting an HTTP response body to `type`, or null if
         * `type` cannot be handled by this factory. This is used to create converters for
         * response types such as `SimpleResponse` from a `Call<SimpleResponse>`
         * declaration.
         */
        override fun responseBodyConverter(
            type: Type?,
            annotations: Array<Annotation>?,
            retrofit: Retrofit?
        ): Converter<ResponseBody, *>? {
            return HtmlConverter()
        }
    }

}