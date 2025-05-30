package com.example.lab17

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class MusicGenreFragment : Fragment() {

    companion object {
        private const val ARG_GENRE = "genre"

        fun newInstance(genre: String): MusicGenreFragment {
            val fragment = MusicGenreFragment()
            val args = Bundle()
            args.putString(ARG_GENRE, genre)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_music_genre, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val genre = arguments?.getString(ARG_GENRE) ?: "Неизвестный жанр"
        val textView = view.findViewById<TextView>(R.id.genreTextView)
        textView.text = "Музыка / $genre"
    }
}