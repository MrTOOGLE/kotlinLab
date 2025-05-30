package com.example.lab15

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class ShoppingItemDialog : DialogFragment() {

    interface DataListener {
        fun onDialogData(name: String, quantity: String, position: Int?)
    }

    private lateinit var listener: DataListener
    private var editPosition: Int? = null

    companion object {
        private const val ARG_NAME = "name"
        private const val ARG_QUANTITY = "quantity"
        private const val ARG_POSITION = "position"

        fun newInstance(name: String, quantity: String, position: Int): ShoppingItemDialog {
            val dialog = ShoppingItemDialog()
            val bundle = Bundle().apply {
                putString(ARG_NAME, name)
                putString(ARG_QUANTITY, quantity)
                putInt(ARG_POSITION, position)
            }
            dialog.arguments = bundle
            return dialog
        }
    }

    @SuppressLint("UseGetLayoutInflater")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_shopping_item, null)

        val etName = view.findViewById<EditText>(R.id.et_item_name)
        val etQuantity = view.findViewById<EditText>(R.id.et_item_quantity)

        // Если редактируем существующий элемент, заполняем поля
        arguments?.let { args ->
            etName.setText(args.getString(ARG_NAME, ""))
            etQuantity.setText(args.getString(ARG_QUANTITY, ""))
            editPosition = if (args.containsKey(ARG_POSITION)) {
                args.getInt(ARG_POSITION)
            } else null
        }

        val title = if (editPosition != null) "Редактировать товар" else "Добавить товар"

        builder
            .setTitle(title)
            .setView(view)
            .setPositiveButton("OK") { _, _ ->
                val name = etName.text.toString().trim()
                val quantity = etQuantity.text.toString().trim()

                if (name.isNotEmpty() && quantity.isNotEmpty()) {
                    listener.onDialogData(name, quantity, editPosition)
                }
            }
            .setNegativeButton("Отмена") { _, _ ->
                // Ничего не делаем
            }

        return builder.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as DataListener
    }
}