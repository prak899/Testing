package com.example.testing.java_activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testing.R;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<String> itemList;
    private List<Integer> selectedItems = new ArrayList<>();
    private boolean selectionMode = false;
    private OnSelectionChangedListener onSelectionChangedListener;

    public interface OnSelectionChangedListener {
        void onSelectionChanged(boolean isSelectionModeActive);
    }

    public MyAdapter(List<String> itemList) {
        this.itemList = itemList;
    }

    public List<String> getItemList() {
        return itemList;
    }

    public List<Integer> getSelectedItems() {
        return selectedItems;
    }

    public void setOnSelectionChangedListener(OnSelectionChangedListener listener) {
        this.onSelectionChangedListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.itemName.setText(itemList.get(position));

        // Set checkmark visibility based on selection state
        holder.checkMark.setVisibility(selectedItems.contains(position) ? View.VISIBLE : View.GONE);

        // Handle long press to trigger selection mode
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!selectionMode) {
                    selectionMode = true;
                    toggleSelection(position);
                    if (onSelectionChangedListener != null) {
                        onSelectionChangedListener.onSelectionChanged(true);
                    }
                }
                return true;
            }
        });

        // Handle click to toggle item selection when in selection mode
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectionMode) {
                    toggleSelection(position);
                    if (onSelectionChangedListener != null) {
                        onSelectionChangedListener.onSelectionChanged(!selectedItems.isEmpty());
                    }
                }
            }
        });
    }

    private void toggleSelection(int position) {
        if (selectedItems.contains(position)) {
            selectedItems.remove(Integer.valueOf(position));
        } else {
            selectedItems.add(position);
        }
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void clearSelection() {
        selectionMode = false;
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        ImageView checkMark;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            checkMark = itemView.findViewById(R.id.checkMark);
        }
    }
}

