package pl.dsamsel.mp2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pl.dsamsel.mp2.Models.Product;
import pl.dsamsel.mp2.R;

public class ProductAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<Product> productList;
    private Context context;
    private RecyclerViewClickListener recyclerViewClickListener;

    public ProductAdapter(List<Product> productList, Context context, RecyclerViewClickListener
            recyclerViewClickListener) {
        this.productList = productList;
        this.context = context;
        this.recyclerViewClickListener = recyclerViewClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_list_row, parent,false);

        return new ViewHolder(view, context, recyclerViewClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        Product product = productList.get(position);
        viewHolder.name.setText(product.getName());
        viewHolder.price.setText(String.valueOf(product.getPrice()));
        viewHolder.quantity.setText(String.valueOf(product.getQuantity()));
        if (product.isBought()) {
            viewHolder.isBought.setChecked(true);
        } else {
            viewHolder.isBought.setChecked(false);
        }

        viewHolder.modifyProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewClickListener.onClick(view, position);
            }
        });

        viewHolder.deleteProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewClickListener.onClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
