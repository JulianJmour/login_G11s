package com.example.loging11s

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvRegistrar = findViewById<TextView>(R.id.tv_registrar)
        tvRegistrar.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
    }
}

class RegistroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val btnRegistro = findViewById<Button>(R.id.Btn_registro)
        btnRegistro.setOnClickListener {
            val name = findViewById<EditText>(R.id.et_nombre).text.toString()
            val username = findViewById<EditText>(R.id.et_usuario).text.toString()
            val password = findViewById<EditText>(R.id.et_contrasena).text.toString()
            val perfil = "usuario" // Puedes cambiar esto según lo necesites

            if (name.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty() && perfil.isNotEmpty()) {
                registrarUsuario(name, username, password, perfil)
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registrarUsuario(name: String, username: String, password: String, perfil: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL("http://10.0.2.2/basededatos/registro.php")
                val postData = "name=$name&username=$username&password=$password&perfil=$perfil"

                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.doOutput = true
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")

                val outputStreamWriter = OutputStreamWriter(conn.outputStream)
                outputStreamWriter.write(postData)
                outputStreamWriter.flush()

                val responseCode = conn.responseCode
                withContext(Dispatchers.Main) {
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        Toast.makeText(this@RegistroActivity, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@RegistroActivity, "Error en el registro", Toast.LENGTH_SHORT).show()
                    }
                }
                outputStreamWriter.close()
                conn.disconnect()
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RegistroActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
