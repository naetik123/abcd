package au.edu.unsw.infs3634.cryptoprofile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
<<<<<<< Updated upstream
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
=======
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
>>>>>>> Stashed changes

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.concurrent.Executors;

import au.edu.unsw.infs3634.cryptoprofile.DB.CoinDatabase;
import au.edu.unsw.infs3634.cryptoprofile.api.Coin;
import au.edu.unsw.infs3634.cryptoprofile.api.CoinService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity {
    public static final String INTENT_MESSAGE = "intent_message";
    private static final String TAG = "DetailActivity";
    private TextView mName;
    private TextView mSymbol;
    private TextView mValue;
    private TextView mChange1h;
    private TextView mChange24h;
    private TextView mChange7d;
    private TextView mMarketcap;
    private TextView mVolume;
<<<<<<< Updated upstream
    private ImageView mSearch, mArt;
    private CoinDatabase mDb;
=======
    private ImageView mSearch;
    private CheckBox cbCoin;

>>>>>>> Stashed changes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Get handle for view elements
        mName = findViewById(R.id.tvName);
        mSymbol = findViewById(R.id.tvSymbol);
        mValue = findViewById(R.id.tvValueField);
        mChange1h = findViewById(R.id.tvChange1hField);
        mChange24h = findViewById(R.id.tvChange24hField);
        mChange7d = findViewById(R.id.tvChange7dField);
        mMarketcap = findViewById(R.id.tvMarketcapField);
        mVolume = findViewById(R.id.tvVolumeField);
        mSearch = findViewById(R.id.ivSearch);
<<<<<<< Updated upstream
        mArt = findViewById(R.id.ivImage);

=======
        cbCoin = findViewById(R.id.cbCoin);
>>>>>>> Stashed changes
        // Get the intent that started this activity and extract the string
        Intent intent = getIntent();
        if (intent.hasExtra(INTENT_MESSAGE)) {
            String coinSymbol = intent.getStringExtra(INTENT_MESSAGE);
            Log.d(TAG, "Intent Message = " + coinSymbol);

            // Instantiate a CountryDatabase object for "country-database"
            mDb = Room.databaseBuilder(getApplicationContext(), CoinDatabase.class, "coin-database").build();

            // Create an asynchronous database call using Java Runnable to:
            // Select the coin from the database by coin symbol received from MainActivity
            // Update activity_detail with the coin details
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    Coin coin = mDb.coinDao().getCoin(coinSymbol);
                    NumberFormat formatter = NumberFormat.getCurrencyInstance();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            NumberFormat formatter = NumberFormat.getCurrencyInstance();
                            setTitle(coin.getName());
                            mName.setText(coin.getName());
                            Glide.with(DetailActivity.this)
                                    .load("https://www.coinlore.com/img/" + coin.getNameid() + ".png")
                                    .fitCenter()
                                    .into(mArt);
                            mSymbol.setText(coin.getSymbol());
                            mValue.setText(formatter.format(Double.valueOf(coin.getPriceUsd())));
                            mChange1h.setText(coin.getPercentChange1h() + " %");
                            mChange24h.setText(coin.getPercentChange24h() + " %");
                            mChange7d.setText(coin.getPercentChange7d() + " %");
                            mMarketcap.setText(formatter.format(Double.valueOf(coin.getMarketCapUsd())));
                            mVolume.setText(formatter.format(coin.getVolume24()));
                            mSearch.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    searchCoin(coin.getName());
                                }
                            });
                        }
                    });
                }
            });
        }
<<<<<<< Updated upstream

=======
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference messageRef =database.getReference(FirebaseAuth.getInstance().getUid());

        messageRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String result = (String) snapshot.getValue();

                if {
                    (result != null && result.equals(Coin.getSymbol()));
                    cbCoin.setChecked(true);
                }
            }
            else {
                cbCoin.setChecked(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            System.out.println(error);
            }
        });
        cbCoin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                DatabaseReference messageRef = database.getReference(FirebaseAuth.getInstance().getUid());
                if (b) {

                    messageRef.setValue((Coin.getSymbol()));
                }
                else{
                    messageRef.setValue("");
                }

            }
        });
>>>>>>> Stashed changes
    }

    // Called when the user taps the search icon
    public void searchCoin(String name) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=" + name));
        startActivity(intent);
    }
}