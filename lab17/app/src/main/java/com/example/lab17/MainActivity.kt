package com.example.lab17

import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    private var newsTimer: CountDownTimer? = null
    private var newsCount = 0
    private var isInNewsSection = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupBottomNavigation()
        startNewsTimer()

        // Показываем музыкальный фрагмент по умолчанию
        if (savedInstanceState == null) {
            showFragment(MusicFragment())
        }
    }

    private fun setupBottomNavigation() {
        bottomNav = findViewById(R.id.bottomNav)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_music -> {
                    isInNewsSection = false
                    showFragment(MusicFragment())
                    true
                }
                R.id.menu_books -> {
                    isInNewsSection = false
                    showFragment(BooksFragment())
                    true
                }
                R.id.menu_news -> {
                    isInNewsSection = true
                    showFragment(NewsFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    private fun startNewsTimer() {
        newsTimer = object : CountDownTimer(Long.MAX_VALUE, 3000) {
            override fun onTick(millisUntilFinished: Long) {
                if (!isInNewsSection) {
                    newsCount++
                    updateNewsBadge()
                }
            }

            override fun onFinish() {
                // Таймер не должен заканчиваться
            }
        }
        newsTimer?.start()
    }

    private fun updateNewsBadge() {
        val badge = bottomNav.getOrCreateBadge(R.id.menu_news)
        if (newsCount > 0) {
            badge.number = newsCount
            badge.isVisible = true
        } else {
            badge.isVisible = false
        }
    }

    fun clearNews() {
        newsCount = 0
        updateNewsBadge()
    }

    override fun onDestroy() {
        super.onDestroy()
        newsTimer?.cancel()
    }
}