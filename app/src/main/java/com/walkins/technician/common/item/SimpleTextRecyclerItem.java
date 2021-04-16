package com.walkins.technician.common.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trading212.demo.item.ItemType;
import com.trading212.diverserecycleradapter.DiverseRecyclerAdapter;
import com.walkins.technician.R;
import com.walkins.technician.common.onClickAdapter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleTextRecyclerItem extends DiverseRecyclerAdapter.RecyclerItem<String, SimpleTextRecyclerItem.ViewHolder> {

    // Using Enum ordinal positions to guarantee uniqueness of item type
    public static final int TYPE = ItemType.SIMPLE_TEXT.ordinal();

    private String text;
    private onClickAdapter onClickAd;

    public SimpleTextRecyclerItem(String text, onClickAdapter onClickAdapter) {

        this.onClickAd = onClickAdapter;
        this.text = text;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Nullable
    @Override
    public String getData() {
        return text;
    }

    @NotNull
    @Override
    protected ViewHolder createViewHolder(@NotNull ViewGroup parent, @NotNull LayoutInflater inflater) {
        View view = (inflater.inflate(R.layout.item_simple_text, parent, false));
        return new ViewHolder(view);
    }

    @NotNull
    protected void onBindViewHolder(ViewHolder holder, int position) {

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (onClickAd != null) {
                    onClickAd.onPositionClick(position, 0);
                }

            }
        });
    }


    public static class ViewHolder extends DiverseRecyclerAdapter.ViewHolder<String> {

        private TextView textView;

        public ViewHolder(@NotNull View itemView) {
            super(itemView);

            textView = findViewById(R.id.tvaddress);
        }

        @Override
        protected void bindTo(@Nullable String data) {
            textView.setText(data);

        }
    }
}
