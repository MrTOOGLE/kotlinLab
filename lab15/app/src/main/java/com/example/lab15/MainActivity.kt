package com.example.lab15

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), ShoppingItemDialog.DataListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ShoppingListAdapter
    private lateinit var fabAdd: FloatingActionButton
    private val shoppingItems = mutableListOf<ShoppingItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupRecyclerView()
        setupSwipeToDelete()
        setupFab()

        // Добавляем несколько примеров для демонстрации
        addSampleData()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerView)
        fabAdd = findViewById(R.id.fab_add)
    }

    private fun setupRecyclerView() {
        adapter = ShoppingListAdapter(shoppingItems) { position ->
            editItem(position)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupSwipeToDelete() {
        val swipeCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                shoppingItems.removeAt(position)
                adapter.notifyItemRemoved(position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun setupFab() {
        fabAdd.setOnClickListener {
            val dialog = ShoppingItemDialog()
            dialog.show(supportFragmentManager, "add_item_dialog")
        }
    }

    private fun editItem(position: Int) {
        val item = shoppingItems[position]
        val dialog = ShoppingItemDialog.newInstance(item.name, item.quantity, position)
        dialog.show(supportFragmentManager, "edit_item_dialog")
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addSampleData() {
        shoppingItems.apply {
            add(ShoppingItem("Молоко", "1 шт"))
            add(ShoppingItem("Хлеб", "1 шт"))
            add(ShoppingItem("Томаты", "500 гр"))
            add(ShoppingItem("Чипсы", "1 уп"))
        }
        adapter.notifyDataSetChanged()
    }

    override fun onDialogData(name: String, quantity: String, position: Int?) {
        if (position != null) {
            // Редактирование существующего элемента
            shoppingItems[position] = ShoppingItem(name, quantity)
            adapter.notifyItemChanged(position)
        } else {
            // Добавление нового элемента
            shoppingItems.add(ShoppingItem(name, quantity))
            adapter.notifyItemInserted(shoppingItems.size - 1)
        }
    }
}