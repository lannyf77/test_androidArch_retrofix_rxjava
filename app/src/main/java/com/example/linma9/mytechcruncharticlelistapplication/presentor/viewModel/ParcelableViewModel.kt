package om.example.linma9.mywctcokhttprecycleviewapplication.viewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModel
import android.os.Bundle

/**
 * Created by linma9 on 1/23/18.
 */

abstract class ParcelableViewModel(application: Application) : AndroidViewModel(application) {
    abstract fun writeTo(bundle: Bundle)
    abstract fun readFrom(bundle: Bundle)
}