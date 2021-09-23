package com.matthewcasperson.azureapi.controllers;

import com.azure.core.credential.TokenCredential;
import com.azure.core.util.BinaryData;
import com.azure.identity.AuthorizationCodeCredential;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UploadFileController {
    @PutMapping("/upload/{fileName}")
    public void upload(@RequestBody String content,
                       String fileName,
                       @AuthenticationPrincipal OidcUser principal,
                       @RegisteredOAuth2AuthorizedClient("storage") OAuth2AuthorizedClient client) {

        TokenCredential tokenCredential = new AuthorizationCodeCredential(client.getAccessToken().toString());
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().credential(tokenCredential).buildClient();
        BlobContainerClient containerClient = blobServiceClient.createBlobContainer(principal.getEmail());
        BlobClient blobClient = containerClient.getBlobClient(fileName);
        blobClient.upload(BinaryData.fromString(content));
    }
}
