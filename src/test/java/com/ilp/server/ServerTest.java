package com.ilp.server;


import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ServerTest {

    @Test
    void default_Server() {
        assertDoesNotThrow(() -> Server.main(new String[]{}));
    }


    @ParameterizedTest
    @ValueSource(strings = {"GLPK", "highs", "Debug"})
    void choose_Server(String solver) {
        int port = getRandomPort();
        assertDoesNotThrow(() -> Server.main(new String[]{"-p", String.valueOf(port), "-s", solver}));
    }

    @Test
    void choose_Server_Invalid() {
        int port = getRandomPort();
        assertDoesNotThrow(() -> Server.main(new String[]{"-p", String.valueOf(port), "-s", "Invalid"}));
    }

    @Test
    void server_request() {
        int port = getRandomPort();
        assertDoesNotThrow(() -> Server.main(new String[]{"-p", String.valueOf(port)}));
        try {
            assertEquals(MockClient.makeRequest(port), 200);
        } catch (IOException | InterruptedException e) {
            fail();
        }
    }

    @Test
    void server_get_request() {
        int port = getRandomPort();
        assertDoesNotThrow(() -> Server.main(new String[]{"-p", String.valueOf(port)}));
        try {
            assertEquals(405, MockClient.getRequest(port));
        } catch (IOException | InterruptedException e) {
            fail();
        }
    }

    @Test
    void anySolver() {
        int port = getRandomPort();
        assertDoesNotThrow(() -> Server.main(new String[]{"-p", String.valueOf(port), "-s", "any"}));
        try {
            assertEquals(MockClient.makeRequest(port), 200);
        } catch (IOException | InterruptedException e) {
            fail();
        }
    }

    @Test
    void jsonReadError() throws IOException {
        // Mock HttpExchange
        HttpExchange exchange = mock(HttpExchange.class);
        when(exchange.getRequestMethod()).thenReturn("POST");

        // Mock InputStream to throw IOException
        InputStream inputStream = mock(InputStream.class);
        when(exchange.getRequestBody()).thenReturn(inputStream);
        when(inputStream.read(any(byte[].class), anyInt(), anyInt())).thenThrow(new IOException("Mocked IOException"));

        // Mock OutputStream
        OutputStream outputStream = mock(OutputStream.class);
        when(exchange.getResponseBody()).thenReturn(outputStream);

        // Create instance of WebHandler and call handle method
        Server.WebHandler handler = new Server.WebHandler();
        handler.handle(exchange);

        // Verify that the response code is set to 400
        verify(exchange).sendResponseHeaders(eq(400), anyLong());

        // Verify that the error message is written to the output stream
        verify(outputStream).write(eq("Error reading the request body".getBytes()));
        verify(outputStream).close();
    }



    private int getRandomPort() {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        } catch (IOException e) {
            throw new RuntimeException("Failed to find a free port", e);
        }
    }
}