package com.benkkstudios.ads.shared.consent

import android.app.Activity
import com.benkkstudios.ads.shared.AdsConstant
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentDebugSettings.DebugGeography
import com.google.android.ump.ConsentForm
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.FormError
import com.google.android.ump.UserMessagingPlatform

class ConsentManager {
    fun interface OnConsentGatheringCompleteListener {
        fun consentGatheringComplete(error: FormError?)
    }


    fun request(activity: Activity, onSuccess: () -> Unit) {
        UserMessagingPlatform.getConsentInformation(activity).let { consentInformation ->
            gatherConsent(activity, consentInformation) {
                canRequestAds = consentInformation.canRequestAds()
                onSuccess.invoke()
            }
        }
    }

    private fun createDebugSetting(activity: Activity): ConsentDebugSettings {
        return ConsentDebugSettings.Builder(activity)
            .setDebugGeography(DebugGeography.DEBUG_GEOGRAPHY_EEA)
            .setForceTesting(true)
            .build()
    }

    private fun createRequestParameter(activity: Activity): ConsentRequestParameters {
        return if (AdsConstant.DEBUG_MODE) {
            ConsentRequestParameters.Builder()
                .setConsentDebugSettings(createDebugSetting(activity))
                .build()
        } else {
            ConsentRequestParameters.Builder().build()
        }

    }

    private fun gatherConsent(
        activity: Activity,
        consentInformation: ConsentInformation,
        onConsentGatheringCompleteListener: OnConsentGatheringCompleteListener,
    ) {
        consentInformation.requestConsentInfoUpdate(
            activity,
            createRequestParameter(activity),
            {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { formError ->
                    onConsentGatheringCompleteListener.consentGatheringComplete(formError)
                }
            },
            { requestConsentError ->
                onConsentGatheringCompleteListener.consentGatheringComplete(requestConsentError)
            },
        )
    }

    fun showPrivacyOptionsForm(
        activity: Activity,
        onConsentFormDismissedListener: ConsentForm.OnConsentFormDismissedListener,
    ) {
        UserMessagingPlatform.showPrivacyOptionsForm(activity, onConsentFormDismissedListener)
    }

    companion object {
        var canRequestAds = true

        @Volatile
        private var instance: ConsentManager? = null

        fun getInstance() =
            instance
                ?: synchronized(this) {
                    instance ?: ConsentManager().also { instance = it }
                }
    }
}
