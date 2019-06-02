package com.ferrup.espresser.ui.settings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.ferrup.espresser.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SectionListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_SECTION = 0;
    private static final int TYPE_ITEM = 1;

    private ArrayList<Object> data;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position, Pair itemData);
    }

    public SectionListAdapter(ArrayList<Object> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_SECTION:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_section_item, parent, false);
                return new SectionViewHolder(view);
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
                return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case TYPE_SECTION:
                ((SectionViewHolder) holder).textView.setText((String) data.get(position));
                break;
            default:
                Pair itemData = (Pair) data.get(position);
                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                if (itemData.second instanceof Integer) {
                    itemViewHolder.checkboxView.setVisibility(View.GONE);
                    itemViewHolder.textView.setVisibility(View.VISIBLE);
                    itemViewHolder.textView.setText(String.valueOf(itemData.second));
                    itemViewHolder.titleView.setText(String.valueOf(itemData.first));
                } else if (itemData.second instanceof Boolean) {
                    //TODO: bind view for checkbox
                }
                if (listener != null)
                    itemViewHolder.itemView.setOnClickListener(v -> listener.onItemClick(position, itemData));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position) instanceof String ? TYPE_SECTION : TYPE_ITEM;
    }

    public void setItemData(int position, Pair itemData) {
        data.set(position, itemData);
        notifyItemChanged(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    static class SectionViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.section_text) TextView textView;

        public SectionViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_title) TextView titleView;
        @BindView(R.id.item_text) TextView textView;
        @BindView(R.id.item_checkbox) CheckBox checkboxView;

        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
