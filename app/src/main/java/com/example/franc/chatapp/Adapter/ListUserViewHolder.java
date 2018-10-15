package com.example.franc.chatapp.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.franc.chatapp.Common.ItemOnClickListener;
import com.example.franc.chatapp.R;

public class ListUserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtName, txtEmail;

    private ItemOnClickListener itemOnClickListener;

    public void setItemOnClickListener(ItemOnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }

    public ListUserViewHolder(View itemView) {
        super(itemView);
        txtEmail = itemView.findViewById(R.id.txtEmail);
        txtName = itemView.findViewById(R.id.txtName);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemOnClickListener.OnClick(view, getAdapterPosition(), false);
    }
}
