package mx.com.sisei.www.sisei.connections;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by manuel on 8/05/17.
 */

public interface APIService {

    @GET("Admin/sourcingdevices/")
    Call<ResponseBody> getTimeSource();

    //Metodo para checar el usuario
    @GET("Admin/checkuser/{fb_user}/{name}/{first_name}/{fb_link}/Android/{llave1}/{llave2}")
    Call<ResponseBody> doAuth(@Path("fb_user") String fb_user, @Path("name") String name, @Path("first_name") String firstName,@Path("fb_link") String fb_link, @Path("llave1") String key1, @Path("llave2") String key2);
}
