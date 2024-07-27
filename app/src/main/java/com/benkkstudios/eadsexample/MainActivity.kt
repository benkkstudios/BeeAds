package com.benkkstudios.eadsexample


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.benkkstudios.ads.core.BannerAdCompose
import com.benkkstudios.ads.core.BeeAds
import com.benkkstudios.ads.core.showInterstitial
import com.benkkstudios.ads.core.showReward
import com.benkkstudios.eadsexample.ui.theme.BeeAdsExampleTheme
import com.benkkstudios.eadsexample.units.AdmobUnit
import com.benkkstudios.eadsexample.units.MaxUnit
import com.benkkstudios.eadsexample.units.MetaUnit
import com.benkkstudios.eadsexample.units.WortiseUnit
import com.benkkstudios.eadsexample.units.YandexUnit


class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            BeeAdsExampleTheme {
                Scaffold(Modifier.fillMaxSize()) {
                    val activity = LocalContext.current as Activity
                    val beeAdsInitialized by BeeAds.isInitialized.collectAsState()
                    LaunchedEffect(beeAdsInitialized) {
                        BeeAds.Builder(activity)
                            .primaryProvider("YANDEX", YandexUnit())
                            .appOpen(App.getInstance())
                            .interval(5)
                            .debugMode(true)
                            .build()
                    }
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.align(Alignment.Center)) {
                            Button(onClick = {
                                startActivity(Intent(this@MainActivity, SecondActivity::class.java))
                            }) {
                                Text(text = "open second", modifier = Modifier.padding(10.dp))
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            Button(onClick = {
                                activity.showInterstitial {
                                    //on interstitial closed
                                }
                            }) {
                                Text(text = "show inter", modifier = Modifier.padding(10.dp))
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            Button(onClick = {
                                activity.showReward {
                                    //on reward closed
                                }
                            }) {
                                Text(text = "show reward", modifier = Modifier.padding(10.dp))
                            }
                        }
                        if (beeAdsInitialized) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomCenter)
                                    .background(Color.White)
                            ) {
                                BannerAdCompose()
                                //   BeeAds.BannerCompose()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
