package com.example.homesecure

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import kotlinx.android.synthetic.main.activity_login.*
import kotlin.math.log

class LoginActivity : AppCompatActivity() {

    private lateinit var dialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        buttonLogin.setOnClickListener {
            POST()
        }
    }

    fun showLoading() {

        dialog = ProgressDialog.show(this, "", "Loading", true)
    }

    fun closeLoading() {
        dialog.dismiss()
    }


    fun POST() {
        showLoading()
        val payload = """
            {"username": "${editLoginEmail.text}", "password": "${editLoginPassword.text}"}
        """.trimIndent()

        Fuel.post("${Api.host}/login")
           .jsonBody(payload)
            .header("Content-type" to "application/json")
            .responseString { request, response, result ->
                Log.d("POST REQUEST: " , "$request \n $response \n $result")

                val mapper = ObjectMapper().registerModule(KotlinModule())
                val state: PostResult = mapper.readValue(result.get())

                val message = checkIfOk(state)
                closeLoading()
                Looper.prepare()
                Toast.makeText(this, message, Toast.LENGTH_LONG ).show()
                Looper.loop()

            }

    }

    private fun checkIfOk(state: PostResult): String {
        return when (state.code){
            "200"-> {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("user", state.user.id)
                intent.putExtra("email", state.user.email)
                intent.putExtra("username", state.user.username)
                startActivity(intent)
                "Welcome!"
            }
            "404"-> "Username or password error!"
            else->"Something went wrong"
        }
    }


    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    class PostResult(
        var code: String,
        var description: String
    ) {
        @JsonProperty("user")
        public lateinit var user: User
    }
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    class User
        (var id: Int,
         @JsonProperty("username")
         var username: String,
         @JsonProperty("lastname")
         var lastname: String,
         @JsonProperty("firstname")
         var firstname: String,
         @JsonProperty("email")
         var email: String,
         @JsonProperty("createdAt")
         var createdAt: String,
         @JsonProperty("updatedAt")
         var updatedAt: String,
         @JsonProperty("password")
         var password: String
         )
    {

    }
}
