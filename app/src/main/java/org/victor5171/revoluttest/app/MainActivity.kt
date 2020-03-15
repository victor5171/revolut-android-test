package org.victor5171.revoluttest.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import javax.inject.Inject
import org.victor5171.revoluttest.BuildConfig
import org.victor5171.revoluttest.R
import org.victor5171.revoluttest.app.di.DaggerAppComponent
import org.victor5171.revoluttest.rateconversion.RateConversionViewModel

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: RateConversionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerAppComponent.factory()
            .create(application, BuildConfig.API_BASE_URL)
            .inject(this)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
    }
}
