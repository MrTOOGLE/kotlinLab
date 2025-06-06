package com.example.lab19

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var btnGetContacts: Button
    private lateinit var textViewStatus: TextView
    private lateinit var recyclerViewContacts: RecyclerView
    private lateinit var contactsAdapter: ContactsAdapter

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Пользователь дал разрешение
            loadContacts()
        } else {
            // Пользователь отказал в разрешении
            showPermissionDeniedDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupUI()
        setupRecyclerView()
    }

    private fun initViews() {
        btnGetContacts = findViewById(R.id.btnGetContacts)
        textViewStatus = findViewById(R.id.textViewStatus)
        recyclerViewContacts = findViewById(R.id.recyclerViewContacts)
    }

    private fun setupUI() {
        btnGetContacts.setOnClickListener {
            requestContactsPermission()
        }
    }

    private fun setupRecyclerView() {
        contactsAdapter = ContactsAdapter()
        recyclerViewContacts.apply {
            adapter = contactsAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun requestContactsPermission() {
        when {
            // Проверяем, есть ли уже разрешение
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Разрешение уже есть, загружаем контакты
                loadContacts()
            }

            // Нужно ли показать обоснование?
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_CONTACTS
            ) -> {
                // Показываем обоснование пользователю
                showPermissionRationale()
            }

            else -> {
                // Запрашиваем разрешение напрямую
                requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }
        }
    }

    private fun showPermissionRationale() {
        AlertDialog.Builder(this)
            .setTitle("Разрешение на доступ к контактам")
            .setMessage("Для отображения списка контактов приложению требуется разрешение на доступ к контактам. Без этого разрешения работа, увы, невозможна.")
            .setPositiveButton("Разрешить") { _, _ ->
                requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }
            .setNegativeButton("Не хочу") { dialog, _ ->
                dialog.dismiss()
                showPermissionDeniedDialog()
            }
            .show()
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(this)
            .setTitle("Доступ ограничен")
            .setMessage("Для отображения списка контактов приложению требуется разрешение на доступ к контактам. Без этого разрешения работа, увы, невозможна.\n\nЕсли Вы хотите дать программе ещё один шанс, то откройте системные настройки и выдайте разрешение программе на доступ к контактам.")
            .setPositiveButton("Открыть системные настройки") { _, _ ->
                openAppSettings()
            }
            .setNegativeButton("Нет, спасибо, не нужно") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun openAppSettings() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:$packageName")
        )
        startActivity(intent)
    }

    @SuppressLint("Range")
    private fun getContacts(): List<String> {
        val result = mutableListOf<String>()

        try {
            // Запрос списка контактов
            val cur = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null
            )

            if (cur != null) {
                // Определение номера столбца, содержащего имя контакта
                val colName = cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                while (cur.moveToNext()) {
                    val name = cur.getString(colName)
                    if (!name.isNullOrEmpty()) {
                        result.add(name)
                    }
                }
                cur.close()
            }
        } catch (e: SecurityException) {
            Toast.makeText(this, "Нет разрешения на чтение контактов", Toast.LENGTH_SHORT).show()
        }

        return result
    }

    private fun loadContacts() {
        val contacts = getContacts()

        if (contacts.isNotEmpty()) {
            textViewStatus.text = "Успех! Получены контакты:"
            contactsAdapter.updateContacts(contacts)
            recyclerViewContacts.visibility = android.view.View.VISIBLE
        } else {
            textViewStatus.text = "Контакты не найдены или список пуст"
            recyclerViewContacts.visibility = android.view.View.GONE
        }
    }
}
