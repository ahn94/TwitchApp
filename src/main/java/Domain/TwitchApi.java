package Domain;

import Objects.StreamList;
import Objects.TopGames;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;


public interface TwitchApi {



    @GET("streams/followed")
    Observable<StreamList> getFollowedStreams(
            @Query("oauth_token") String token,
            @Query("limit") int limit,
            @Query("offset") int offset
    );

    @GET("streams")
    Observable<StreamList> getTopStreamsForGame(
            @Query("game") String game,
            @Query("limit") int limit
    );

    @GET("games/top")
    Observable<TopGames> getGamesList(
            @Query("limit") int limit
    );
}
