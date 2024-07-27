package com.benkkstudios.eadsexample

import android.annotation.SuppressLint
import android.app.Activity
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.benkkstudios.ads.core.BeeAds
import com.benkkstudios.ads.core.NativeAdCompose
import com.benkkstudios.eadsexample.ui.theme.BeeAdsExampleTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class SecondActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BeeAdsExampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    val activity = LocalContext.current as Activity
                    val beeAdsInitialized by BeeAds.isInitialized.collectAsState()
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.align(Alignment.Center)) {
                            Button(onClick = {
                                BeeAds.showInterstitial(activity) {

                                }
                            }) {
                                Text(text = "show inter", modifier = Modifier.padding(10.dp))
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            Button(onClick = {
                                BeeAds.showReward(this@SecondActivity) {

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
                                NativeAdCompose()
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
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BeeAdsExampleTheme {
        Greeting2("Android")
    }
}