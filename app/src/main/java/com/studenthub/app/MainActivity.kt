package com.studenthub.app

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.EditText
import android.widget.FrameLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.studenthub.app.databinding.ActivityMainBinding
import com.studenthub.app.ui.*
import com.studenthub.app.util.Profile

class MainActivity : AppCompatActivity() {
    private lateinit var b: ActivityMainBinding
    private val notificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // First launch: no name saved yet -> show the welcome screen
        if (Profile.getName(this).isBlank()) {
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
            return
        }

        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        ViewCompat.setOnApplyWindowInsetsListener(b.content) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        showName()
        b.topBar.setOnClickListener { b.drawer.openDrawer(GravityCompat.START) }
        b.btnEditName.setOnClickListener { editName() }

        b.bottomNav.setOnItemSelectedListener { item ->
            show(
                when (item.itemId) {
                    R.id.nav_exams -> ExamsFragment()
                    R.id.nav_assignments -> AssignmentsFragment()
                    R.id.nav_tasks -> TasksFragment()
                    R.id.nav_ideas -> IdeasFragment()
                    else -> HomeFragment()
                }
            )
            true
        }

        if (savedInstanceState == null) {
            b.bottomNav.selectedItemId = R.id.nav_home
            if (Build.VERSION.SDK_INT >= 33) notificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun show(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

    /** Puts the name and its first letter in the top corner and in the profile panel. */
    private fun showName() {
        val name = Profile.getName(this)
        val initial = name.take(1).uppercase()
        b.avatarSmall.text = initial
        b.nameSmall.text = name
        b.avatarBig.text = initial
        b.nameBig.text = name
    }

    private fun editName() {
        val input = EditText(this).apply {
            setText(Profile.getName(this@MainActivity))
            setSingleLine()
        }
        val holder = FrameLayout(this).apply {
            setPadding(64, 16, 64, 0)
            addView(input)
        }
        MaterialAlertDialogBuilder(this)
            .setTitle("Edit name")
            .setView(holder)
            .setPositiveButton("Save") { _, _ ->
                val newName = input.text.toString().trim()
                if (newName.isNotBlank()) {
                    Profile.setName(this, newName)
                    showName()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
