package mchehab.com.countdowntimerdemo

import kotlinx.android.synthetic.main.activity_main.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation

class MainActivity : AppCompatActivity() {

    var countDownTimer: CountDownTimer? = null

    var remainingTime: Long = 0
    val INTERVAL: Long = 1

    var didStartCountDown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setButtonCountDownListener()
        setButtonStopCountDownListener()

        if(savedInstanceState != null){
            remainingTime = savedInstanceState.getLong("remainingTime")
            didStartCountDown = savedInstanceState.getBoolean("didStartCountDown")
            buttonStopCountDown.setText(savedInstanceState.getString("buttonStopCountDownText"))
            textViewCountDown.setText(savedInstanceState.getString("textViewCountDown"))
            if(didStartCountDown){
                startCountDownTimer(remainingTime, INTERVAL)
                buttonStopCountDown.isEnabled = true
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putLong("remainingTime", remainingTime)
        outState?.putBoolean("didStartCountDown", didStartCountDown)
        outState?.putString("buttonStopCountDownText", buttonStopCountDown.text.toString())
        outState?.putString("textViewCountDown", textViewCountDown.text.toString())
    }

    private fun setButtonCountDownListener(){
        buttonCountDown.setOnClickListener {
            if(editText.text.toString().isEmpty()){
                return@setOnClickListener
            }
            buttonStopCountDown.isEnabled = true
            remainingTime = Integer.parseInt(editText.text.toString()).toLong()
            remainingTime *= 1000
            stopCountDownTimer()
            startCountDownTimer(remainingTime, INTERVAL)
        }
    }

    private fun setButtonStopCountDownListener(){
        buttonStopCountDown.setOnClickListener{
            if (buttonStopCountDown.text.toString().equals(getString(R.string.stopCountDown), ignoreCase = true)) {
                stopCountDownTimer()
                buttonStopCountDown.text = getString(R.string.resumeCountDown)
            } else {
                startCountDownTimer(remainingTime, INTERVAL)
                buttonStopCountDown.text = getString(R.string.stopCountDown)
            }
        }
    }

    private fun startCountDownTimer(duration: Long, interval: Long){
        countDownTimer = object : CountDownTimer(duration, interval){
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished
                var seconds = millisUntilFinished / 1000
                var minutes = seconds / 60
                val hours = minutes / 60

                if (minutes > 0)
                    seconds = seconds % 60
                if (hours > 0)
                    minutes = minutes % 60
                val time = formatNumber(hours) + ":" + formatNumber(minutes) + ":" +
                        formatNumber(seconds)
                textViewCountDown.setText(time)
            }

            override fun onFinish() {
                textViewCountDown.setText("00:00:00")
                flashAnimate(textViewCountDown, 500, 0, Animation.REVERSE, Animation.INFINITE)
            }
        }
        buttonStopCountDown.text = getString(R.string.stopCountDown)
        countDownTimer!!.start()
        didStartCountDown = true
    }

    private fun formatNumber(value: Long): String{
        if(value < 10)
            return "0$value"
        return "$value"
    }

    private fun stopCountDownTimer(){
        countDownTimer?.cancel()
        didStartCountDown = false
    }

    private fun flashAnimate(view: View, duration: Long, startOffset: Int,repeatMode: Int,
                             repeatCount: Int){
        val flashAnimation = AlphaAnimation(0.0f, 1.0f)
        flashAnimation.duration = duration
        flashAnimation.startOffset = startOffset.toLong()
        flashAnimation.repeatMode = repeatMode
        flashAnimation.repeatCount = repeatCount
        view.startAnimation(flashAnimation)
    }
}