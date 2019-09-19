package `fun`.gladkikh.fastpallet5.ui


import `fun`.gladkikh.fastpallet5.R
import `fun`.gladkikh.fastpallet5.domain.intety.Box
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.DecimalFormat


class BoxAdapter : RecyclerView.Adapter<BoxAdapter.ViewHolder>() {

    private var list: List<Box> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_box, parent, false)
        return ViewHolder(itemView)
    }

    fun updateData(newList: List<Box>) {
        list = newList
        notifyDataSetChanged()
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvName?.text = list[position].data.toString()
        holder.tvCode?.text = list[position].countBox.toString()


        val format = DecimalFormat("#,###")

        holder.tvPrice?.text =
            format.format(list[position].weight)
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView? = null
        var tvCode: TextView? = null
        var tvPrice: TextView? = null

        init {
            tvName = itemView.findViewById(R.id.name)
            tvCode = itemView.findViewById(R.id.code)
            tvPrice = itemView.findViewById(R.id.price)
        }

    }

}