package au.edu.unsw.infs3634.cryptoprofile.recyclerview_adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import au.edu.unsw.infs3634.cryptoprofile.R;
import au.edu.unsw.infs3634.cryptoprofile.api.Coin;

public class CoinAdapter extends RecyclerView.Adapter<CoinAdapter.MyViewHolder> implements Filterable {
    private ArrayList<Coin> mCoins, mCoinsFiltered;
    private RecyclerViewInterface recyclerViewInterface;
    public static final int SORT_METHOD_NAME = 1;
    public static final int SORT_METHOD_VALUE = 2;

    // CoinAdapter constructor method
    public CoinAdapter(ArrayList<Coin> coins, RecyclerViewInterface rvInterface) {
        mCoins = coins;
        mCoinsFiltered = coins;
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
        Coin coin = mCoinsFiltered.get(position);
        holder.name.setText(coin.getName());
        holder.change.setText(coin.getPercentChange1h() + " %");
        holder.value.setText(formatter.format(Double.valueOf(coin.getPriceUsd())));
        holder.itemView.setTag(coin.getId());
    }

    @Override
    public int getItemCount() {
        // return the number of items in the recycler view
        return mCoinsFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mCoinsFiltered = mCoins;
                } else {
                    ArrayList<Coin> filteredList = new ArrayList<>();
                    for (Coin coin : mCoins) {
                        if (coin.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(coin);
                        }
                    }
                    mCoinsFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mCoinsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mCoinsFiltered = (ArrayList<Coin>) filterResults.values;
                notifyDataSetChanged();
            }
        };
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

    // Use the Java Collections.sort() and Comparator methods to sort the list
    public void sort(final int sortMethod) {
        if (mCoinsFiltered.size() > 0) {
            Collections.sort(mCoinsFiltered, new Comparator<Coin>() {
                @Override
                public int compare(Coin o1, Coin o2) {
                    if (sortMethod == SORT_METHOD_NAME) {
                        // Make the comparison case insensitive
                        return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
                    } else if (sortMethod == SORT_METHOD_VALUE) {
                        // Sort by value in descending order
                        return Double.valueOf(o2.getPriceUsd()).compareTo(Double.valueOf(o1.getPriceUsd()));
                    }
                    // By default sort the list by coin name
                    return o1.getName().compareTo(o2.getName());
                }
            });
        }
        notifyDataSetChanged();
    }

    // Set the supplied data to the adapter
    public void setData(ArrayList<Coin> data) {
        mCoins.clear();
        mCoins.addAll(data);
        notifyDataSetChanged();
    }
}