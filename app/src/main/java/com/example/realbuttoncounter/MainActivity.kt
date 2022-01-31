package com.example.realbuttoncounter

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "MainActivity"
private const val TEXT_VIEW_CONTENT = "savedString"

class MainActivity : AppCompatActivity() {

	private var textData: String? = null
	private var textView: TextView? = null
	private var secondTextView: TextView? = null

	private fun getTime(): String {
		val time = Calendar.getInstance().time
		val sdf = SimpleDateFormat("hh:mm:ss")
		return sdf.format(time)
	}

	private fun displayState(message: String) {
		val formattedTime = getTime()		// Add to textview

		secondTextView?.append("⏳ $formattedTime:		$message\n")		// Show toast
		Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
	}


	// ========== Activity Life Cycle Methods ============== //
	// onCreate method
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		// Find widgets
		val editText = findViewById<EditText>(R.id.editText)
		val button = findViewById<Button>(R.id.button)
		textView = findViewById<EditText>(R.id.textView)
		secondTextView = findViewById<TextView>(R.id.secondTextView)
		textView?.movementMethod = ScrollingMovementMethod()
		secondTextView?.movementMethod = ScrollingMovementMethod()


		// onClickListener for Button
		button?.setOnClickListener(object : View.OnClickListener {
			override fun onClick(v: View?) {
				if (editText.text.isEmpty()) return
				textView?.append("${editText?.text}\n")
				editText?.setText("")
			}
		})

		// display state to textView and toast
		displayState("onCreate: Activity Created 😇")
	}

	override fun onStart() {
		super.onStart()
		displayState("onStart: Activity Started")
	}


	// called by onStart, *only if activity was destroyed*
	override fun onRestoreInstanceState(savedInstanceState: Bundle) {
		super.onRestoreInstanceState(savedInstanceState)		// update textdata
		val message = "\n⬇ onRestoreInstanceState: Instance State Restored\n"
		textData = savedInstanceState.getString(TEXT_VIEW_CONTENT, "")
		textData = "$textData$message\n"
		secondTextView?.append(textData)
	}

	override fun onResume() {
		super.onResume()
		displayState("onResume: Activity Resumed\n")

		// Display Timer
		val timerView = findViewById<TextView>(R.id.time)
		Thread {
			Log.d("JHOO", Thread.currentThread().toString())
			while (true) {
				val time = getTime()
				runOnUiThread { timerView.text = time }    // UI must be changed on UI thread
				Thread.sleep(1000)
			}
		}.start()

	}

	override fun onPause() {
		super.onPause()
		displayState("onPause: Activity Paused")
	}

	// called after onPause .
	override fun onSaveInstanceState(savedInstanceState: Bundle) {
		super.onSaveInstanceState(savedInstanceState)

		// update textData and save
		val msg = "\n💾 onSaveInstanceState: Instance State Saved\n"
		textData = secondTextView?.text.toString()
		textData = "$textData$msg"
		savedInstanceState.putString(TEXT_VIEW_CONTENT, textData)
	}

	override fun onStop() {
		super.onStop()
		displayState("onStop: Activity Stopped\n")
	}

	override fun onDestroy() {
		super.onDestroy()
		Toast.makeText(this@MainActivity, "Activty Destroyed 😈", Toast.LENGTH_SHORT).show()
	}

	override fun onRestart() {
		super.onRestart()
		displayState("onRestart: Activity Restarted")
	}
}