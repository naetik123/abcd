package au.edu.unsw.infs3634.cryptoprofile.recyclerview_adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;

import au.edu.unsw.infs3634.cryptoprofile.R;
import au.edu.unsw.infs3634.cryptoprofile.api.Coin;

public class CoinAdapter extends RecyclerView.Adapter<CoinAdapter.MyViewHolder> {
    private ArrayList<Coin> mCoins;
    private RecyclerViewInterface recyclerViewInterface;

    // CoinAdapter constructor method
    public CoinAdapter(ArrayList<Coin> coins, RecyclerViewInterface rvInterface) {
        mCoins = coins;
        recyclerViewInterface = rvInterface;
    }
    @NonNull
    @Override
    public CoinAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate the layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        return new MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull CoinAdapter.MyViewHolder holder, int position) {
        // assign value to each row in the recyclerview
        // based on the position of the recycler view item
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        Coin coin = mCoins.get(position);
        holder.name.setText(coin.getName());
        holder.change.setText(coin.getPercentChange1h() + " %");
        holder.value.setText(formatter.format(Double.valueOf(coin.getPriceUsd())));
        holder.itemView.setTag(coin.getSymbol());
    }

    @Override
    public int getItemCount() {
        // return the number of items in the recycler view
        return mCoins.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // find handle to the views items from list_row.xml layout
        ImageView image;
        TextView name, change, value;
        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            image = itemView.findViewById(R.id.ivImage);
            name = itemView.findViewById(R.id.tvName);
            change = itemView.findViewById(R.id.tvChange);
            value = itemView.findViewById(R.id.tvValue);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null) {
                        recyclerViewInterface.onItemClick((String) itemView.getTag());
                    }
                }
            });
        }
    }
}