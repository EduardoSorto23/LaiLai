package net.dsdev.lailai.clientes.retrofit.users;

import net.dsdev.lailai.clientes.model.ActiveOrders;
import net.dsdev.lailai.clientes.model.Store;
import net.dsdev.lailai.clientes.model.StoreList;
import net.dsdev.lailai.clientes.util.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface StoreService {
    @GET(Constants.getStores)
    Call<StoreList> getStores();
}
