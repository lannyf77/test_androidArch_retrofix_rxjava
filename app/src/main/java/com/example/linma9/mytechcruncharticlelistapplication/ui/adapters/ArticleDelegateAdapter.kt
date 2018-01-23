package com.example.linma9.mykotlinapplication2.features.news.adapter

import android.os.Build
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.ViewGroup

import com.example.linma9.mytechcruncharticlelistapplication.commons.extensions.adapterInterfaces.ViewType
import com.example.linma9.mytechcruncharticlelistapplication.commons.extensions.adapterInterfaces.ViewTypeDelegateAdapter
import com.example.linma9.mytechcruncharticlelistapplication.commons.extensions.getFriendlyTime
import com.example.linma9.mytechcruncharticlelistapplication.R
import com.example.linma9.mytechcruncharticlelistapplication.commons.extensions.inflate
import com.example.linma9.mytechcruncharticlelistapplication.commons.extensions.loadImg
import com.example.linma9.mytechcruncharticlelistapplication.model.repository.CTViewDataItem

import kotlinx.android.synthetic.main.articls_item.view.*
import android.support.annotation.ColorInt
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.shapes.RectShape
import android.graphics.drawable.ShapeDrawable
import android.content.res.ColorStateList
import android.annotation.TargetApi
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.StateListDrawable
import android.graphics.drawable.ColorDrawable
import android.view.View


/**
 * Created by linma9 on 1/23/18.
 */

class ArticleDelegateAdapter(val viewActions: onViewSelectedListener) : ViewTypeDelegateAdapter {

    interface onViewSelectedListener {
        fun onItemSelected(url: String?)
        fun filterOn(filter: Int)
        fun filterOnCategory(filter: String?)
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {

        var articlHoder = ArticleViewHolder(parent)
        if (Build.VERSION.SDK_INT >= 16) {
            articlHoder.itemView.setBackground(getColorDrawable(articlHoder.itemView.getContext().getResources().getColor(R.color.mail_purple_color), true));
        }
        return articlHoder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        holder as ArticleViewHolder
        holder.bind(item as CTViewDataItem)
    }

    inner class ArticleViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            parent.inflate(R.layout.articls_item_c2)) {

        fun bind(item: CTViewDataItem) = with(itemView) {
            //Picasso.with(itemView.context).load(item.thumbnail).into(img_thumbnail)
            img_thumbnail.loadImg(item.thumbnail)
            description.text = Html.fromHtml(item.title)
            author.text = item.author
            time.text = item.created.getFriendlyTime()
            snippet.text = Html.fromHtml(item.snippet)
            categories.text = item.categories
            comments.text = ""

            ///
            //public static void addTouchFeedback(View view, @ColorRes int selectionColor, boolean bounded) {
            if (Build.VERSION.SDK_INT >= 16) {
                itemView.setBackground(getColorDrawable(itemView.getContext().getResources().getColor(R.color.mail_purple_color), true));
                img_thumbnail.setBackground(getColorDrawable(itemView.getContext().getResources().getColor(R.color.colorAccent), true));
                categories.setBackground(getColorDrawable(itemView.getContext().getResources().getColor(R.color.mail_purple_color), true));
            }
            ///

            img_thumbnail.setOnClickListener(View.OnClickListener {
                postDelayed(Runnable { viewActions.filterOn(item.authorId) }, 200)
            })

            categories.setPaintFlags(categories.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)

            categories.setOnClickListener(View.OnClickListener {
                postDelayed(Runnable { viewActions.filterOnCategory(item.categories) }, 200)
            })

            super.itemView.setOnClickListener {
                viewActions.onItemSelected(item.url)
            }
        }
    }

    /**********************************************************/
    // belong to util
    private fun getColorDrawableFromColor(@ColorInt color: Int): ColorDrawable {
        return ColorDrawable(color)
    }
    private fun getPressedSelectorDrawable(@ColorInt pressedColor: Int): Drawable {
        var pressedColor = pressedColor
        val stateListDrawable = StateListDrawable()

        // Add transparency to the color.
        pressedColor = Color.argb(100, Color.red(pressedColor), Color.green(pressedColor), Color.blue(pressedColor))

        stateListDrawable.addState(intArrayOf(android.R.attr.state_pressed), getColorDrawableFromColor(pressedColor))
        stateListDrawable.addState(intArrayOf(android.R.attr.state_focused), getColorDrawableFromColor(pressedColor))
        return stateListDrawable
    }

    private fun getColorDrawable(@ColorInt pressedColor: Int, bounded: Boolean): Drawable {
        return if (Build.VERSION.SDK_INT >= 21) {
            getPressedColorRippleDrawable(pressedColor, bounded)
        } else {
            getPressedSelectorDrawable(pressedColor)
        }
    }

    private fun getPressedColorSelector(@ColorInt pressedColor: Int): ColorStateList {
        return ColorStateList(
                arrayOf(intArrayOf()),
                intArrayOf(pressedColor)
        )
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getPressedColorRippleDrawable(@ColorInt pressedColor: Int, masked: Boolean): Drawable {
        val rippleColor = getPressedColorSelector(pressedColor)

        var maskDrawable: Drawable? = null
        if (masked) {
            maskDrawable = ShapeDrawable(RectShape())
        }

        return RippleDrawable(rippleColor, null, maskDrawable)
    }
}