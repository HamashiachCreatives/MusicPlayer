package com.example.musicplayer

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    //Declaring variables
    var startTime = 0.00
    var finalTime = 0.00
    var jumpTime = 10000
    var oneTimeOnly = 0

    //Initializing the handler
    var handler: Handler = Handler()

    //Media Player initialization
    var mediaPlayer: MediaPlayer = MediaPlayer()
    lateinit var time_txt: TextView
    lateinit var seekBar: SeekBar

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Declaration and Initialization of components
        var txtLabel: TextView = findViewById(R.id.txtLabel)
        var imageView: ImageView = findViewById(R.id.imageView)
        var song_title: TextView = findViewById(R.id.song_title)
        var back_btn: Button = findViewById(R.id.back_btn)
        var pause_btn: Button = findViewById(R.id.pause_btn)
        var play_btn: Button = findViewById(R.id.play_btn)
        var forward_btn: Button = findViewById(R.id.forward_btn)
        seekBar = findViewById(R.id.seek_bar)
        time_txt = findViewById(R.id.time_left_text)

        //Setting Media Player Library
        mediaPlayer = MediaPlayer.create(this, R.raw.our_father)

        //Setting the song title
        song_title.text = "Title: ${resources.getResourceEntryName(R.raw.our_father)}."

        //Setting play button behaviour
        play_btn.setOnClickListener(){

            mediaPlayer.start()
            startTime = mediaPlayer.currentPosition.toDouble()
            finalTime = mediaPlayer.duration.toDouble()

            if (oneTimeOnly == 0){
                seekBar.max = finalTime.toInt()
                oneTimeOnly = 1
            }

            time_txt.text = startTime.toString()
            seekBar.setProgress(startTime.toInt())
            handler.postDelayed(UpdateSongTime, 100)

        }

        //Setting the pause button behavior
        pause_btn.setOnClickListener(){
            mediaPlayer.pause()
        }

        //Setting back button behavior
        back_btn.setOnClickListener(){

            var temp = startTime
            if((temp - jumpTime) > 0){

                startTime -= jumpTime
                mediaPlayer.seekTo(startTime.toInt())

            }else{

                Toast.makeText(this, "Can't jump backward.", Toast.LENGTH_LONG).show()

            }

        }

        //Setting behavior of forward button
        forward_btn.setOnClickListener(){

            var temp = startTime
            if((temp + jumpTime) <= finalTime){

                startTime += jumpTime
                mediaPlayer.seekTo(startTime.toInt())

            }else{

                Toast.makeText(this, "Can't jump forward.", Toast.LENGTH_LONG).show()

            }

        }

    }

    // Creating the Runnable
    val UpdateSongTime: Runnable = object : Runnable {
        override fun run() {
            startTime = mediaPlayer.currentPosition.toDouble()
            time_txt.text = "" +
                    String.format(
                        "%d min , %d sec",
                        TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()),
                        TimeUnit.MILLISECONDS.toSeconds(
                            startTime.toLong()
                                    - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(
                                    startTime.toLong()
                                )
                            ))
                    )


            seekBar.progress = startTime.toInt()
            handler.postDelayed(this, 100)

        }
    }

}