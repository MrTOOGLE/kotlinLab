package com.example.mokrovlevlab9

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private lateinit var name: EditText
    private lateinit var password: EditText
    private lateinit var country: Spinner
    private lateinit var firstNumOfPhoneNum: TextView
    private lateinit var phoneNum: EditText
    private lateinit var showNum: RadioGroup
    private lateinit var agreement: CheckBox
    private lateinit var reg: Button
    private lateinit var close: Button

    private lateinit var userName: String
    private lateinit var userPassword: String
    private lateinit var userCountry: String
    private lateinit var userPhoneCode: String
    private lateinit var userPhone: String

    private val countryCodes = mapOf(
        "Россия" to "+7",
        "США" to "+1",
        "Великобритания" to "+44",
        "Китай" to "+86",
        "Канада" to "+1"
    )

    @SuppressLint("SetTextI18n", "InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        name = findViewById(R.id.editTextName)
        password = findViewById(R.id.editTextPassword)
        country = findViewById(R.id.spinnerCountry)
        firstNumOfPhoneNum = findViewById(R.id.textViewNumber)
        phoneNum = findViewById(R.id.editTextPhone)
        showNum = findViewById(R.id.radioGroup)
        agreement = findViewById(R.id.checkBoxAgree)
        reg = findViewById(R.id.buttonReg)
        close = findViewById(R.id.buttonClose)

        /**
         * Обработка регистрации
         */

        name.doAfterTextChanged {
            userName = name.text.toString()
        }
        password.doAfterTextChanged {
            userPassword = password.text.toString()
        }

        // В зависимости от выбора страны - пишем её код для номера телефона
        country.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                userCountry = p0?.getItemAtPosition(p2).toString()
                userPhoneCode = countryCodes[userCountry] ?: ""
                firstNumOfPhoneNum.text = userPhoneCode
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                //
            }
        }

        phoneNum.doAfterTextChanged {
            userPhone = userPhoneCode + phoneNum.text.toString()
        }

        // Проверяем RadioGroup
        showNum.setOnCheckedChangeListener { _, i ->
            val userChoose = when (i) {
                R.id.radioButtonAll -> R.string.all
                R.id.radioButtonFriends -> R.string.friends_only
                R.id.radioButtonNobody -> R.string.nobody
                else -> "Не выбрано"
            }
        }

        // Если условия пользования не приняты - кнопка регистрации не действительна
        agreement.setOnClickListener {
            reg.isEnabled = agreement.isChecked
        }

        reg.setOnClickListener {
            // Корректное отображение всего сообщения
            val inflater = layoutInflater
            val layout = inflater.inflate(R.layout.custom_toast, null)
            val text = layout.findViewById<TextView>(R.id.toastText)
            text.text = """
                Имя: $userName
                Пароль: $userPassword
                Страна: $userCountry
                Телефон: $userPhone
            """.trimIndent()

            text.maxLines = 10 // Максимальное количество строк
            text.ellipsize = TextUtils.TruncateAt.END // Обрезка в конце, если не помещается

            val toast = Toast(applicationContext)
            toast.view = layout
            toast.duration = Toast.LENGTH_LONG
            toast.show()
        }

        close.setOnClickListener {
            finish()
            exitProcess(0)
        }
    }
}