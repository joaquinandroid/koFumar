package joa.edu.kofumar


import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class MainActivity : AppCompatActivity() {

    lateinit var momento: Button
    lateinit var calculo: Button
    lateinit var textViewInicio: TextView
    lateinit var textViewGasto: TextView

    val ahora = Date()

    private val sharedPrefFile = "kotlinsharedpreference"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        setContentView(R.layout.activity_main)


        momento = findViewById(R.id.btnIniciar)
        calculo = findViewById(R.id.btnCalcular)
        textViewInicio = findViewById(R.id.txtvMomentoInicio)
        textViewGasto = findViewById(R.id.txtvGasto)

        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

        val sharedNameValue = sharedPreferences.getString("name_key", "defaultname")
        textViewInicio.text = sharedNameValue

        momento.setOnClickListener{
            val momentoInicio = ahora
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale("es", "ES"))
            sdf.timeZone = TimeZone.getDefault()
            val momentoInicioEsp = sdf.format(momentoInicio)
            val inicioTexto: String = momentoInicioEsp.toString()

            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("name_key", inicioTexto)
            editor.apply()
            editor.commit()

            val sharedNameValue = sharedPreferences.getString("name_key", "defaultname")
            textViewInicio.text = sharedNameValue
        }

        calculo.setOnClickListener {
            val fechaGuardada = sharedPreferences.getString("name_key", "defaultname")

            val textoGasto: String = calcularDiferenciaMinutos(fechaGuardada)
            textViewGasto.text = textoGasto
        }

    }

    fun calcularDiferenciaMinutos(inicio: String?): String{

        val form = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        val dateInicial = form.parse(inicio)
        val dateFinal = Date()

        var diferencia = dateFinal.time - dateInicial.time

        val gastoMs = 4.5/86400000

        val gastoFinal = gastoMs*diferencia



        val df = DecimalFormat("#.00")
        val gastoFinalFormateado: String = df.format(gastoFinal)
        return gastoFinalFormateado
    }
}