package com.xh.xspan

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.xh.xspan.databinding.ActivityMainBinding
import com.xh.xspan.xspan.SpecialChar
import com.xh.xspan.xspan.data.Topic
import com.xh.xspan.xspan.data.User
import com.xh.xspan.xspan.span.AtSpan
import com.xh.xspan.xspan.span.TopicSpan
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            et.setOnSpecialCharInputAction { specialChar ->
                when (specialChar) {
                    SpecialChar.CHAR_TOPIC -> {
                        Toast.makeText(this@MainActivity, "输入了 #", Toast.LENGTH_SHORT).show()
                    }

                    SpecialChar.CHAR_AT -> {
                        Toast.makeText(this@MainActivity, "输入了 @", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            tvTopic.setOnClickListener {
                binding.et.insertTextSpan(TopicSpan(Topic(0, "话题${generateNum()}")))
            }

            tvAt.setOnClickListener {
                binding.et.insertTextSpan(AtSpan(User(0, "用户${generateNum()}")))
            }

            tvGetAllTopic.setOnClickListener {
                val allTopics = binding.et.getSpans(TopicSpan::class.java).map { it.topic.name }
                Toast.makeText(this@MainActivity, "$allTopics", Toast.LENGTH_SHORT).show()
            }

            tvGetAllAt.setOnClickListener {
                val allUsers = binding.et.getSpans(AtSpan::class.java).map { it.user.name }
                Toast.makeText(this@MainActivity, "$allUsers", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun generateNum(): Int {
        return Random.nextInt(1..99)
    }
}