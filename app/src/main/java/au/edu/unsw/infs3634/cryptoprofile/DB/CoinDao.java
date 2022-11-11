package au.edu.unsw.infs3634.cryptoprofile.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import au.edu.unsw.infs3634.cryptoprofile.api.Coin;

@Dao
public interface CoinDao {
    @Query("SELECT * FROM coin")
    List<Coin> getCoins();

    @Query("SELECT * FROM coin WHERE symbol == :coinSymbol")
    Coin getCoin(String coinSymbol);

    @Insert
    void insertAll(Coin... coins);

    @Delete
    void deleteAll(Coin... coins);
}
