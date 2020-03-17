package org.victor5171.revoluttest.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import javax.inject.Inject
import org.victor5171.revoluttest.BuildConfig
import org.victor5171.revoluttest.R
import org.victor5171.revoluttest.app.di.DaggerAppComponent
import org.victor5171.revoluttest.rateconversion.viewmodel.RateConversionViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
    }
}
