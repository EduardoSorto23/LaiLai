package net.dsdev.lailai.clientes.retrofit.Promo;

import net.dsdev.lailai.clientes.model.Promo.PromoList;
import net.dsdev.lailai.clientes.util.Constants;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PromoService {

    @GET(Constants.getPromos)
    Call<PromoList> getPromos();

}
