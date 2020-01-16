package pl.dsamsel.mp1.Adapters;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import pl.dsamsel.mp1.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView name;
    TextView price;
    TextView quantity;
    CheckBox isBought;
    Button deleteProductButton;
    Button modifyProductButton;
    private RecyclerViewClickListener recyclerViewClickListener;

    ProductViewHolder(View view, Context context, RecyclerViewClickListener recyclerViewClickListener) {
        super(view);
        this.recyclerViewClickListener = recyclerViewClickListener;
        name = view.findViewById(R.id.product_name);
        price = view.findViewById(R.id.product_price);
        quantity = view.findViewById(R.id.product_quantity);
        isBought = view.findViewById(R.id.is_product_bought);
        deleteProductButton = view.findViewById(R.id.delete_product_button);
        modifyProductButton = view.findViewById(R.id.modify_product_button);
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        recyclerViewClickListener.onClick(view, getAdapterPosition());
    }
}
