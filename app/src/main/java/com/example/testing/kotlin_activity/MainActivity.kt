package com.example.testing.kotlin_activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ActionMode
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testing.BaseActivity
import com.example.testing.R
import com.example.testing.java_activity.MainActivity
import com.google.android.material.appbar.CollapsingToolbarLayout

class MainActivity : BaseActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ListAdapter
    private var actionMode: ActionMode? = null

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var greetingTextView: TextView
    private var defaultColor = Color.parseColor("#FF5722")

    //    private lateinit var topAppBar : Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val topAppBar: Toolbar = findViewById(R.id.topAppBar)
        topAppBar.title = "Dashboard"
        setSupportActionBar(topAppBar)



        val collapsingToolbarLayout: CollapsingToolbarLayout = findViewById(R.id.collapsingToolbar)
//        collapsingToolbarLayout.title = "Collapsing Toolbar Example"
        // Enable the back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        recyclerView = findViewById(R.id.recyclerView)
        val dummyData = MutableList(20 * 20) { "Item $it" }
        adapter = ListAdapter(dummyData)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        adapter.onSelectionChangedListener = { isSelectionModeActive ->
            if (isSelectionModeActive && actionMode == null) {
                actionMode = startSupportActionMode(actionModeCallback)
            } else if (!isSelectionModeActive && actionMode != null) {
                actionMode?.finish()
            }
        }

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("ThemePrefs", Context.MODE_PRIVATE)

        // Load the saved theme color when the app starts
        val savedColor = sharedPreferences.getInt("theme_color", defaultColor)
        applyThemeColor(savedColor)
    }

    private val actionModeCallback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            mode.menuInflater.inflate(R.menu.contextual_menu, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.delete -> {
                    deleteSelectedItems()
//                    logSelectedItemsForDeletion()
//                    Toast.makeText(this@MainActivity, "Delete action", Toast.LENGTH_SHORT).show()
                    mode.finish()
                    true
                }

                R.id.delete1 -> {
                    startActivity(Intent(this@MainActivity, MainActivity::class.java))
                    mode.finish()
                    true
                }

                R.id.delete2 -> {
                    openColorPickerDialog()
                    true
                }

                else -> false
            }
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            actionMode = null
            adapter.clearSelection()
        }
    }

    private fun logSelectedItemsForDeletion() {
        val selectedPositions = adapter.selectedItems.toList()
        val selectedNames = selectedPositions.map { adapter.itemList[it] }

        Log.d("SelectedForDeletion", "Items selected for deletion: $selectedNames")
        Toast.makeText(this, "Selected items: $selectedNames", Toast.LENGTH_SHORT).show()
    }

    private fun deleteSelectedItems() {
        val selectedPositions = adapter.selectedItems.toList()

        selectedPositions.sortedDescending().forEach { position ->
            adapter.itemList.removeAt(position)
        }

        adapter.clearSelection()
        Toast.makeText(this, "Items deleted", Toast.LENGTH_SHORT).show()
    }

    private fun openColorPickerDialog() {
        // Create a dialog
        val dialogView = layoutInflater.inflate(R.layout.color_picker_dialog, null)
        val radioGroupColors: RadioGroup = dialogView.findViewById(R.id.radioGroupColors)

        // Create the AlertDialog
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Choose a Color")
            .setPositiveButton("OK") { dialog, _ ->
                // Get the selected radio button ID
                val selectedId = radioGroupColors.checkedRadioButtonId

                // Determine the selected color based on the selected RadioButton
                val selectedColor = when (selectedId) {
                    R.id.radioRed -> Color.RED
                    R.id.radioBlue -> Color.BLUE
                    R.id.radioGreen -> Color.GREEN
                    R.id.radioYellow -> Color.YELLOW
                    R.id.radioPurple -> Color.parseColor("#800080") // Purple color code
                    R.id.radioOrange -> Color.parseColor("#FFA500") // Orange color code
                    R.id.radioCyan -> Color.CYAN
                    else -> defaultColor
                }

                // Save and apply the selected color
                saveThemeColor(selectedColor)
                applyThemeColor(selectedColor)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .create()

        dialog.show()
    }

    private fun saveThemeColor(color: Int) {
        // Save the selected color in SharedPreferences
        val editor = sharedPreferences.edit()
        editor.putInt("theme_color", color)
        editor.apply()
    }

    private fun applyThemeColor(color: Int) {
        // Apply the selected color to the TextView or other UI elements
//        greetingTextView.setTextColor(color)
        // Optionally, change other elements dynamically
        supportActionBar?.setBackgroundDrawable(android.graphics.drawable.ColorDrawable(color))
        window.statusBarColor = color // Change status bar color if needed
    }
}