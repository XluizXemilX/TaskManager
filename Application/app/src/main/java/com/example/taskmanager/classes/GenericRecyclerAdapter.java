package com.example.taskmanager.classes;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class GenericRecyclerAdapter<T> extends RecyclerView.Adapter<GenericRecyclerAdapter.MyViewHolder> {

    private List<T> data;
    private final int layoutResourceId;

    private GenericRecyclerListener<T> listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final ViewDataBinding binding;

        public MyViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(T model) {
            binding.setVariable(com.example.taskmanager.BR.model, model);
            binding.executePendingBindings();
        }
    }

    public GenericRecyclerAdapter(List<T> data, int layoutResourceId) {

        if(data == null) {
            this.data = Collections.emptyList();
        } else {
            this.data = data;
        }
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, layoutResourceId, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GenericRecyclerAdapter.MyViewHolder holder, int position) {

        final T model = data.get(holder.getAdapterPosition());
        holder.bind(model);
        holder.itemView.setOnClickListener( v -> {
            if (listener != null) {
                listener.onClick(model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public GenericRecyclerListener<T> getListener() {
        return listener;
    }
    public void setListener(GenericRecyclerListener<T> listener) {
        this.listener = listener;
    }

    public interface GenericRecyclerListener<T> {
        void onClick(T d );
    }
}