package com.example.linma9.mytechcruncharticlelistapplication.eventbus

/**
 * Created by linma9 on 2/13/18.
 */
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.reactivestreams.Subscription

// Use object so we have a singleton instance
object RxBus {

    private val publisher = PublishSubject.create<Any>()
            .toSerialized()
    /**
     *
     * PublishSubject.create<Any>() returns a == PublishSubject<T> extends Subject<T> ==
     *
     * the Subject<T>::toSerialized()
     * Wraps this Subject and serializes the calls to the onSubscribe, onNext, onError and
     * onComplete methods, making them thread-safe.
     * <p>The method is thread-safe.
     * @return the wrapped and serialized subject
     */

    fun publish(event: Any) {
        publisher.onNext(event)
    }

    // Listen should return an Observable and not the publisher
    // Using ofType we filter only events that match that class type
    fun <T> listen(eventType: Class<T>): Observable<T> = publisher.ofType(eventType)

}


//class RxBus2 {
//
//    private val mBusSubject = SerializedSubject(PublishSubject.create())
//
//    fun <T> register(eventClass: Class<T>, onNext: Action1<T>): Subscription {
//        return mBusSubject
//                .filter({ event -> event.getClass().equals(eventClass) })
//                .map({ obj -> obj })
//                .subscribe(onNext)
//    }
//
//    fun post(event: Any) {
//        mBusSubject.onNext(event)
//    }
//
//    companion object {
//
//        val instance = RxBus()
//    }
//}