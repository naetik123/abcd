package au.edu.unsw.infs3634.cryptoprofile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import au.edu.unsw.infs3634.cryptoprofile.DB.CoinDatabase;
import au.edu.unsw.infs3634.cryptoprofile.api.Coin;
import au.edu.unsw.infs3634.cryptoprofile.api.CoinLoreResponse;
import au.edu.unsw.infs3634.cryptoprofile.api.CoinService;
import au.edu.unsw.infs3634.cryptoprofile.recyclerview_adapter.CoinAdapter;
import au.edu.unsw.infs3634.cryptoprofile.recyclerview_adapter.RecyclerViewInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface {
    private static final String TAG = "MainActivity";
    private Button btnLaunchActivity;
    private RecyclerView recyclerView;
    private CoinAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private CoinDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // get the handle to the recycler view
        recyclerView = findViewById(R.id.rvList);

        // instantiate a linear recycler view layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Create an adapter instance with an empty ArrayList of Coin objects
        adapter = new CoinAdapter(new ArrayList<Coin>(), this);

        // Instantiate a CoinDatabase object
        mDb = Room.databaseBuilder(getApplicationContext(), CoinDatabase.class, "coin-database").build();
        // Create an asynchronous database call using Java Runnable to
        // get the list of coins from the database
        // Set the adapter using the result
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<Coin> coins = (ArrayList<Coin>) mDb.coinDao().getCoins();
                adapter.setData(coins);
                adapter.sort(CoinAdapter.SORT_METHOD_NAME);
            }
        });


        // Implement Retrofit to make API call
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.coinlore.net") // Set the base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create object for the service interface
        CoinService service = retrofit.create(CoinService.class);
        Call<CoinLoreResponse> responseCall = service.getCoins();
        responseCall.enqueue(new Callback<CoinLoreResponse>() {
            @Override
            public void onResponse(Call<CoinLoreResponse> call, Response<CoinLoreResponse> response) {
                Log.d(TAG, "API call successful!");
                List<Coin> coins = response.body().getData();

                // Create an asynchronous database call using Java Runnable to:
                // Delete all rows currently in the database
                // Add all rows from API call result into the database
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.coinDao().deleteAll(mDb.coinDao().getCoins().toArray(new Coin[0]));
                        mDb.coinDao().insertAll(coins.toArray(new Coin[0]));
                    }
                });

                // Supply data to the adapter to be displayed
                adapter.setData((ArrayList)coins);
                adapter.sort(CoinAdapter.SORT_METHOD_VALUE);
            }

            @Override
            public void onFailure(Call<CoinLoreResponse> call, Throwable t) {
                Log.d(TAG, "API call failure.");
            }
        });

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

    @Override
    // Instantiate the menu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    // React to user interaction with the menu
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sortName:
                adapter.sort(CoinAdapter.SORT_METHOD_NAME);
                return true;
            case R.id.sortValue:
                adapter.sort(CoinAdapter.SORT_METHOD_VALUE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}