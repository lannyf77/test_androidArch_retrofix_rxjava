package om.example.linma9.mywctcokhttprecycleviewapplication.viewModel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log


/**
 * Created by linma9 on 1/23/18.
 */

class BundleAwareViewModelFactory<T : ParcelableViewModel>(private val bundle: Bundle?,
                                                           private val provider: ViewModelProvider.Factory) : ViewModelProvider.Factory {
    override fun <T : ViewModel>create(modelClass: Class<T>): T {
        val viewModel : T = provider.create(modelClass) as T

//        Log.e("eee888", "+++ +++ %%% BundleAwareViewModelFactory:create(), viewModel:"+viewModel+
//                "\nbundle==null:"+(bundle==null))

        if (bundle != null && viewModel is ParcelableViewModel) {

            //Log.d("eee888", "+++ +++ %%% BundleAwareViewModelFactory:create(), viewModel:"+viewModel)

            viewModel.readFrom(bundle)
        }
        return viewModel
    }
}

///
//class BundleAwareViewModelFactory<T : ParcelableViewModel>(bundle:Bundle?,
//                                                           provider:ViewModelProvider.Factory):ViewModelProvider.Factory {
//    private val bundle:Bundle?
//    private val provider:ViewModelProvider.Factory
//    init{
//        this.bundle = bundle
//        this.provider = provider
//    }
//    override fun create(modelClass:Class<*>):T {
//        val viewModel = provider.create(modelClass) as T
//        if (bundle != null)
//        {
//            viewModel.readFrom(bundle)
//        }
//        return viewModel
//    }
//}
//abstract class ParcelableViewModel:ViewModel() {
//    abstract fun writeTo(@NonNull bundle:Bundle)
//    abstract fun readFrom(@NonNull bundle:Bundle)
//}