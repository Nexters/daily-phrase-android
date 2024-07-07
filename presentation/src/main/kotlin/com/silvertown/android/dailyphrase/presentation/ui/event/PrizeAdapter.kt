package com.silvertown.android.dailyphrase.presentation.ui.event

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.silvertown.android.dailyphrase.presentation.R
import com.silvertown.android.dailyphrase.presentation.databinding.ItemPrizeBinding

class PrizeAdapter : RecyclerView.Adapter<PrizeAdapter.PrizeViewHolder>() {
    private val prizes = mutableListOf<PrizeInfo.Prize>()

    class PrizeViewHolder(private val binding: ItemPrizeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(prize: PrizeInfo.Prize) {
            Glide.with(binding.ivPrize)
                .load(prize.imageUrl)
                .centerCrop()
                .override(262, 262)
                .into(binding.ivPrize)

            binding.tvTitle.text = prize.name
            binding.tvDescription.text = prize.manufacturer
            binding.tvRequiredTickets.text = itemView.context.getString(R.string.required_tickets, prize.requiredTicketCount)
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

    fun setList(list: List<PrizeInfo.Prize>) {
        prizes.clear()
        prizes.addAll(list)
        notifyDataSetChanged()
    }
}
