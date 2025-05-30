package com.example.lab16

import android.view.View
import android.widget.Button

class FinalGoodFragment : BaseStoryFragment() {

    override fun getLayoutId(): Int = R.layout.fragment_story_one_button

    override fun getImageResource(): Int = R.drawable.final_good

    override fun getStoryText(): String =
        "Но раскусил колобок хитрость лисы, и сбежал от неё. Стал путешествовать по свету " +
                "и жить долго и счастливо!"

    override fun setupButtons(view: View) {
        val button = view.findViewById<Button>(R.id.button)

        button.text = "Ещё разок сначала!"

        button.setOnClickListener {
            navigateToFragment(R.id.action_final_good_to_intro)
        }
    }
}