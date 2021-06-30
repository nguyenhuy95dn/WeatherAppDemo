package com.example.weatherappdemo.core.di

import android.content.Context
import com.example.weatherappdemo.core.data.local.database.AppDatabaseHelper
import com.example.weatherappdemo.core.data.local.database.BaseDatabaseHelper
import com.example.weatherappdemo.core.data.repository.AppRepository
import com.fatboyindustrial.gsonjodatime.DateTimeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.joda.time.DateTime
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.CertificateException
import javax.inject.Singleton
import javax.net.ssl.*

@InstallIn(ApplicationComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideAppRepository(
        databaseHelper: BaseDatabaseHelper,
        appService: AppService
    ): AppRepository {
        return AppRepository(databaseHelper as AppDatabaseHelper, appService)
    }

    @Provides
    @Singleton
    fun provideBaseDatabaseHelper(
        @ApplicationContext context: Context
    ): BaseDatabaseHelper {
        return AppDatabaseHelper(context)
    }

    @Provides
    @Singleton
    fun appService(retrofit: Retrofit): AppService {
        return retrofit.create<AppService>(AppService::class.java)
    }

    @Provides
    @Singleton
    fun retrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .baseUrl("https://www.metaweather.com/api/")
            .build()
    }

    @Provides
    @Singleton
    fun gson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(DateTime::class.java, DateTimeConverter())
        return gsonBuilder.create()
    }

    @Provides
    @Singleton
    fun loggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BASIC
        return interceptor
    }

    @Provides
    @Singleton
    fun okHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return getUnsafeOkHttpClient()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun getUnsafeOkHttpClient(): OkHttpClient.Builder {
        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(
                    chain: Array<java.security.cert.X509Certificate>,
                    authType: String
                ) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(
                    chain: Array<java.security.cert.X509Certificate>,
                    authType: String
                ) {
                }

                override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                    return arrayOf()
                }
            })

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory

            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier(object : HostnameVerifier {
                override fun verify(hostname: String, session: SSLSession): Boolean {
                    return true
                }
            })

            return builder
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}
