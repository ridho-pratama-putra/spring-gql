//package com.example.springgql.config;
//
//import com.example.springgql.repositories.AlbumRepository;
//import com.example.springgql.repositories.ArtistRepository;
//import graphql.schema.DataFetcher;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.graphql.data.query.QuerydslDataFetcher;
//import org.springframework.graphql.execution.RuntimeWiringConfigurer;
//
//@Configuration
//public class GraphQLConfigRuntimeWiringConfigurer {
//
//    @Bean
//    public RuntimeWiringConfigurer runtimeWiringConfigurer(AlbumRepository albumRepository, ArtistRepository artistRepository) {
//        DataFetcher dataFetcherAlbum = QuerydslDataFetcher.builder(albumRepository).single();
//        DataFetcher dataFetcherAlbums = QuerydslDataFetcher.builder(albumRepository).many();
//        DataFetcher dataFetcherArtist = QuerydslDataFetcher.builder(artistRepository).single();
//        DataFetcher dataFetcherArtists = QuerydslDataFetcher.builder(artistRepository).many();
//        return wiringBuilder -> wiringBuilder
//                .type("Query", builder -> builder.dataFetcher("album", dataFetcherAlbum))
//                .type("Query", builder -> builder.dataFetcher("album", dataFetcherAlbums))
//                .type("Query", builder -> builder.dataFetcher("artists", dataFetcherArtist))
//                .type("Query", builder -> builder.dataFetcher("artist", dataFetcherArtists))
//                ;
//    }
//}
