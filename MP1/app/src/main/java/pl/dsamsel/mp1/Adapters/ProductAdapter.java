package pl.dsamsel.mp1.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pl.dsamsel.mp1.Models.Product;
import pl.dsamsel.mp1.R;

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {

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
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_list_row, parent,false);

        return new ProductViewHolder(view, context, recyclerViewClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, final int position) {
        Product product = productList.get(position);
        productViewHolder.name.setText(product.getName());
        productViewHolder.price.setText(String.valueOf(product.getPrice()));
        productViewHolder.quantity.setText(String.valueOf(product.getQuantity()));
        if (product.isBought()) {
            productViewHolder.isBought.setChecked(true);
        } else {
            productViewHolder.isBought.setChecked(false);
        }

        productViewHolder.modifyProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewClickListener.onClick(view, position);
            }
        });

        productViewHolder.deleteProductButton.setOnClickListener(new View.OnClickListener() {
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
