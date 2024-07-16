package com.silvertown.android.dailyphrase.presentation.ui.event

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.silvertown.android.dailyphrase.presentation.R
import com.silvertown.android.dailyphrase.presentation.databinding.ItemNoneBinding
import com.silvertown.android.dailyphrase.presentation.databinding.ItemPrizeAfterWinningDrawBinding
import com.silvertown.android.dailyphrase.presentation.databinding.ItemPrizeBeforeWinningDrawBinding
import com.silvertown.android.dailyphrase.presentation.model.PrizeInfoUi

class PrizeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val prizes = mutableListOf<PrizeInfoUi.Item>()

    class PrizeViewHolder(private val binding: ItemPrizeBeforeWinningDrawBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(prize: PrizeInfoUi.Item.BeforeWinningDraw) {
            Glide.with(binding.ivPrize)
                .load(prize.imageUrl)
                .centerCrop()
                .override(262, 262)
                .into(binding.ivPrize)

            binding.tvTitle.text = prize.name
            binding.tvDescription.text = prize.manufacturer
            binding.tvRequiredTickets.text = itemView.context.getString(R.string.required_tickets, prize.requiredTicketCount)
            binding.tvNotEnoughEntryGuide.isVisible = !prize.hasEnoughEntry && !prize.isEventPeriodEnded
            binding.tvEventResultReleaseDateGuide.text = itemView.context.getString(R.string.event_result_release_date_guide, "6월 23일") // TODO JH: 임시 데이터 수정
            binding.tvEventResultReleaseDateGuide.isVisible = prize.isEventPeriodEnded
        }
    }

    class AfterWinningDrawViewHolder(private val binding: ItemPrizeAfterWinningDrawBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(prize: PrizeInfoUi.Item.AfterWinningDraw) {
            Glide.with(binding.ivPrize)
                .load(prize.imageUrl)
                .centerCrop()
                .override(262, 262)
                .into(binding.ivPrize)

            binding.tvTitle.text = prize.name
        }
    }

    class UnknownViewHolder(binding: ItemNoneBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return when (prizes[position % prizes.size]) {
            is PrizeInfoUi.Item.AfterWinningDraw -> Type.AFTER_WINNING_DRAW.value
            is PrizeInfoUi.Item.BeforeWinningDraw -> Type.BEFORE_WINNING_DRAW.value
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (Type.ofValue(viewType)) {
            Type.BEFORE_WINNING_DRAW -> PrizeViewHolder(
                ItemPrizeBeforeWinningDrawBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            )
            Type.AFTER_WINNING_DRAW -> AfterWinningDrawViewHolder(
                ItemPrizeAfterWinningDrawBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            )
            Type.UNKNOWN -> UnknownViewHolder(
                ItemNoneBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            )
        }
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PrizeViewHolder -> holder.bind(prizes[position % prizes.size] as PrizeInfoUi.Item.BeforeWinningDraw)
            is AfterWinningDrawViewHolder -> holder.bind(prizes[position % prizes.size] as PrizeInfoUi.Item.AfterWinningDraw)
            else -> Unit
        }
    }

    fun setList(list: List<PrizeInfoUi.Item>) {
        prizes.clear()
        prizes.addAll(list)
        notifyDataSetChanged()
    }

    enum class Type(val value: Int) {
        BEFORE_WINNING_DRAW(0),
        AFTER_WINNING_DRAW(1),
        UNKNOWN(-1);

        companion object {
            fun ofValue(value: Int): Type {
                return entries.find { it.value == value } ?: UNKNOWN
            }
        }
    }
}
