package com.example.lab16

import android.view.View
import android.widget.Button

class IntroFragment : BaseStoryFragment() {

    override fun getLayoutId(): Int = R.layout.fragment_story_two_buttons

    override fun getImageResource(): Int = R.drawable.elders

    override fun getStoryText(): String =
        "Жили-были старик со старухой. Однажды старик попросил старуху испечь ему колобок. " +
                "Старуха по коробу поскребла, по сусеку помела, замесила тесто, испекла колобок и " +
                "положила она его на окошко остывать."

    override fun setupButtons(view: View) {
        val button1 = view.findViewById<Button>(R.id.button1)
        val button2 = view.findViewById<Button>(R.id.button2)

        button1.text = "Убежать"
        button2.text = "Остаться"

        button1.setOnClickListener {
            navigateToFragment(R.id.action_intro_to_hare)
        }

        button2.setOnClickListener {
            navigateToFragment(R.id.action_intro_to_final_bad)
        }
    }
}