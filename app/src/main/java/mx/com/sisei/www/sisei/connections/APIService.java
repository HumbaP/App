package mx.com.sisei.www.sisei.connections;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by manuel on 8/05/17.
 */

public interface APIService {
    //Metodo para checar el estado del servidor
    @GET("Admin/sourcingdevices/")
    Call<ResponseBody> getTimeSource();

    //Metodo para checar el usuario
    @GET("Admin/checkuser/{fb_user}/{name}/{first_name}/{fb_link}/Android/{llave1}/{llave2}")
    Call<ResponseBody> doAuth(@Path("fb_user") String fb_user, @Path("name") String name, @Path("first_name") String firstName,@Path("fb_link") String fb_link, @Path("llave1") String key1, @Path("llave2") String key2);
    /*USUARIO*/
    //Metodo para regresar la información del usuario
    @GET("Usuario_controller/me/{fb_id}/Android/{key_one}/{key_two}")
    Call<ResponseBody> me(@Path("fb_id") String fb_id, @Path("key_one") String key_one, @Path("key_two") String key_two);
    //Metodo para resetear al usuario
    @GET("Usuario_controller/me_reset/{fb_id}/Android/{key_one}/{key_two}")
    Call<ResponseBody> me_reset(@Path("fb_id") String fb_id, @Path("key_one") String key_one, @Path("key_two") String key_two);

    //Evento
    //Talleres
    @GET("talleres_controller/lista_talleres")
    Call<ResponseBody> get_workshops();

    @GET("Conferencias_controller/lista_conferencias")
    Call<ResponseBody> getConferences();


    /*Eventos*/
    //Intenta resolver una palabra
    @GET("sources/Palabrasocultas_controller/dar_palabra/{fb_id}/Android/{key_one}/{key_two}/{palabra}")
    Call<ResponseBody> try_word(@Path("fb_id") String fb_id, @Path("key_one") String key_one, @Path("key_two") String key_two,@Path("palabra") String palabra);
    //Regresa el quizz del día
    @GET("sources/Quizz_controller/quizz_del_dia/{fb_id}/Android/{key_one}/{key_two}")
    Call<ResponseBody> my_quizz(@Path("fb_id") String fb_id, @Path("key_one") String key_one, @Path("key_two") String key_two);

    @GET("sources/Quizz_controller/responder_quizz/{fb_id}/Android/{key_one}/{key_two}/{quizz_id}/{respuesta}")
    Call<ResponseBody> answe_quizz(@Path("fb_id") String fb_id, @Path("key_one") String key_one, @Path("key_two") String key_two,@Path("respuesta") String respuesta);

    @GET("Usuario_controller/lista_clasificada")
    Call<ResponseBody> rank_status();



}
