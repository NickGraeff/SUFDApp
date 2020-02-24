package com.nicksnacs.smashultimateframedata.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nicksnacs.smashultimateframedata.R
import com.nicksnacs.smashultimateframedata.databinding.RowCharacterBinding
import com.nicksnacs.smashultimateframedata.db.BasicCharacterInformation


class CharacterAdapter : RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>, Observer<List<BasicCharacterInformation>?> {
    class CharacterViewHolder : RecyclerView.ViewHolder, View.OnClickListener {
        private val binding: RowCharacterBinding
        private val clickListener: ClickListener
        constructor(binding: RowCharacterBinding, clickListener: ClickListener) : super(binding.root) {
            this.binding = binding
            this.clickListener = clickListener
            binding.root.setOnClickListener(this)
        }

        fun bind(basicCharacterInformation: BasicCharacterInformation) {
            binding.character = basicCharacterInformation
            Glide.with(binding.characterIcon.context).load(basicCharacterInformation.imageUrl).into(binding.characterIcon)
            binding.executePendingBindings()
        }

        override fun onClick(v: View) {
            clickListener.characterClicked(adapterPosition)
        }
    }

    private var basicCharacterInformations: List<BasicCharacterInformation>? = null
    private val clickListener: ClickListener
    constructor(clickListener: ClickListener) {
        this.clickListener = clickListener
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<RowCharacterBinding>(
            layoutInflater,
            R.layout.row_character,
            parent,
            false
        )

        return CharacterViewHolder(binding, clickListener)
    }

    override fun onChanged(basicCharacterInformations: List<BasicCharacterInformation>?) {
        this.basicCharacterInformations = basicCharacterInformations
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        basicCharacterInformations?.apply {
            holder.bind(get(position))
        }
    }

    override fun getItemCount(): Int {
        return basicCharacterInformations?.size ?: 0
    }

    interface ClickListener {
        fun characterClicked(position: Int)
    }
}