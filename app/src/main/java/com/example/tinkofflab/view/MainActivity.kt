package com.example.tinkofflab.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tinkofflab.R

class MainActivity:AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.left_to_right_anim, R.anim.right_to_left_anim)
            .add(R.id.fragment_container, MainFragment())
            .commit()
    }
}