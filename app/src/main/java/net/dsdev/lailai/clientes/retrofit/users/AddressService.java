package net.dsdev.lailai.clientes.retrofit.users;
import net.dsdev.lailai.clientes.model.users.Address;
import net.dsdev.lailai.clientes.model.users.AddressResponse;
import net.dsdev.lailai.clientes.model.users.AuthResponse;
import net.dsdev.lailai.clientes.util.Constants;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AddressService {
    @GET(Constants.listDirections)
    Call<AddressResponse> listDirections(@Path("id") long idClient);

    @POST(Constants.saveAddress)
    Call<AuthResponse> saveAddress(@Body Address address);

    @POST(Constants.updateAddress)
    Call<AuthResponse> updateAddress(@Body Address address);

    @GET(Constants.deleteAddress)
    Call<AuthResponse> deleteAddress(@Path("id") long idDirection);

}
