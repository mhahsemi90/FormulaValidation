package graph;

import net.minidev.json.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class MainClass {
    public static void main(String[] args) {
        String line, queryString, url;
        url = "http://localhost:8080/graphql";
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;

        client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);

        httpPost.addHeader("Authorization", "Basic VG91**");
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-Type", "application/json");

        try {

            String query = "{wildAnimal: findAnimal(isWild: true){...comparisonFields}domesticAnimal: findAnimal(isWild: false){...comparisonFields}}";
            query = query + ",variables: { \"myId\": 7}";

            Map<String, Object> variables1 = new HashMap<>();

            variables1.put("query", query);
            //variables1.put("variables", variables);

            JSONObject jsonobj;
            jsonobj = new JSONObject(variables1);


            StringEntity entity = new StringEntity(jsonobj.toString());
            httpPost.setEntity(entity);


            response = client.execute(httpPost);

            BufferedReader bufReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder builder = new StringBuilder();

            while ((line = bufReader.readLine()) != null) {
                builder.append(line);
                builder.append(System.lineSeparator());
            }

            System.out.println(builder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
