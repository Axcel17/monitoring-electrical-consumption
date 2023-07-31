package com.example.appcontrole

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.appcontrole.databinding.ActivityMainBinding
import com.example.appcontrole.ui.login.FirstViewFragment
import com.example.appcontrole.ui.login.LoginFragment


class MainActivity : AppCompatActivity(), FirstViewFragment.OnLoginButtonClickListener, LoginFragment.OnLoggedButtonClickListener {

        private lateinit var binding: ActivityMainBinding

        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_main);

                if (savedInstanceState == null) {
                        supportFragmentManager.beginTransaction()
                                .replace(R.id.container_fragment, FirstViewFragment())
                                .commit()
                }
        }

        override fun onLoginButtonClicked() {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.container_fragment, LoginFragment())
                        .addToBackStack(null)
                        .commit()
        }

        override fun onLoggedButtonClicked(view: View) {
                val user = view.findViewById<EditText>(R.id.etUsername).text.toString()
                val password = view.findViewById<EditText>(R.id.etPassword).text.toString()

                //Missing validation of existence with database
                if (user == "admin" && password == "admin"){
                        val intent = Intent(this, LoggedActivity::class.java)
                        intent.putExtra("user", user)
                        startActivity(intent)
                } else{
                        Toast.makeText(this, "Usuario o contraseña inválida", Toast.LENGTH_SHORT).show()

                }
        }

}