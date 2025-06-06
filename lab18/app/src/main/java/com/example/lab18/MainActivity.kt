package com.example.lab18

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader
import java.net.URL
import java.nio.charset.Charset

data class Currency(
    val name: String,
    val value: String,
    val charCode: String
)

class MainActivity : AppCompatActivity() {

    private lateinit var btnGetRates: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val currencies = mutableListOf<Currency>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupListView()
    }

    private fun initViews() {
        btnGetRates = findViewById(R.id.btnGetRates)
        progressBar = findViewById(R.id.progressBar)
        listView = findViewById(R.id.listView)

        btnGetRates.setOnClickListener {
            getCurrencyRates()
        }

        // Изначально прогресс бар скрыт
        progressBar.visibility = View.GONE
    }

    private fun setupListView() {
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf<String>())
        listView.adapter = adapter
    }

    private fun getCurrencyRates() {
        // Создаем корутину для загрузки данных
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            try {
                // Показываем прогресс бар в UI потоке
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.VISIBLE
                    btnGetRates.isEnabled = false
                    currencies.clear()
                    adapter.clear()
                }

                // Загружаем XML данные в фоновом потоке
                val xml = URL("https://www.cbr.ru/scripts/XML_daily.asp")
                    .readText(Charset.forName("Windows-1251"))

                // Парсим XML
                val parsedCurrencies = parseXml(xml)

                // Обновляем UI в главном потоке
                withContext(Dispatchers.Main) {
                    currencies.addAll(parsedCurrencies)
                    updateListView()
                    progressBar.visibility = View.GONE
                    btnGetRates.isEnabled = true

                    Toast.makeText(
                        applicationContext,
                        "Загружено ${currencies.size} валют",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } catch (e: Exception) {
                // Обрабатываем ошибки
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    btnGetRates.isEnabled = true

                    Toast.makeText(
                        applicationContext,
                        "Ошибка загрузки: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun parseXml(xml: String): List<Currency> {
        val currencies = mutableListOf<Currency>()
        val parser = XmlPullParserFactory.newInstance().newPullParser()
        parser.setInput(StringReader(xml))

        var currentName = ""
        var currentValue = ""
        var currentCharCode = ""
        var isInsideValute = false
        var currentTag = ""

        while (parser.eventType != XmlPullParser.END_DOCUMENT) {
            when (parser.eventType) {
                XmlPullParser.START_TAG -> {
                    currentTag = parser.name
                    if (currentTag == "Valute") {
                        isInsideValute = true
                        currentName = ""
                        currentValue = ""
                        currentCharCode = ""
                    }
                }

                XmlPullParser.TEXT -> {
                    if (isInsideValute) {
                        when (currentTag) {
                            "Name" -> currentName = parser.text
                            "Value" -> currentValue = parser.text
                            "CharCode" -> currentCharCode = parser.text
                        }
                    }
                }

                XmlPullParser.END_TAG -> {
                    if (parser.name == "Valute" && isInsideValute) {
                        if (currentName.isNotEmpty() && currentValue.isNotEmpty() && currentCharCode.isNotEmpty()) {
                            currencies.add(Currency(currentName, currentValue, currentCharCode))
                        }
                        isInsideValute = false
                    }
                }
            }
            parser.next()
        }

        return currencies
    }

    private fun updateListView() {
        val displayList = currencies.map { currency ->
            "${currency.name} (${currency.charCode}): ${currency.value} ₽"
        }

        adapter.clear()
        adapter.addAll(displayList)
        adapter.notifyDataSetChanged()
    }
}