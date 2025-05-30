package com.example.lab16

import android.view.View
import android.widget.Button

class FinalBadFragment : BaseStoryFragment() {

    override fun getLayoutId(): Int = R.layout.fragment_story_one_button

    override fun getImageResource(): Int = R.drawable.final_bad

    override fun getStoryText(): String =
        "Тут-то глупого колобка и скушали... И это грустный, но полезный урок."

    override fun setupButtons(view: View) {
        val button = view.findViewById<Button>(R.id.button)

        button.text = "Попробовать ещё раз..."

        button.setOnClickListener {
            navigateToFragment(R.id.action_final_bad_to_intro)
        }
    }
}