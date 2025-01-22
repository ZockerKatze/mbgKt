package com.example.rndclick2

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import kotlin.random.Random

class MainActivity : Activity() {
    private var score = 0
    private val boxSize = 200
    private lateinit var scoreLabel: TextView
    private lateinit var canvas: FrameLayout
    private lateinit var pauseButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        canvas = FrameLayout(this)
        setContentView(canvas)
        canvas.setBackgroundColor(Color.BLACK);

        // Initialize the score label
        scoreLabel = TextView(this).apply {
            text = "Score: $score"
            setTextColor(Color.WHITE)
            textSize = 24f;
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
        }
        canvas.addView(scoreLabel)

        // Add the Pause button
        pauseButton = Button(this).apply {
            text = "Pause"
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                marginStart = 40
                topMargin = 100
            }
            setOnClickListener { callPause() }
        }
        canvas.addView(pauseButton)

        // Ensure layout is ready before calculating dimensions
        canvas.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                canvas.viewTreeObserver.removeOnGlobalLayoutListener(this)
                createRandomBox()
            }
        })
    }

    private fun createRandomBox() {
        canvas.removeAllViews()
        canvas.addView(scoreLabel)
        canvas.addView(pauseButton)

        // Generate random coordinates within bounds
        val x1 = Random.nextInt(50, canvas.width - 50 - boxSize)
        val y1 = Random.nextInt(150, canvas.height - 50 - boxSize) // Avoid overlapping with Pause button

        val box = View(this).apply {
            setBackgroundColor(Color.WHITE) // Set box color to white
            layoutParams = FrameLayout.LayoutParams(boxSize, boxSize).apply {
                setMargins(x1, y1, 0, 0)
            }
        }
        canvas.addView(box)

        box.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                onBoxClick()
            }
            true
        }
    }

    private fun onBoxClick() {
        score++
        updateScore()
        createRandomBox()
    }

    private fun updateScore() {
        scoreLabel.text = "Score: $score"
    }

    private fun callPause() {
        val pauseOverlay = FrameLayout(this).apply {
            setBackgroundColor(Color.argb(128, 0, 0, 0))
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        }

        val pauseLabel = TextView(this).apply {
            text = "PAUSED"
            setTextColor(Color.WHITE)
            textSize = 40f
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = canvas.height / 2 - 100
                marginStart = canvas.width / 2 - 100
            }
        }
        pauseOverlay.addView(pauseLabel)

        val continueButton = Button(this).apply {
            text = "Continue"
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = canvas.height / 2
                marginStart = canvas.width / 2 - 100
            }
            setOnClickListener {
                canvas.removeView(pauseOverlay)
            }
        }
        pauseOverlay.addView(continueButton)

        val exitButton = Button(this).apply {
            text = "Exit"
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = canvas.height / 2 + 100
                marginStart = canvas.width / 2 - 100
            }
            setOnClickListener {
                finish()
            }
        }
        pauseOverlay.addView(exitButton)

        canvas.addView(pauseOverlay)
    }
}
