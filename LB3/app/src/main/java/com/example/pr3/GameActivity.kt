package com.example.pr3

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import org.json.JSONObject
import org.json.JSONArray

class GameActivity : AppCompatActivity() {

    private lateinit var guessInput: EditText
    private lateinit var submitButton: Button
    private lateinit var gameLog: TextView
    private lateinit var username: String
    private lateinit var leaderboardView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        guessInput = findViewById(R.id.guessInput)
        submitButton = findViewById(R.id.submitButton)
        gameLog = findViewById(R.id.gameLog)
        leaderboardView = findViewById(R.id.leaderboard)

        username = intent.getStringExtra("username") ?: "Unknown"

        GameSocketManager.connect(username)

        GameSocketManager.setOnGameMessage { message ->
            runOnUiThread {
                gameLog.text = "Game Log:\n$message"
            }
        }

        GameSocketManager.setOnLeaderboardUpdate { jsonArray ->
            Log.d("GameActivity", "Leaderboard update: $jsonArray")

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                Log.d("GameActivity", "Entry $i: $obj")
            }// jsonArray - параметр лямбди
            runOnUiThread {
                leaderboardView.text = ""  // очищуємо перед оновленням
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    val playerName = obj.optString("playerName", "Unknown")
                    val score = obj.optInt("score", 0)
                    leaderboardView.append("$playerName: $score\n")
                }
            }
        }

        submitButton.setOnClickListener {
            val guess = guessInput.text.toString().trim()
            if (guess.isNotEmpty()) {
                GameSocketManager.sendGuess(username, guess.toInt())
                guessInput.text.clear()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        GameSocketManager.disconnect()
    }



}
