package com.example.springgql.controllers;

import com.example.springgql.models.Release;
import com.example.springgql.services.ReleaseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RestControllerEndpoint {

    public final ReleaseService releaseService;


    public RestControllerEndpoint(ReleaseService releaseService) {
        this.releaseService = releaseService;
    }

    @GetMapping("/allRelease")
    List<Release> allRelease () {
        System.out.println("lklkkhuihug");
        return releaseService.getAllReleasesRest();
    }
}
