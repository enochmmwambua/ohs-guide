package com.icl.ohsguide.auth

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.fhir.sync.SyncJobStatus
import kotlinx.coroutines.launch
import com.icl.ohsguide.R

class PatientListActivity : AppCompatActivity() {

    private val viewModel: PatientListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patients_list)

        val btnSync = findViewById<Button>(R.id.btnSyncNow)
        val tvStatus = findViewById<TextView>(R.id.tvSyncStatus)
        val tvResults = findViewById<TextView>(R.id.tvPatientResults)

        btnSync.setOnClickListener {
            tvStatus.text = "Status: Connecting to Server..."
            viewModel.triggerOneTimeSync()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.pollState.collect { status ->
                    when (status) {
                        is SyncJobStatus.Started -> tvStatus.text = "Status: Sync Started..."
                        is SyncJobStatus.InProgress -> tvStatus.text = "Status: Downloading..."
                        is SyncJobStatus.Finished -> {
                            tvStatus.text = "Status: SUCCESS! Database Updated."
                            viewModel.searchPatientsByName("")
                        }
                        is SyncJobStatus.Failed -> tvStatus.text = "Status: FAILED. Is Docker running?"
                        else -> {}
                    }
                }
            }
        }

        viewModel.liveSearchedPatients.observe(this) { patients ->
            if (patients.isEmpty()) {
                tvResults.text = "0 patients found in local database."
            } else {
                var displayString = "Found ${patients.size} patients:\n\n"

                patients.forEach { patient ->
                    val firstName = patient.name.firstOrNull()?.given?.firstOrNull()?.value ?: "Unknown"
                    val lastName = patient.name.firstOrNull()?.family ?: "Unknown"
                    val city = patient.address.firstOrNull()?.city ?: "No City"

                    displayString += "- $firstName $lastName ($city)\n"
                }

                tvResults.text = displayString
            }
        }
    }
}