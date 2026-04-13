package com.icl.ohsguide.auth

import android.app.Application
import android.content.Context
import com.google.android.fhir.DatabaseErrorStrategy
import com.google.android.fhir.FhirEngine
import com.google.android.fhir.FhirEngineConfiguration
import com.google.android.fhir.FhirEngineProvider
import com.google.android.fhir.ServerConfiguration
class FhirApplication : Application() {

    private lateinit var myFhirEngine: FhirEngine

    override fun onCreate() {
        super.onCreate()

            FhirEngineProvider.init(
            FhirEngineConfiguration(
                enableEncryptionIfSupported = true,
                databaseErrorStrategy = DatabaseErrorStrategy.RECREATE_AT_OPEN,
                serverConfiguration = ServerConfiguration(
                    baseUrl = "http://192.168.100.2:8080/fhir/",
                )
            )
        )

        myFhirEngine = FhirEngineProvider.getInstance(this)
    }

    companion object {
        private var fhirEngine: FhirEngine? = null

        fun getEngine(context: Context): FhirEngine {
            return fhirEngine ?: synchronized(this) {
                fhirEngine ?: try {
                    // Initialize the provider with the application context to avoid leaks
                    val instance = FhirEngineProvider.getInstance(context.applicationContext)
                    fhirEngine = instance
                    instance
                } catch (e: Exception) {
                    // If it fails, we log it and fallback to a fresh retrieval next time
                    e.printStackTrace()
                    FhirEngineProvider.getInstance(context.applicationContext)
                }
            }
        }
    }
}