package com.example.pr3

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject

class LeaderboardActivity : AppCompatActivity() {

    private lateinit var leaderboardTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        leaderboardTextView = findViewById(R.id.leaderboardTextView)

        GameSocketManager.setOnLeaderboardUpdate { data ->
            val builder = StringBuilder()
            for (i in 0 until data.length()) {
                val item = data.getJSONObject(i)
                val name = item.getString("playerName")
                val score = item.getInt("guess")
                builder.append("$name: $score\n")
            }

            runOnUiThread {
                leaderboardTextView.text = builder.toString()
            }
        }
    }
}
