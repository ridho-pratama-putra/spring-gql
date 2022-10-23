package com.example.springgql.controllers;

import com.example.springgql.models.Transaction;
import com.example.springgql.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.graphql.test.tester.GraphQlTester;

@GraphQlTest
class GQLControllerTest {

    @Autowired
    GraphQlTester graphQlTester;

    @Test
    public void users_shouldReturnListOfUser_whenCalled() {
        String request = "query {\n" +
                "    users {\n" +
                "        name\n" +
                "    }\n" +
                "}";

        graphQlTester.document(request)
                .execute()
                .path("users")
                .entityList(User.class);
    }

    @Test
    public void userByid_shouldReturnSingleUser_whenCalled() {
        String request = "query {\n" +
                "    userById(id: 1) {\n" +
                "        name\n" +
                "    }\n" +
                "}";

        graphQlTester.document(request)
                .execute()
                .path("userById")
                .entity(User.class);
    }

    @Test
    public void usersById_shouldReturnSingleUserWithTransaction_whenCalled() {
        String request = "query userById($id: ID){\n" +
                "    userById(id: $id) {\n" +
                "        name\n" +
                "        transactions {\n" +
                "           title\n" +
                "        }\n" +
                "    }\n" +
                "}";

        graphQlTester.document(request)
                .execute()
                .path("userById")
                .entity(User.class)
                .path("userById.transactions")
                .entityList(Transaction.class);
    }

    @Test
    public void users_shouldReturnListOfUserWithTransaction_whenCalled() {
        String request = "query {\n" +
                "    users {\n" +
                "        name\n" +
                "        transactions {\n" +
                "           title\n" +
                "        }\n" +
                "    }\n" +
                "}";

        graphQlTester.document(request)
                .execute()
                .path("users")
                .entityList(User.class)
                .path("users.[0].transactions")
                .entityList(User.class)
        ;
    }
}