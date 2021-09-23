package com.matthewcasperson.azureapi.controllers;

import com.azure.core.credential.AccessToken;
import com.azure.core.credential.TokenCredential;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.specialized.BlockBlobClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.ZoneOffset;

@RestController
public class UploadFileController {
    @PutMapping("/upload/{fileName}")
    public void upload(@RequestBody String content,
                       @PathVariable("fileName") String fileName,
                       BearerTokenAuthentication principal,
                       @RegisteredOAuth2AuthorizedClient("storage") OAuth2AuthorizedClient client) {
        System.out.println("\n" + client.getAccessToken().getTokenValue() + "\n");

        TokenCredential tokenCredential = request -> Mono.just(new AccessToken(
                    client.getAccessToken().getTokenValue(),
                    client.getAccessToken().getExpiresAt().atOffset(ZoneOffset.UTC))
        );

        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .credential(tokenCredential)
                .endpoint("https://msaldemostorageaccount.blob.core.windows.net")
                .buildClient();

        String containerName = principal.getTokenAttributes().get("upn").toString().replaceAll("[^A-Za-z0-9\\-]", "-");

        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        if (!containerClient.exists()) containerClient.create();

        BlockBlobClient blockBlobClient = containerClient.getBlobClient(fileName).getBlockBlobClient();

        if (blockBlobClient.exists()) blockBlobClient.delete();

        try (ByteArrayInputStream dataStream = new ByteArrayInputStream(content.getBytes())) {
            blockBlobClient.upload(dataStream, content.length());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
