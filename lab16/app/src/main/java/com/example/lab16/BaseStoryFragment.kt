package com.example.lab16

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

abstract class BaseStoryFragment : Fragment() {

    abstract fun getLayoutId(): Int
    abstract fun getImageResource(): Int
    abstract fun getStoryText(): String
    abstract fun setupButtons(view: View)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageView = view.findViewById<ImageView>(R.id.storyImage)
        val textView = view.findViewById<TextView>(R.id.storyText)

        imageView.setImageResource(getImageResource())
        textView.text = getStoryText()

        setupButtons(view)
    }

    protected fun navigateToFragment(actionId: Int) {
        findNavController().navigate(actionId)
    }
}