package com.example.springgql.services;

import com.example.springgql.repositories.AlbumRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class AlbumServiceTest {

    @MockBean
    AlbumRepository repository;
}