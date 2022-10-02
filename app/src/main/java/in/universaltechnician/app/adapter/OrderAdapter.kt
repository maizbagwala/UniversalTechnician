package `in`.universaltechnician.app.adapter

import `in`.universaltechnician.app.OrderDetailActivity
import `in`.universaltechnician.app.R
import `in`.universaltechnician.app.model.Order
import `in`.universaltechnician.app.model.OrderResponse
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OrderAdapter(private val orderList: ArrayList<OrderResponse.Data>) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        )
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.tvName.text = orderList[position].name
        holder.tvPhone.text = orderList[position].phone
        holder.tvStatus.text = when (orderList[position].order_status) {

            "0" -> {
                holder.tvStatus.setBackgroundResource(R.color.orange)
                "Pending"
            }
            "1" -> {
                holder.tvStatus.setBackgroundResource(R.color.green)
                "Completed"
            }
            else -> {
                holder.tvStatus.setBackgroundResource(R.color.red)
                "Canceled"
            }
        }
        holder.tvDate.text = getStringToDate(orderList[position].order_time).toString()
        holder.itemView.setOnClickListener {
            holder.itemView.context.startActivity(
                Intent(
                    holder.itemView.context,
                    OrderDetailActivity::class.java
                ).putExtra("model",orderList[position])
            )
        }

    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    fun getStringToDate(dtStart:String): Date? {
        val format = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")

        return format.parse(dtStart)
    }
    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
        val tvPhone: TextView = itemView.findViewById(R.id.tv_number)
        val tvStatus: TextView = itemView.findViewById(R.id.tv_status)
        val tvDate: TextView = itemView.findViewById(R.id.tv_date)
    }
}