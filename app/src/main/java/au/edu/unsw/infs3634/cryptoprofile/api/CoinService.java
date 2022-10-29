package au.edu.unsw.infs3634.cryptoprofile.api;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CoinService {
    // First 100 coins
    // Example: https://api.coinlore.net/api/tickers
    @GET("api/tickers")
    Call<CoinLoreResponse> getCoins();

    // Get information for specific coin
    // example: https://api.coinlore.net/api/ticker/?id=90
    @GET("api/ticker/")
    Call<ArrayList<Coin>> getCoin(@Query("id") Integer id);
}
