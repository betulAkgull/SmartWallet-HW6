package com.example.smartwallet_hw6.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwallet_hw6.databinding.RvItemBinding
import com.example.smartwallet_hw6.model.data.IncomeExpense

class IncomeExpenseAdapter(
    private val incomeExpenseListener: IncomeExpenseListener
) : ListAdapter<IncomeExpense, IncomeExpenseAdapter.IncomeExpenseVH>(IncomeExpenseDiffCallBack()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncomeExpenseVH =
        IncomeExpenseVH(
            RvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            incomeExpenseListener
        )


    override fun onBindViewHolder(holder: IncomeExpenseVH, position: Int) = holder.bind(getItem(position))


    class IncomeExpenseVH(
        private val binding: RvItemBinding,
        private val incomeExpenseListener: IncomeExpenseListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(incomeExpense: IncomeExpense){
            with(binding){
                tvTitle.text = incomeExpense.title.toString()
                tvCost.text = incomeExpense.cost.toString()
                tvDate.text = incomeExpense.date.toString()

                ivDelete.setOnClickListener {
                    incomeExpenseListener.onItemDeleteClicked()
                }

                root.setOnClickListener {
                    incomeExpenseListener.onItemClick()
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

    interface IncomeExpenseListener {
        fun onItemClick()
        fun onItemDeleteClicked()
    }


}