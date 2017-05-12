package mx.com.sisei.www.sisei.connections;

import retrofit2.Retrofit;

/**
 * Created by manuel on 8/05/17.
 */

public class APIClient {

    private static Retrofit client=null;

    public static Retrofit getClient(String servidor){
        if(client==null){
            client= new Retrofit.Builder()
                    .baseUrl(servidor)
                    .build();
        }
        return client;
    }
}
