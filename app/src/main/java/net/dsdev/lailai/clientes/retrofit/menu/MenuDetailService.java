package net.dsdev.lailai.clientes.retrofit.menu;

import net.dsdev.lailai.clientes.model.menuDetail.JsonMenuDetail;
import net.dsdev.lailai.clientes.util.Constants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MenuDetailService {

    @GET(Constants.findById)
    Call<JsonMenuDetail> getMenu(@Path("id") long idMenu);

}
