package com.example.cinemaup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth

class RegisterScreen : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var mGoogleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_screen)
        val registerButton = findViewById<Button>(R.id.registerButton)
        firebaseAuth = FirebaseAuth.getInstance()

        registerButton.setOnClickListener{
            val email =findViewById<EditText>(R.id.emailEditReg).text.toString()
            val password =findViewById<EditText>(R.id.passEditReg2).text.toString()
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Login status", "signInWithEmail:success")
                        val user = firebaseAuth.currentUser

                            val mainActvityIntent : Intent =  Intent(this,MainActivity::class.java)
                            startActivity(mainActvityIntent)



                    } else {
                        // If sign in fails, display a message to the user.

                        Log.w("Error", "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}