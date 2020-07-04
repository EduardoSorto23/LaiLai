package net.dsdev.lailai.clientes.retrofit.users;

import net.dsdev.lailai.clientes.model.ActiveOrders;
import net.dsdev.lailai.clientes.model.Request.Order;
import net.dsdev.lailai.clientes.model.users.CancelOrderResponse;
import net.dsdev.lailai.clientes.util.Constants;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OrderService {
    @GET(Constants.getActiveOrders)
    Call<ActiveOrders> getActiveOrders(@Path("idClient") long idClient);

    @GET(Constants.getActiveOrderDetail)
    Call<Order> getActiveOrderDetail(@Path("idOrder") long idOrder);

    @GET(Constants.orderCancel)
    Call<CancelOrderResponse> orderCancel(@Path("idOrder") long idOrder);

}
