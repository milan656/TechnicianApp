package com.walkins.aapkedoorstep.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.trading212.diverserecycleradapter.layoutmanager.DiverseLinearLayoutManager
import com.walkins.aapkedoorstep.R

/**
 * Created by petar.marinov on 29.3.2018 г..
 */
abstract class BaseActivity : AppCompatActivity() {

    protected lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = DiverseLinearLayoutManager(this)

        fillRecyclerView()
    }

    abstract fun fillRecyclerView()

    fun generateGamesList() = listOf(
            "Demon Souls", "Bloodborne", "Overwatch", "Monter Hunter World", "God of War", "WoW", "LoL", "OSU!", "Horizon", "Zelda", "CS"
    )

    fun generateProgrammingLanguagesList() = listOf(
            "JavaScript", "Swift", "Python", "Java", "C++", "Ruby", "Rust", "Lisp (EW.)", "Haskell", "F#", "SQL", "C#"
    )

    fun generateSongsList() = listOf(
            "Rainbow Eyes", "Man on the silver mountain", "Blue Morning", "Human", "Try it out", "Sitting on the dock",
            "Alexander Hamilton", "The Trooper", "Nemo", "The Islander", "Jukebox Hero"
    )
}