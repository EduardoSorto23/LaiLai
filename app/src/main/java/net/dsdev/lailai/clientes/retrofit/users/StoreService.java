package net.dsdev.lailai.clientes.retrofit.users;

import net.dsdev.lailai.clientes.model.ActiveOrders;
import net.dsdev.lailai.clientes.model.Store;
import net.dsdev.lailai.clientes.model.StoreList;
import net.dsdev.lailai.clientes.model.users.Coverage;
import net.dsdev.lailai.clientes.util.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface StoreService {
    @GET(Constants.getStores)
    Call<StoreList> getStores();

    @GET(Constants.getCoverage)
    Call<Coverage> getCoverage(@Path("id") long idTienda, @Query(Constants.futureOrderCoverage) String pedidoFuturo, @Query(Constants.fecHourCoverage) String fecHora);
}
