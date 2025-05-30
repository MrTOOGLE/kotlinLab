package com.example.lab16

import android.view.View
import android.widget.Button

class WolfFragment : BaseStoryFragment() {

    override fun getLayoutId(): Int = R.layout.fragment_story_two_buttons

    override fun getImageResource(): Int = R.drawable.wolf

    override fun getStoryText(): String =
        "Катится колобок дальше, а навстречу ему волк. Волк и говорит: «Колобок, колобок, " +
                "я тебя съем!». Но колобок ответил «Не ешь меня, я тебе песенку спою», и спел песенку " +
                "о том, как его испекла старуха и как он убежал от бабушки и дедушки, и от зайца тоже."

    override fun setupButtons(view: View) {
        val button1 = view.findViewById<Button>(R.id.button1)
        val button2 = view.findViewById<Button>(R.id.button2)

        button1.text = "Убежать"
        button2.text = "Остаться"

        button1.setOnClickListener {
            navigateToFragment(R.id.action_wolf_to_bear)
        }

        button2.setOnClickListener {
            navigateToFragment(R.id.action_wolf_to_final_bad)
        }
    }
}