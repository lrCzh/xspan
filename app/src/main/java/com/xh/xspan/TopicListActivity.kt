package com.xh.xspan

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.xh.xspan.databinding.ActivityListBinding
import com.xh.xspan.databinding.ItemTvBinding

class TopicListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            val adapter = TopicAdapter {
                setResult(RESULT_OK, Intent().apply { putExtra("topic", it.name) })
                finish()
            }
            rv.adapter = adapter

            val topicList = mutableListOf<Topic>().apply {
                add(Topic("Android", 1))
                add(Topic("Google", 2))
                add(Topic("Kotlin", 3))
                add(Topic("Okhttp", 4))
                add(Topic("Retrofit", 5))
            }
            adapter.setData(topicList)
        }
    }

    class TopicAdapter(private val onClick: (Topic) -> Unit) :
        RecyclerView.Adapter<TopicAdapter.ViewHolder>() {

        private val data = mutableListOf<Topic>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val itemTvBinding = ItemTvBinding.inflate(inflater, parent, false)
            return ViewHolder(itemTvBinding, onClick)
        }

        override fun getItemCount(): Int {
            return data.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(data[position])
        }

        @SuppressLint("NotifyDataSetChanged")
        fun setData(list: List<Topic>) {
            this.data.clear()
            this.data.addAll(list)
            notifyDataSetChanged()
        }

        class ViewHolder(
            private val itemTvBinding: ItemTvBinding,
            private val onClick: (Topic) -> Unit
        ) : RecyclerView.ViewHolder(itemTvBinding.root) {

            private lateinit var current: Topic

            init {
                itemView.setOnClickListener {
                    onClick.invoke(current)
                }
            }

            fun bind(data: Topic) {
                this.current = data
                itemTvBinding.tv.text = data.name
            }
        }
    }

    companion object {

        fun actionStart(activity: Activity, requestCode: Int) {
            activity.startActivityForResult(
                Intent(activity, TopicListActivity::class.java),
                requestCode
            )
        }
    }
}