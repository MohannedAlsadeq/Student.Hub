package com.studenthub.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import com.studenthub.app.databinding.ActivityWelcomeBinding
import com.studenthub.app.util.Profile

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val b = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(b.root)

        ViewCompat.setOnApplyWindowInsetsListener(b.root) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        b.etName.doAfterTextChanged { b.btnContinue.isEnabled = !it.isNullOrBlank() }
        b.btnContinue.setOnClickListener {
            Profile.setName(this, b.etName.text.toString().trim())
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
