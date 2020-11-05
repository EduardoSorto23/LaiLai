package net.dsdev.lailai.clientes.retrofit.menu;

import net.dsdev.lailai.clientes.model.users.ventaSugerida;
import net.dsdev.lailai.clientes.model.users.ventaSugeridaResponse;
import net.dsdev.lailai.clientes.util.Constants;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MenuVentaSugeridaService {

    @POST(Constants.getVentaSugerida)
    Call<ventaSugeridaResponse> ventaSugerida(@Body ventaSugerida venSugerida);
}
