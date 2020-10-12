/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package cmdApiKeys;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class App {

    public String getApiKeys(String apiPath) {

        JsonNode jsonNodeRoot = connectAndParse(apiPath);

        String outputText = "";
        JsonNode formTitleNode = jsonNodeRoot.get("title");
        outputText = outputText.concat("Form Title: " + formTitleNode.asText() + "\n");

        ArrayNode pagesNode = (ArrayNode) jsonNodeRoot.get("components");
        for (int pageNum = 0; pageNum < pagesNode.size(); pageNum++) {
            JsonNode pageNode = pagesNode.get(pageNum);
            String pageTitle = pageNode.get("title").asText();
            if (pageTitle != null) {
                outputText = outputText.concat("Page " + pageNum + ": " + pageTitle + "\n");
                outputText = outputText.concat("    * " + pageNode.get("key").asText() + "\n");
                ArrayNode panelComponentsNode = (ArrayNode) pageNode.get("components");
                for (int panelComponentNum = 0; panelComponentNum < panelComponentsNode.size(); panelComponentNum++) {
                    JsonNode panelComponentNode = panelComponentsNode.get(panelComponentNum);
                    outputText = outputText.concat("    * " + panelComponentNode.get("key").asText() + "\n");
                }
            }
        }

        return outputText;
    }

    JsonNode connectAndParse(String apiPath) {

        HttpRequest apiRequest = null;
        try {
            apiRequest = HttpRequest.newBuilder().header("x-token", "VwTdih9OdR2Z39nNLhvIElwZR5xLp2")
                    .uri(new URI(apiPath)).GET().build();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        HttpResponse<String> apiResponse = null;
        try {
            apiResponse = HttpClient.newBuilder().proxy(ProxySelector.getDefault()).build().send(apiRequest,
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // create a new Jackson ObjectMapper to read json
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = null;
        try {
            rootNode = mapper.readTree(apiResponse.body());
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return rootNode;
    }

    void writeKeyOutput(String apiKeyOutput, String outputFilename) {
        // write the data to an output file
        try {

            File myObj = new File(outputFilename);
            FileWriter myWriter = new FileWriter(outputFilename);
            myWriter.write(apiKeyOutput);
            myWriter.close();
            System.out.println("Successfully wrote to the " + outputFilename + " file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String apiPath = args[0];
        String outputFilename = args[1];
        App apiKeysApp = new App();
        String apiKeyOutput = apiKeysApp.getApiKeys(apiPath);
        apiKeysApp.writeKeyOutput(apiKeyOutput, outputFilename);
    }

}
