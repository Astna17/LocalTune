package com.app.localtune

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Affiche le splash screen pendant 3 secondes (ajuste le délai si nécessaire)
        Handler().postDelayed({ // Ouvre MainActivity après le délai
            val intent = Intent(
                this@SplashActivity,
                MainActivity::class.java
            )
            startActivity(intent)

            // Ferme SplashActivity pour ne pas revenir dessus
            finish()
        }, 2000) // 3000 millisecondes = 3 secondes
    }
}