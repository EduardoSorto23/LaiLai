package net.dsdev.lailai.clientes.retrofit.cards;

import net.dsdev.lailai.clientes.model.cards.CardAuth;
import net.dsdev.lailai.clientes.model.cards.CardList;
import net.dsdev.lailai.clientes.model.users.AuthResponse;
import net.dsdev.lailai.clientes.util.Constants;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CardService {

    @POST(Constants.saveCard)
    Call<AuthResponse> saveCard(@Body CardAuth cardAuth);

    @GET(Constants.getCards)
    Call<CardList> getCards(@Path("id") long idCliente);

    @GET(Constants.deleteCard)
    Call<AuthResponse> deleteCards(@Path("id") long idCard);

    @POST(Constants.updateCard)
    Call<AuthResponse> updateCard(@Body CardAuth cardAuth);

}
