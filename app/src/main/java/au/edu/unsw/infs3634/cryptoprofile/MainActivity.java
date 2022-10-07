package au.edu.unsw.infs3634.cryptoprofile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import au.edu.unsw.infs3634.cryptoprofile.api.Coin;
import au.edu.unsw.infs3634.cryptoprofile.recyclerview_adapter.CoinAdapter;
import au.edu.unsw.infs3634.cryptoprofile.recyclerview_adapter.RecyclerViewInterface;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface {
    private static final String TAG = "MainActivity";
    private Button btnLaunchActivity;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // get the handle to the recycler view
        recyclerView = findViewById(R.id.rvList);

        // instantiate a linear recycler view layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // instantiate the adapter and pass on the list of coins
        adapter = new CoinAdapter(Coin.getCoins(), this);

        // connect the adapter to the recycler view
        recyclerView.setAdapter(adapter);
    }

    // Called when the user taps launch button
    public void launchDetailActivity(String msg) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(DetailActivity.INTENT_MESSAGE ,msg);
        startActivity(intent);
    }

    @Override
    public void onItemClick(String symbol) {
        launchDetailActivity(symbol);
    }
}