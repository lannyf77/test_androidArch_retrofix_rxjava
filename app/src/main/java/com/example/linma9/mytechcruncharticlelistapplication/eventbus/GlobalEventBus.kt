package com.example.linma9.mytechcruncharticlelistapplication.eventbus

import android.util.Log
//import com.squareup.otto.Bus
//import com.squareup.otto.ThreadEnforcer

//import de.greenrobot.event.EventBus

/**
 * Created by linma9 on 1/23/18.
 */


/**
 * Maintains a singleton instance for obtaining the bus. Ideally this would be replaced with a more efficient means
 * such as through injection directly into interested classes.
 */
//object BusProvider {
//    val instance = Bus()
//}// No instances.

//class GlobalEventBus {
//
//    companion object {
//
////        // greenrobot EventBus
////        private var bus: EventBus? = null
////        val instance: EventBus
////            @Synchronized get() {
////                if (bus == null) {
////                    bus = EventBus.getDefault()
////                    //Log.i("eee888", "+++ +++ @@@ 111 GlobalEventBus:ctor(), bus: ${bus}");
////                }
////                //Log.i("eee888", "+++ +++ @@@ 222 GlobalEventBus:ctor(), has bus: ${bus}");
////                return bus!!
////            }
//    }
//
//    // otto
////    companion object {
////
////        private var bus: Bus? = null
////        val instance: Bus
////            @Synchronized get() {
////                if (bus == null) {
////                    bus = Bus(ThreadEnforcer.ANY)
////                    //Log.i("eee888", "+++ +++ @@@ 111 GlobalEventBus:ctor(), bus: ${bus}");
////                }
////                //Log.i("eee888", "+++ +++ @@@ 222 GlobalEventBus:ctor(), has bus: ${bus}");
////                return bus!!
////            }
////    }
//
//}