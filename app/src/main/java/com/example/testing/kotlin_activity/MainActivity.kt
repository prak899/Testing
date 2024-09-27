package com.example.testing.kotlin_activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.view.ActionMode
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testing.BaseActivity
import com.example.testing.R
import com.example.testing.java_activity.MainActivity

class MainActivity : BaseActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ListAdapter
    private var actionMode: ActionMode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recyclerView)
        val dummyData = MutableList(20) { "Item $it" }
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

}