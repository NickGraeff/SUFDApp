package com.nicksnacs.smashultimateframedata.character_page

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IntDef
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nicksnacs.smashultimateframedata.R
import com.nicksnacs.smashultimateframedata.databinding.RowAttackBinding
import com.nicksnacs.smashultimateframedata.databinding.RowDodgeRollBinding
import com.nicksnacs.smashultimateframedata.databinding.RowGrabThrowBinding
import com.nicksnacs.smashultimateframedata.databinding.RowMiscBinding
import com.nicksnacs.smashultimateframedata.db.*


class MoveAdapter : RecyclerView.Adapter<MoveAdapter.GenericViewHolder>(), Observer<DetailedCharacterInformation?> {
    abstract class GenericViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(type: Any?)
    }
    abstract class MoveViewHolder<V: ViewDataBinding>(val context: Context, val binding: V) : GenericViewHolder(binding.root)

    class AttackViewHolder(context: Context, rowAttackBinding: RowAttackBinding) : MoveViewHolder<RowAttackBinding>(context, rowAttackBinding) {
        override fun bind(attack: Any?) {
            binding.attack = (attack as Attack).apply {
                hitboxImageUrl?.let {
                    Glide.with(binding.moveIcon.context).load(it).into(binding.moveIcon)
                }
            }
            binding.executePendingBindings()
        }
    }

    class GrabThrowViewHolder(context: Context, rowGrabThrowBinding: RowGrabThrowBinding) : MoveViewHolder<RowGrabThrowBinding>(context, rowGrabThrowBinding) {
        override fun bind(grabThrow: Any?) {
            binding.grabThrow = (grabThrow as GrabThrow).apply {
                hitboxImageUrl?.let {
                    Glide.with(binding.moveIcon.context).load(it).into(binding.moveIcon)
                }
            }
            binding.executePendingBindings()
        }
    }

    class DodgeRollViewHolder(context: Context, rowDodgeRollBinding: RowDodgeRollBinding) : MoveViewHolder<RowDodgeRollBinding>(context, rowDodgeRollBinding) {
        override fun bind(dodgeRoll: Any?) {
            binding.dodgeRoll = (dodgeRoll as DodgeRoll).apply {
                hitboxImageUrl?.let {
                    Glide.with(binding.moveIcon.context).load(it).into(binding.moveIcon)
                }
            }
            binding.executePendingBindings()
        }
    }

    class MiscViewHolder(context: Context, rowMiscBinding: RowMiscBinding) : MoveViewHolder<RowMiscBinding>(context, rowMiscBinding) {
        override fun bind(misc: Any?) {
            binding.misc = (misc as List<Misc>).apply {
                // We assume that this is a misc view
                // Grab the linear layout and inject a bunch of TextViews
                val linearLayout = binding.moveMisc
                for (misc: Misc in this) {
                    val textView = TextView(context)
                    textView.text = misc.misc
                    linearLayout.addView(textView)
                }
            }
            binding.executePendingBindings()
        }
    }

    private var detailedCharacterInformation: DetailedCharacterInformation? = null

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ATTACK_VIEW -> DataBindingUtil.inflate<RowAttackBinding>(
                layoutInflater,
                R.layout.row_attack,
                parent,
                false).run {
                AttackViewHolder(parent.context, this)
            }
            GRAB_THROW_VIEW -> DataBindingUtil.inflate<RowGrabThrowBinding>(
                layoutInflater,
                R.layout.row_grab_throw,
                parent,
                false).run {
                GrabThrowViewHolder(parent.context, this)
            }
            DODGE_ROLL_VIEW -> DataBindingUtil.inflate<RowDodgeRollBinding>(
                layoutInflater,
                R.layout.row_dodge_roll,
                parent,
                false).run {
                DodgeRollViewHolder(parent.context, this)
            }
            MISC_VIEW -> DataBindingUtil.inflate<RowMiscBinding>(
                layoutInflater,
                R.layout.row_misc,
                parent,
                false).run {
                MiscViewHolder(parent.context, this)
            }
            else -> throw IllegalStateException("ViewHolder cannot be created for this viewType")
        }
    }

    override fun onChanged(detailedCharacterInformation: DetailedCharacterInformation?) {
        this.detailedCharacterInformation = detailedCharacterInformation
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: GenericViewHolder, position: Int) {
        detailedCharacterInformation?.apply {
            holder.bind(get(position))
        }
    }

    companion object {
        private const val ATTACK_VIEW: Int = 0
        private const val GRAB_THROW_VIEW: Int = 1
        private const val DODGE_ROLL_VIEW: Int = 2
        private const val MISC_VIEW: Int = 3
    }
    @IntDef(value = [ATTACK_VIEW, GRAB_THROW_VIEW, DODGE_ROLL_VIEW, MISC_VIEW])
    annotation class ViewHolderType

    @ViewHolderType
    override fun getItemViewType(position: Int): Int {
        var length = 0
        var currentLength = detailedCharacterInformation!!.groundAttacks.size
        if (position < length+currentLength) return ATTACK_VIEW
        length += currentLength
        currentLength = detailedCharacterInformation!!.aerialAttacks.size
        if (position < length+currentLength) return ATTACK_VIEW
        length += currentLength
        currentLength = detailedCharacterInformation!!.specialAttacks.size
        if (position < length+currentLength) return ATTACK_VIEW
        length += currentLength
        currentLength = detailedCharacterInformation!!.grabThrows.size
        if (position < length+currentLength) return GRAB_THROW_VIEW
        length += currentLength
        currentLength = detailedCharacterInformation!!.dodgeRolls.size
        if (position < length+currentLength) return DODGE_ROLL_VIEW
        length += currentLength
        currentLength = 1
        if (position < length+currentLength) return MISC_VIEW
        throw IllegalArgumentException("Position cannot be mapped to a view type")
    }

    override fun getItemCount(): Int {
        return detailedCharacterInformation?.size ?: 0
    }

    fun <T> get(position: Int) : T {
        var length = 0
        var currentLength = detailedCharacterInformation!!.groundAttacks.size
        if (position < length+currentLength) return detailedCharacterInformation!!.groundAttacks[position-length] as T
        length += currentLength
        currentLength = detailedCharacterInformation!!.aerialAttacks.size
        if (position < length+currentLength) return detailedCharacterInformation!!.aerialAttacks[position-length] as T
        length += currentLength
        currentLength = detailedCharacterInformation!!.specialAttacks.size
        if (position < length+currentLength) return detailedCharacterInformation!!.specialAttacks[position-length] as T
        length += currentLength
        currentLength = detailedCharacterInformation!!.grabThrows.size
        if (position < length+currentLength) return detailedCharacterInformation!!.grabThrows[position-length] as T
        length += currentLength
        currentLength = detailedCharacterInformation!!.dodgeRolls.size
        if (position < length+currentLength) return detailedCharacterInformation!!.dodgeRolls[position-length] as T
        length += currentLength
        currentLength = 1
        if (position < length+currentLength) return detailedCharacterInformation!!.misc as T
        throw IllegalArgumentException("Position cannot be mapped to a view type")
    }
}