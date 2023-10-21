package com.xh.xspan

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xh.xspan.databinding.ActivityMainBinding
import com.xh.xspan.xspan.span.TopicSpan
import com.xh.xspan.xspan.span.ToySpan

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            et.setOnJumpTopicAction {
                jumpToTopicListActivity()
            }

            ivTopic.setOnClickListener {
                binding.et.insertSpannableString(ToySpan().getDisplaySpannableString())
            }
        }
    }

    private fun jumpToTopicListActivity() {
        TopicListActivity.actionStart(this@MainActivity, REQUEST_CODE_TOPIC_LIST)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_OK) return

        when (requestCode) {
            1001 -> {
                val topic = data?.getStringExtra("topic") ?: ""
                binding.et.insertSpannableString(TopicSpan(topic).getDisplaySpannableString())
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_TOPIC_LIST = 1001
    }
}