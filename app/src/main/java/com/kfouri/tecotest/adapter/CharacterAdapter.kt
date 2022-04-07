package com.kfouri.tecotest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.kfouri.tecotest.R
import com.kfouri.tecotest.api.model.CharacterModel
import com.kfouri.tecotest.view.ui.GlideApp
import kotlinx.android.synthetic.main.item_list_content.view.container
import kotlinx.android.synthetic.main.item_list_content.view.imageView_thumbnail
import kotlinx.android.synthetic.main.item_list_content.view.textView_lastKnownLocationText
import kotlinx.android.synthetic.main.item_list_content.view.textView_name
import kotlinx.android.synthetic.main.item_list_content.view.textView_status

class CharacterAdapter(
    private val context: Context,
    private val clickListener: (String) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list = ArrayList<CharacterModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_content, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        (holder as ViewHolder).bind(item, context)
        holder.itemView.container.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.list_item_animation)
        holder.itemView.setOnClickListener { clickListener(item.id) }
    }

    fun setData(newList: List<CharacterModel>) {
        val oldCount = list.size
        list.addAll(newList)
        notifyItemRangeInserted(oldCount, list.size)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: CharacterModel, context: Context) {
            itemView.textView_name.text = item.name
            itemView.textView_status.text = context.getString(R.string.status, item.species, item.status, item.gender)
            itemView.textView_lastKnownLocationText.text = item.location.name
            GlideApp.with(itemView.imageView_thumbnail.context.applicationContext)
                .load(item.image)
                .into(itemView.imageView_thumbnail)
        }
    }
}