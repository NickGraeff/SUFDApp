package com.nicksnacs.smashultimateframedata.net;

import androidx.annotation.NonNull;

import org.jsoup.nodes.Document;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SUFDApiService {
    @GET(".")
    @NonNull
    Single<Document> getHomePage();

    @GET("{character}")
    @NonNull Single<Document> getCharacterPage(@Path("character") String character);
}
