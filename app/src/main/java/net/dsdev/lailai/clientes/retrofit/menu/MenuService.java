package net.dsdev.lailai.clientes.retrofit.menu;

import net.dsdev.lailai.clientes.model.JsonMenus;
import net.dsdev.lailai.clientes.util.Constants;
import retrofit2.Call;
import retrofit2.http.GET;

public interface MenuService {

    @GET(Constants.findAll)
    Call<JsonMenus> getMenus();

}
