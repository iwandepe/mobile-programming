package com.iwan.ets

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContactListAdapater (
        private var contactList: List<ContactModel>,
        private val rowLayout: Int,
): RecyclerView.Adapter<ContactListAdapater.ViewHolder>() {

    var onItemClick: ((ContactModel) -> Unit)? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView
        val phone: TextView

        init {
            name = view.findViewById(R.id.tvName)
            phone = view.findViewById(R.id.tvPhone)

            view.setOnClickListener {
                onItemClick?.invoke(contactList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(rowLayout, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.name.text = contactList[position].name
        viewHolder.phone.text = contactList[position].phone
    }

    override fun getItemCount() = contactList.size

    fun setData(data: List<ContactModel>) {
        contactList = data
    }
}