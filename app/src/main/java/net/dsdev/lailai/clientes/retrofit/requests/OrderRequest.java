package net.dsdev.lailai.clientes.retrofit.requests;

import net.dsdev.lailai.clientes.model.Request.Order;
import net.dsdev.lailai.clientes.model.users.AuthResponse;
import net.dsdev.lailai.clientes.util.Constants;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OrderRequest {

    @POST(Constants.sendOrder)
    Call<AuthResponse> sendFinalOrder(@Body Order order);

}
