package net.dsdev.lailai.clientes.retrofit.users;

import net.dsdev.lailai.clientes.model.users.AuthResponse;
import net.dsdev.lailai.clientes.model.users.LoginApiRequest;
import net.dsdev.lailai.clientes.model.users.LoginLocalRequest;
import net.dsdev.lailai.clientes.model.users.NewUser;
import net.dsdev.lailai.clientes.model.users.Accounts;
import net.dsdev.lailai.clientes.util.Constants;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UsersService {

    @POST(Constants.resetPassword)
    Call<Accounts> resetPassword(@Body Accounts accounts);

    @POST(Constants.loginLocalService)
    Call<AuthResponse> sendLoginLocalRequest(@Body LoginLocalRequest loginLocalRequest);

    @POST(Constants.loginApiService)
    Call<AuthResponse> sendLoginApiRequest(@Body LoginApiRequest loginApiRequest);

    @POST(Constants.createNewUser)
    Call<AuthResponse> createNewUser(@Body NewUser newUser);

    @POST(Constants.changePassword)
    Call<Accounts> changePassword(@Body Accounts account);

}
