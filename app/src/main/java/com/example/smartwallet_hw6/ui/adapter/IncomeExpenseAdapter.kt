package com.example.smartwallet_hw6.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwallet_hw6.R
import com.example.smartwallet_hw6.databinding.RvItemBinding
import com.example.smartwallet_hw6.model.data.IncomeExpense

class IncomeExpenseAdapter(
    private val onItemClick: (IncomeExpense) -> Unit,
    private val onDeleteClick: (String) -> Unit
) : ListAdapter<IncomeExpense, IncomeExpenseAdapter.IncomeExpenseVH>(IncomeExpenseDiffCallBack()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncomeExpenseVH =
        IncomeExpenseVH(
            RvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onItemClick,
            onDeleteClick
        )


    override fun onBindViewHolder(holder: IncomeExpenseVH, position: Int) =
        holder.bind(getItem(position))


    class IncomeExpenseVH(
        private val binding: RvItemBinding,
        private val onItemClick: (IncomeExpense) -> Unit,
        private val onDeleteClick: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(incomeExpense: IncomeExpense) {
            with(binding) {

                tvTitle.text = incomeExpense.title.toString().capitalize()
                tvCost.text = incomeExpense.cost.toString()
                tvDate.text = incomeExpense.date.toString()

                if (incomeExpense.type == true) {
                    tvCost.text = "-${incomeExpense.cost.toString()}"
                    tvCost.setTextColor(tvCost.context.resources.getColor(R.color.red))
                } else {
                    tvCost.text = "+${incomeExpense.cost.toString()}"
                    tvCost.setTextColor(tvCost.context.resources.getColor(R.color.green))
                }


                ivDelete.setOnClickListener {
                    incomeExpense.documentId?.let(onDeleteClick)
                }

                root.setOnClickListener {
                    onItemClick(incomeExpense)
                }
            }
        }

    }


    class IncomeExpenseDiffCallBack : DiffUtil.ItemCallback<IncomeExpense>() {
        override fun areItemsTheSame(oldItem: IncomeExpense, newItem: IncomeExpense): Boolean {
            return oldItem.documentId == newItem.documentId
        }

        override fun areContentsTheSame(oldItem: IncomeExpense, newItem: IncomeExpense): Boolean {
            return oldItem == newItem
        }
    }


}