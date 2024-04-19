package pe.edu.idat.toinvoicemobileapp.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class SingleLiveEvent<T> : MutableLiveData<T>() {
    private val pendingObservers = mutableMapOf<Observer<in T>, Boolean>()

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, Observer { value ->
            if (pendingObservers[observer] != true) {
                observer.onChanged(value)
                pendingObservers[observer] = true
            }
        })
    }

    override fun observeForever(observer: Observer<in T>) {
        super.observeForever(Observer { value ->
            if (pendingObservers[observer] != true) {
                observer.onChanged(value)
                pendingObservers[observer] = true
            }
        })
    }

    override fun removeObserver(observer: Observer<in T>) {
        pendingObservers.remove(observer)
        super.removeObserver(observer)
    }

    fun call() {
        value = null
    }
}
