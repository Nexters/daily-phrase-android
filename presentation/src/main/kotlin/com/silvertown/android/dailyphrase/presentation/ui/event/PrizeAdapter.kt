package com.silvertown.android.dailyphrase.presentation.ui.event

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.silvertown.android.dailyphrase.presentation.R
import com.silvertown.android.dailyphrase.presentation.databinding.ItemPrizeBinding
import com.silvertown.android.dailyphrase.presentation.model.PrizeInfoUi

class PrizeAdapter : RecyclerView.Adapter<PrizeAdapter.PrizeViewHolder>() {
    private val prizes = mutableListOf<PrizeInfoUi.Item>()

    class PrizeViewHolder(private val binding: ItemPrizeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(prize: PrizeInfoUi.Item) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrizeViewHolder {
        return PrizeViewHolder(
            ItemPrizeBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        )
    }

    override fun getItemCount(): Int = prizes.size

    override fun onBindViewHolder(holder: PrizeViewHolder, position: Int) {
        holder.bind(prizes[position])
    }

    fun setList(list: List<PrizeInfoUi.Item>) {
        prizes.clear()
        prizes.addAll(list)
        notifyDataSetChanged()
    }
}
