package com.itechpro.ezmaxvietchuan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itechpro.ezmaxvietchuan.R;
import com.itechpro.ezmaxvietchuan.mode.Popup;

import java.util.ArrayList;


public class ChonKhachhangAdapter extends RecyclerView.Adapter<ChonKhachhangAdapter.MultiViewHolder> {

    private Context context;
    private ArrayList<Popup> employees;
    private static OnItemClickListener listener;

    public ChonKhachhangAdapter(ArrayList<Popup> employees, Context context, OnItemClickListener listener) {
        this.context = context;
        this.employees = employees;
        this.listener = listener;
    }

    public interface OnItemClickListener {

        void onItemClick(View itemView, int position);

    }


    @NonNull
    @Override
    public MultiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_select, viewGroup, false);
        return new MultiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MultiViewHolder multiViewHolder, int position) {
        multiViewHolder.bind(employees.get(position));

    }

    @Override
    public int getItemCount() {
        return employees.size();
    }

    class MultiViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private ImageView imageView;

        MultiViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            imageView = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemClick(itemView, getLayoutPosition());
                }
            });
        }

        void bind(final Popup employee) {
            imageView.setVisibility(employee.isCheck() ? View.VISIBLE : View.GONE);
            textView.setText(employee.getTenpopup());
        }
    }


}