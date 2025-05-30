package com.example.lab16

import android.view.View
import android.widget.Button

class FoxFragment : BaseStoryFragment() {

    override fun getLayoutId(): Int = R.layout.fragment_story_two_buttons

    override fun getImageResource(): Int = R.drawable.fox

    override fun getStoryText(): String =
        "Катится колобок дальше, а навстречу ему лиса. Лиса и говорит: «Колобок, колобок, " +
                "я тебя съем!». Но колобок ответил «Не ешь меня, я тебе песенку спою», и спел песенку " +
                "о том, как его испекла старуха и как он убежал от бабушки и дедушки, и от зайца, " +
                "волка и медведя тоже. А лиса говорит: «Хороша песенка, но глуховата я, сядь мне на " +
                "язычок, и спой ещё разок!»"

    override fun setupButtons(view: View) {
        val button1 = view.findViewById<Button>(R.id.button1)
        val button2 = view.findViewById<Button>(R.id.button2)

        button1.text = "Послушаться"
        button2.text = "Убежать"

        button1.setOnClickListener {
            navigateToFragment(R.id.action_fox_to_final_bad)
        }

        button2.setOnClickListener {
            navigateToFragment(R.id.action_fox_to_final_good)
        }
    }
}