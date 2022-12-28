package com.example.springgql.utils;

import graphql.relay.ConnectionCursor;
import graphql.relay.DefaultConnectionCursor;
import graphql.relay.Edge;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

public class CursorUtil {

    public static ConnectionCursor convertCursorFromId(String id) {
        return new DefaultConnectionCursor(Base64.getEncoder().encodeToString(id.getBytes(StandardCharsets.UTF_8)));
    }

    public static String convertIdFromCursor(String id) {
        return new String(Base64.getDecoder().decode(id));
    }

    public static <T> ConnectionCursor getConnectionCursor(List<Edge<T>> defaultEdges, int index) {
        ConnectionCursor cursor = null;
        if (!defaultEdges.isEmpty()) {
            cursor = defaultEdges.get(index).getCursor();
        }
        return cursor;
    }
}
