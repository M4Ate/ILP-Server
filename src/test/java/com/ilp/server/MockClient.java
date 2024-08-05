package com.ilp.server;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MockClient {

    private static final String jsonString = "{\"variables\":[\"x_v0_c0\",\"x_v0_c1\",\"x_v0_c2\",\"x_v0_c3\",\"x_v0_c4\",\"x_v1_c0\",\"x_v1_c1\",\"x_v1_c2\",\"x_v1_c3\",\"x_v1_c4\",\"x_v2_c0\",\"x_v2_c1\",\"x_v2_c2\",\"x_v2_c3\",\"x_v2_c4\",\"x_v3_c0\",\"x_v3_c1\",\"x_v3_c2\",\"x_v3_c3\",\"x_v3_c4\",\"x_v4_c0\",\"x_v4_c1\",\"x_v4_c2\",\"x_v4_c3\",\"x_v4_c4\",\"y_e0_c0\",\"y_e0_c1\",\"y_e0_c2\",\"y_e0_c3\",\"y_e0_c4\",\"y_e1_c0\",\"y_e1_c1\",\"y_e1_c2\",\"y_e1_c3\",\"y_e1_c4\",\"y_e2_c0\",\"y_e2_c1\",\"y_e2_c2\",\"y_e2_c3\",\"y_e2_c4\",\"y_e3_c0\",\"y_e3_c1\",\"y_e3_c2\",\"y_e3_c3\",\"y_e3_c4\",\"y_e4_c0\",\"y_e4_c1\",\"y_e4_c2\",\"y_e4_c3\",\"y_e4_c4\",\"y_e5_c0\",\"y_e5_c1\",\"y_e5_c2\",\"y_e5_c3\",\"y_e5_c4\",\"y_e6_c0\",\"y_e6_c1\",\"y_e6_c2\",\"y_e6_c3\",\"y_e6_c4\",\"z_c0\",\"z_c1\",\"z_c2\",\"z_c3\",\"z_c4\"],\"constraints\":[\"x_v0_c0 + x_v0_c1 + x_v0_c2 + x_v0_c3 + x_v0_c4 = 1\",\"x_v1_c0 + x_v1_c1 + x_v1_c2 + x_v1_c3 + x_v1_c4 = 1\",\"x_v2_c0 + x_v2_c1 + x_v2_c2 + x_v2_c3 + x_v2_c4 = 1\",\"x_v3_c0 + x_v3_c1 + x_v3_c2 + x_v3_c3 + x_v3_c4 = 1\",\"x_v4_c0 + x_v4_c1 + x_v4_c2 + x_v4_c3 + x_v4_c4 = 1\",\"y_e0_c0 + y_e0_c1 + y_e0_c2 + y_e0_c3 + y_e0_c4 = 1\",\"y_e1_c0 + y_e1_c1 + y_e1_c2 + y_e1_c3 + y_e1_c4 = 1\",\"y_e2_c0 + y_e2_c1 + y_e2_c2 + y_e2_c3 + y_e2_c4 = 1\",\"y_e3_c0 + y_e3_c1 + y_e3_c2 + y_e3_c3 + y_e3_c4 = 1\",\"y_e4_c0 + y_e4_c1 + y_e4_c2 + y_e4_c3 + y_e4_c4 = 1\",\"y_e5_c0 + y_e5_c1 + y_e5_c2 + y_e5_c3 + y_e5_c4 = 1\",\"y_e6_c0 + y_e6_c1 + y_e6_c2 + y_e6_c3 + y_e6_c4 = 1\",\"x_v1_c0 + x_v3_c0 <= 1\",\"x_v1_c1 + x_v3_c1 <= 1\",\"x_v1_c2 + x_v3_c2 <= 1\",\"x_v1_c3 + x_v3_c3 <= 1\",\"x_v1_c4 + x_v3_c4 <= 1\",\"x_v0_c0 + x_v2_c0 <= 1\",\"x_v0_c1 + x_v2_c1 <= 1\",\"x_v0_c2 + x_v2_c2 <= 1\",\"x_v0_c3 + x_v2_c3 <= 1\",\"x_v0_c4 + x_v2_c4 <= 1\",\"x_v4_c0 + x_v0_c0 <= 1\",\"x_v4_c1 + x_v0_c1 <= 1\",\"x_v4_c2 + x_v0_c2 <= 1\",\"x_v4_c3 + x_v0_c3 <= 1\",\"x_v4_c4 + x_v0_c4 <= 1\",\"x_v3_c0 + x_v4_c0 <= 1\",\"x_v3_c1 + x_v4_c1 <= 1\",\"x_v3_c2 + x_v4_c2 <= 1\",\"x_v3_c3 + x_v4_c3 <= 1\",\"x_v3_c4 + x_v4_c4 <= 1\",\"x_v2_c0 + x_v3_c0 <= 1\",\"x_v2_c1 + x_v3_c1 <= 1\",\"x_v2_c2 + x_v3_c2 <= 1\",\"x_v2_c3 + x_v3_c3 <= 1\",\"x_v2_c4 + x_v3_c4 <= 1\",\"x_v1_c0 + x_v2_c0 <= 1\",\"x_v1_c1 + x_v2_c1 <= 1\",\"x_v1_c2 + x_v2_c2 <= 1\",\"x_v1_c3 + x_v2_c3 <= 1\",\"x_v1_c4 + x_v2_c4 <= 1\",\"x_v0_c0 + x_v1_c0 <= 1\",\"x_v0_c1 + x_v1_c1 <= 1\",\"x_v0_c2 + x_v1_c2 <= 1\",\"x_v0_c3 + x_v1_c3 <= 1\",\"x_v0_c4 + x_v1_c4 <= 1\",\"x_v0_c0 + y_e1_c0 + y_e2_c0 + y_e6_c0 <= 1\",\"x_v0_c1 + y_e1_c1 + y_e2_c1 + y_e6_c1 <= 1\",\"x_v0_c2 + y_e1_c2 + y_e2_c2 + y_e6_c2 <= 1\",\"x_v0_c3 + y_e1_c3 + y_e2_c3 + y_e6_c3 <= 1\",\"x_v0_c4 + y_e1_c4 + y_e2_c4 + y_e6_c4 <= 1\",\"x_v1_c0 + y_e0_c0 + y_e5_c0 + y_e6_c0 <= 1\",\"x_v1_c1 + y_e0_c1 + y_e5_c1 + y_e6_c1 <= 1\",\"x_v1_c2 + y_e0_c2 + y_e5_c2 + y_e6_c2 <= 1\",\"x_v1_c3 + y_e0_c3 + y_e5_c3 + y_e6_c3 <= 1\",\"x_v1_c4 + y_e0_c4 + y_e5_c4 + y_e6_c4 <= 1\",\"x_v2_c0 + y_e1_c0 + y_e4_c0 + y_e5_c0 <= 1\",\"x_v2_c1 + y_e1_c1 + y_e4_c1 + y_e5_c1 <= 1\",\"x_v2_c2 + y_e1_c2 + y_e4_c2 + y_e5_c2 <= 1\",\"x_v2_c3 + y_e1_c3 + y_e4_c3 + y_e5_c3 <= 1\",\"x_v2_c4 + y_e1_c4 + y_e4_c4 + y_e5_c4 <= 1\",\"x_v3_c0 + y_e0_c0 + y_e3_c0 + y_e4_c0 <= 1\",\"x_v3_c1 + y_e0_c1 + y_e3_c1 + y_e4_c1 <= 1\",\"x_v3_c2 + y_e0_c2 + y_e3_c2 + y_e4_c2 <= 1\",\"x_v3_c3 + y_e0_c3 + y_e3_c3 + y_e4_c3 <= 1\",\"x_v3_c4 + y_e0_c4 + y_e3_c4 + y_e4_c4 <= 1\",\"x_v4_c0 + y_e2_c0 + y_e3_c0 <= 1\",\"x_v4_c1 + y_e2_c1 + y_e3_c1 <= 1\",\"x_v4_c2 + y_e2_c2 + y_e3_c2 <= 1\",\"x_v4_c3 + y_e2_c3 + y_e3_c3 <= 1\",\"x_v4_c4 + y_e2_c4 + y_e3_c4 <= 1\",\"x_v0_c0 - z_c0 <= 0\",\"x_v0_c1 - z_c1 <= 0\",\"x_v0_c2 - z_c2 <= 0\",\"x_v0_c3 - z_c3 <= 0\",\"x_v0_c4 - z_c4 <= 0\",\"x_v1_c0 - z_c0 <= 0\",\"x_v1_c1 - z_c1 <= 0\",\"x_v1_c2 - z_c2 <= 0\",\"x_v1_c3 - z_c3 <= 0\",\"x_v1_c4 - z_c4 <= 0\",\"x_v2_c0 - z_c0 <= 0\",\"x_v2_c1 - z_c1 <= 0\",\"x_v2_c2 - z_c2 <= 0\",\"x_v2_c3 - z_c3 <= 0\",\"x_v2_c4 - z_c4 <= 0\",\"x_v3_c0 - z_c0 <= 0\",\"x_v3_c1 - z_c1 <= 0\",\"x_v3_c2 - z_c2 <= 0\",\"x_v3_c3 - z_c3 <= 0\",\"x_v3_c4 - z_c4 <= 0\",\"x_v4_c0 - z_c0 <= 0\",\"x_v4_c1 - z_c1 <= 0\",\"x_v4_c2 - z_c2 <= 0\",\"x_v4_c3 - z_c3 <= 0\",\"x_v4_c4 - z_c4 <= 0\",\"y_e0_c0 - z_c0 <= 0\",\"y_e0_c1 - z_c1 <= 0\",\"y_e0_c2 - z_c2 <= 0\",\"y_e0_c3 - z_c3 <= 0\",\"y_e0_c4 - z_c4 <= 0\",\"y_e1_c0 - z_c0 <= 0\",\"y_e1_c1 - z_c1 <= 0\",\"y_e1_c2 - z_c2 <= 0\",\"y_e1_c3 - z_c3 <= 0\",\"y_e1_c4 - z_c4 <= 0\",\"y_e2_c0 - z_c0 <= 0\",\"y_e2_c1 - z_c1 <= 0\",\"y_e2_c2 - z_c2 <= 0\",\"y_e2_c3 - z_c3 <= 0\",\"y_e2_c4 - z_c4 <= 0\",\"y_e3_c0 - z_c0 <= 0\",\"y_e3_c1 - z_c1 <= 0\",\"y_e3_c2 - z_c2 <= 0\",\"y_e3_c3 - z_c3 <= 0\",\"y_e3_c4 - z_c4 <= 0\",\"y_e4_c0 - z_c0 <= 0\",\"y_e4_c1 - z_c1 <= 0\",\"y_e4_c2 - z_c2 <= 0\",\"y_e4_c3 - z_c3 <= 0\",\"y_e4_c4 - z_c4 <= 0\",\"y_e5_c0 - z_c0 <= 0\",\"y_e5_c1 - z_c1 <= 0\",\"y_e5_c2 - z_c2 <= 0\",\"y_e5_c3 - z_c3 <= 0\",\"y_e5_c4 - z_c4 <= 0\",\"y_e6_c0 - z_c0 <= 0\",\"y_e6_c1 - z_c1 <= 0\",\"y_e6_c2 - z_c2 <= 0\",\"y_e6_c3 - z_c3 <= 0\",\"y_e6_c4 - z_c4 <= 0\"],\"optimizationFunction\":\"z_c0 + z_c1 + z_c2 + z_c3 + z_c4\",\"minimize\":true}\n";



    public static int makeRequest(int port) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + "localhost" + ":" + port + "/post"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode();
    }


    public static int getRequest(int port) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + "localhost" + ":" + port + "/post"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode();
    }


}
