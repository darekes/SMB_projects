package pl.dsamsel.mp1.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pl.dsamsel.mp1.Models.Shop;
import pl.dsamsel.mp1.R;

public class ShopAdapter extends RecyclerView.Adapter<ShopViewHolder> {

    private List<Shop> shopList;

    public ShopAdapter(List<Shop> shopList) {
        this.shopList = shopList;
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shop_list_row, parent,false);

        return new ShopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder shopViewHolder, final int position) {
        Shop shop = shopList.get(position);
        shopViewHolder.name.setText(shop.getName());
        shopViewHolder.description.setText(shop.getDescription());
        shopViewHolder.range.setText(String.valueOf(shop.getRange()));
        shopViewHolder.latitude.setText(String.valueOf(shop.getLatitude()));
        shopViewHolder.longitude.setText(String.valueOf(shop.getLongitude()));
    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }
}