package com.icl.ohsguide.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.fhir.search.Order
import com.google.android.fhir.search.StringFilterModifier
import com.google.android.fhir.search.search
import com.google.android.fhir.sync.SyncJobStatus
import com.google.android.fhir.sync.Sync
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import org.hl7.fhir.r4.model.Patient

class PatientListViewModel(application: Application) : AndroidViewModel(application) {

    private val _pollState = MutableSharedFlow<SyncJobStatus>()
    val pollState: Flow<SyncJobStatus> get() = _pollState
    val liveSearchedPatients = MutableLiveData<List<Patient>>()

    init {
        updatePatientList { getSearchResults() }
    }

    fun triggerOneTimeSync() {
        viewModelScope.launch {
            Sync.oneTimeSync<SyncWorker>(getApplication())
                .shareIn(this, SharingStarted.Eagerly, 10)
                .collect { _pollState.emit(it) }
        }
    }

    fun triggerUpdate() {
        viewModelScope.launch {
            val fhirEngine = FhirApplication.getEngine(getApplication())

            val patientsFromWakefield = fhirEngine.search<Patient> {
                filter(Patient.ADDRESS_CITY, { modifier = StringFilterModifier.MATCHES_EXACTLY; value = "Wakefield" })
            }
            val patientsFromTaunton = fhirEngine.search<Patient> {
                filter(Patient.ADDRESS_CITY, { modifier = StringFilterModifier.MATCHES_EXACTLY; value = "Taunton" })
            }

            patientsFromWakefield.forEach {
                it.resource.address.first().city = "Taunton"
                fhirEngine.update(it.resource)
            }
            patientsFromTaunton.forEach {
                it.resource.address.first().city = "Wakefield"
                fhirEngine.update(it.resource)
            }
            triggerOneTimeSync()
        }
    }

    fun searchPatientsByName(nameQuery: String) {
        viewModelScope.launch {
            val fhirEngine = FhirApplication.getEngine(getApplication())
            if (nameQuery.isNotEmpty()) {
                val searchResult = fhirEngine.search<Patient> {
                    filter(Patient.NAME, { modifier = StringFilterModifier.CONTAINS; value = nameQuery })
                }
                liveSearchedPatients.value = searchResult.map { it.resource }
            } else {
                updatePatientList { getSearchResults() }
            }
        }
    }

    private fun updatePatientList(search: suspend () -> List<Patient>) {
        viewModelScope.launch { liveSearchedPatients.value = search() }
    }

    private suspend fun getSearchResults(): List<Patient> {
        val patients: MutableList<Patient> = mutableListOf()
        // Grabs everyone!
        FhirApplication.getEngine(getApplication())
            .search<Patient> { sort(Patient.GIVEN, Order.ASCENDING) }
            .let { patients.addAll(it.map { it.resource }) }
        return patients
    }
}