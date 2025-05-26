package com.example.pr3

import android.os.Bundle
import androidx.activity.ComponentActivity
import android.content.Intent
import android.widget.Button
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startGameButton = findViewById<Button>(R.id.startGameButton)
        val leaderboardButton = findViewById<Button>(R.id.leaderboardButton)
        username = "User" + (1000..9999).random()
        GameSocketManager.connect(username)
        startGameButton.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }

        leaderboardButton.setOnClickListener {
            val intent = Intent(this, LeaderboardActivity::class.java)
            startActivity(intent)
        }
    }


}
