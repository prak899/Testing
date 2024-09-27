package com.example.testing.java_activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testing.BaseActivity;
import com.example.testing.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private ActionMode actionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);
        List<String> dummyData = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            dummyData.add("Item " + i);
        }

        adapter = new MyAdapter(dummyData);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnSelectionChangedListener(new MyAdapter.OnSelectionChangedListener() {
            @Override
            public void onSelectionChanged(boolean isSelectionModeActive) {
                if (isSelectionModeActive && actionMode == null) {
                    actionMode = startSupportActionMode(actionModeCallback);
                } else if (!isSelectionModeActive && actionMode != null) {
                    actionMode.finish();
                }
            }
        });
    }

    private final ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.contextual_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.delete) {
                deleteSelectedItems();
                mode.finish();
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            adapter.clearSelection();
        }
    };

    private void deleteSelectedItems() {
        List<Integer> selectedPositions = new ArrayList<>(adapter.getSelectedItems());

        Collections.sort(selectedPositions, Collections.reverseOrder());

        for (int position : selectedPositions) {
            adapter.getItemList().remove(position);
        }

        adapter.clearSelection();
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Items deleted", Toast.LENGTH_SHORT).show();
    }
}