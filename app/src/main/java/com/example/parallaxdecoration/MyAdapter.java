package com.example.parallaxdecoration;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

    private Context context;

    public MyAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new MyAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyHolder holder, final int position) {
        if (position % 3 == 0) {
            holder.bind("苹果", R.drawable.apple);
        } else if (position % 3 == 1) {
            holder.bind("香蕉", R.drawable.banana);
        } else if (position % 3 == 2) {
            holder.bind("橘子", R.drawable.oragle);
        }

        if (position % 2 == 0) {
            holder.linearLayout.setPadding(0, 150, 0, 0);
        }else {
            holder.linearLayout.setPadding(0, 0, 0, 150);
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position % 3 == 0) {
                    Toast.makeText(context, "苹果", Toast.LENGTH_SHORT).show();
                } else if (position % 3 == 1) {
                    Toast.makeText(context, "香蕉", Toast.LENGTH_SHORT).show();
                } else if (position % 3 == 2) {
                    Toast.makeText(context, "橘子", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 100;
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private LinearLayout linearLayout;

        public final void bind(String data, Integer integer) {
            View view = this.itemView;
            TextView textView = (TextView) view.findViewById(R.id.item_text);
            ImageView imageView = view.findViewById(R.id.iv);
            linearLayout = view.findViewById(R.id.ll);
            textView.setText((CharSequence) data);
            imageView.setImageResource(integer);
        }

        public MyHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
