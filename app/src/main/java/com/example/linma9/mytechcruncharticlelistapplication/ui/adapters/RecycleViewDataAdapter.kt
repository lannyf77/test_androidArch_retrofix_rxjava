package com.example.linma9.mykotlinapplication2.features.news.adapter

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ViewGroup
import com.example.linma9.mytechcruncharticlelistapplication.commons.extensions.adapterInterfaces.ViewTypeConstants
import com.example.linma9.mytechcruncharticlelistapplication.commons.extensions.adapterInterfaces.ViewType
import com.example.linma9.mytechcruncharticlelistapplication.commons.extensions.adapterInterfaces.ViewTypeDelegateAdapter
import com.example.linma9.mykotlinapplication2.features.news.LoadingDelegateAdapter
import com.example.linma9.mytechcruncharticlelistapplication.model.repository.CTViewDataItem

/**
 * Created by linma9 on 1/23/18.
 */

class RecycleViewDataAdapter(val listener: ArticleDelegateAdapter.onViewSelectedListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: ArrayList<ViewType>

    private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()// mapping type to its 'adapter'


    /**
     * Object Expressions
    In Kotlin you have something called “Object expressions”, which works in a similar way as anonymous inner classes in Java,
    and allows you to create an object without explicitly declaring a new subclass for it.
    In this case we are using it to define our loadingItem without creating a new class.
    The syntax is really intuitive and as you can see we are extending from ViewType and implementing the required interface.
    Single Expressions
    The getViewType() method has only a single expression function inside the body.
    In Kotlin we can take advantage of this and convert this method:
     */

    /**
     * for 'loading' to defines a object derive from ViewType interface with override of the getViewType()
     */
    private val loadingItem = object : ViewType {
        override fun getViewType() = ViewTypeConstants.SPINNER

        /*
        override fun getViewType() : Int {
            return ViewTypeConstants.SPINNER  //single expression funcion
}       }
         */
    }

    init {
        delegateAdapters.put(ViewTypeConstants.SPINNER, LoadingDelegateAdapter())
        delegateAdapters.put(ViewTypeConstants.ARTICL, ArticleDelegateAdapter(listener))
        items = ArrayList()
        items.add(loadingItem)

        //Log.e("eee888", "+++ +++ RecycleViewDataAdapter:init() this: $this"+", delegateAdapters:"+delegateAdapters.size())
    }

    override fun getItemCount(): Int {

        ////Log.e("eee888", "+++ +++ RecycleViewDataAdapter:getItemCount() items.size: ${items.size}")

        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return items?.get(position)?.getViewType() ?: ViewTypeConstants.SPINNER;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


//        //Log.e("eee888", "+++ +++ RecycleViewDataAdapter:onCreateViewHolder() viewType: $viewType"+"\nthis:"+this+
//                "\ndelegateAdapters:"+delegateAdapters+
//                ", delegateAdapters.get(viewType):"+(delegateAdapters?.get(viewType) ?: " !!! delegateAdapters==null"))

        return delegateAdapters.get(viewType).onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder, this.items[position])
    }

    fun addArticls(articls: List<CTViewDataItem>) {
        // first remove loading and notify
        val initPosition = items.size - 1
        // insert new articls before the loading (spinner) item
        items.addAll(initPosition, articls)

        // partially notify does not cuase the recycleview to fully update, the list does not show the new added items
        //notifyItemRangeChanged(initPosition, items.size + 1 /* plus loading item */)
        notifyDataSetChanged()

        //Log.i("eee888", "+++ +++ %%% <<<>>> RecycleViewDataAdapter:addArticls() this: $this"+", items:"+items.size)


    }

    fun addArticls_org(articls: List<CTViewDataItem>) {
        // first remove loading and notify
        val initPosition = items.size - 1
        items.removeAt(initPosition)
        notifyItemRemoved(initPosition)

        // insert articls and the loading at the end of the list
        items.addAll(articls)
        items.add(loadingItem)
        notifyItemRangeChanged(initPosition, items.size + 1 /* plus loading item */)
    }

    fun clearAndAddArticls(articls: List<CTViewDataItem>) {

        var testOldItemSize = items.size

        items.clear()
        //notifyItemRangeRemoved(0, getLastPosition())

        items.addAll(articls)
        items.add(loadingItem)

        //Log.i("eee888", "+++ +++ %%% clearAndAddArticls(), testOldItemSize: $testOldItemSize, items.size: ${items.size}")

        notifyDataSetChanged()  //notifyItemRangeInserted(0, items.size)

    }

    fun getArticls(): List<CTViewDataItem> {
        return items
                .filter { it.getViewType() == ViewTypeConstants.ARTICL }
                .map { it as CTViewDataItem }
    }

    private fun getLastPosition() = if (items.lastIndex == -1) 0 else items.lastIndex

}