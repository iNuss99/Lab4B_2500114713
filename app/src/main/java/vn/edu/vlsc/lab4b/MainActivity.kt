package vn.edu.vlsc.lab4b

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import vn.edu.vlsc.lab4b.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isCounting = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startCountdownTimer()

        // Button 1: Gây lỗi ANR (Chặn Main Thread)
        binding.btnBlockThread.setOnClickListener {
            binding.tvStatus.text = "Trạng thái: Đang xử lý nặng (Gây đơ UI)..."
            Thread.sleep(5000)
            binding.tvStatus.text = "Trạng thái: Xử lý xong (Main Thread)!"
        }

        // Button 2: Giải pháp với Kotlin Coroutines
        binding.btnRunCoroutine.setOnClickListener {
            binding.tvStatus.text = "Trạng thái: Đang xử lý ngầm bằng Coroutines..."

            lifecycleScope.launch(Dispatchers.IO) {
                val result = doHeavyCalculation()
                withContext(Dispatchers.Main) {
                    binding.tvStatus.text = result
                }
            }
        }
    }

    private suspend fun doHeavyCalculation(): String {
        delay(5000)
        return "Trạng thái: Xử lý xong mượt mà (Coroutines)!"
    }

    private fun startCountdownTimer() {
        if (isCounting) return
        isCounting = true

        lifecycleScope.launch(Dispatchers.Main) {
            for (i in 60 downTo 1) {
                binding.tvCountdown.text = "Thời gian: $i s"
                delay(1000)
            }
            binding.tvCountdown.text = "Thời gian: Hết giờ!"
            isCounting = false
        }
    }
}