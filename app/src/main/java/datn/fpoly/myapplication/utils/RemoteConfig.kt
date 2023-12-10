package datn.fpoly.myapplication.utils

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

object RemoteConfig {
    private lateinit var remoteConfig: FirebaseRemoteConfig

    init {
        initConfig()
    }

    private fun initConfig() {
        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        val defaults: Map<String, Any> = mapOf(
            "image_urls" to listOf(
                "https://asiatechjsc.vn/cdn/shop/articles/7ac3212def22e3c7b4b6c6805cb01bf5.jpg?v=1610419220",
                "https://channel.mediacdn.vn/2019/9/9/photo-2-15680177604931126060018.jpg",
                "https://giatlatokyo.com/wp-content/uploads/2018/09/dich-vu-giat-kho-la-hoi.jpg"
            )
        )
        remoteConfig.setDefaultsAsync(defaults)
    }

    fun getListImage(completion: (List<String>) -> Unit) {
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val search = remoteConfig.getString("slider_image")
                val imageUrlList = search.split(",")
                completion(imageUrlList)
//                Log.d("SearchActivity", "initConfig: $imageUrlList")
            } else {
//                Log.d("TAG", "initConfig: ${task.exception}")
            }
        }
    }
}