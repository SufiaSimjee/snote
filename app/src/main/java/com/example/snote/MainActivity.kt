package com.example.snote

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.snote.databinding.ActivityMainBinding
import com.example.snote.fragment.NoteDetailFragment
import com.example.snote.fragment.NoteListFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragment = NoteListFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_con , fragment)
            .commit()
    }
}