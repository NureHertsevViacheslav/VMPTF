package com.example.pr3

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import org.json.JSONArray

object GameSocketManager {
    private lateinit var socket: Socket

    private var onGameMessage: ((String) -> Unit)? = null

    fun connect(playerName: String) {
        try {
            val opts = IO.Options()
            opts.transports = arrayOf("websocket")
            socket = IO.socket("http://10.0.2.2:3001", opts)

            socket.on(Socket.EVENT_CONNECT) {
                Log.d("GameSocket", "Connected to server")
            }.on(Socket.EVENT_CONNECT_ERROR) { args ->
                Log.e("GameSocket", "Connect error: ${args.getOrNull(0)}")
            }.on(Socket.EVENT_DISCONNECT) {
                Log.d("GameSocket", "Disconnected from server")
            }

            socket.on("gameLog") { args ->
                if (args.isNotEmpty()) {
                    val logArray = args[0] as JSONArray
                    val builder = StringBuilder()
                    for (i in 0 until logArray.length()) {
                        builder.append(logArray.getString(i)).append("\n")
                    }
                    onGameMessage?.invoke(builder.toString())
                }
            }
            socket.on("guessResult") { args ->
                if (args.isNotEmpty()) {
                    val message = args[0].toString()
                    Log.d("GameSocket", "Guess result: $message")
                    onGameMessage?.invoke("🎯 $message")
                }
            }

            socket.connect()
        } catch (e: Exception) {
            Log.e("Socket", "Connection error: ${e.localizedMessage}")
        }
    }

    fun setOnLeaderboardUpdate(onUpdate: (JSONArray) -> Unit) {
        socket.off("leaderboard")  // запобігає дублюванню
        socket.on("leaderboard") { args ->
            if (args.isNotEmpty() && args[0] is JSONArray) {
                onUpdate(args[0] as JSONArray)
            }
        }
    }

    fun sendGuess(playerName: String, guess: Int) {
        val data = JSONObject()
        data.put("playerName", playerName)
        data.put("guess", guess)
        socket.emit("guess", data)
    }

    fun setOnGameMessage(callback: (String) -> Unit) {
        onGameMessage = callback
    }

    fun disconnect() {
        socket.disconnect()
    }

    data class PlayerScore(val name: String, val score: Int)
}
