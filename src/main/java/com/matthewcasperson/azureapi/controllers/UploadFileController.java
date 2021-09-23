package com.matthewcasperson.azureapi.controllers;

import com.azure.core.credential.AccessToken;
import com.azure.core.credential.TokenCredential;
import com.azure.core.credential.TokenRequestContext;
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
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.time.ZoneOffset;

@RestController
public class UploadFileController {
    @PutMapping("/upload/{fileName}")
    public void upload(@RequestBody String content,
                       @PathVariable("fileName") String fileName,
                       BearerTokenAuthentication principal,
                       @RegisteredOAuth2AuthorizedClient("storage") OAuth2AuthorizedClient client) {

        TokenCredential tokenCredential = request -> Mono.just(new AccessToken(
                    client.getAccessToken().getTokenValue(),
                    client.getAccessToken().getExpiresAt().atOffset(ZoneOffset.UTC))
        );

        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .credential(tokenCredential)
                .endpoint("https://msaldemostorageaccount.blob.core.windows.net")
                .buildClient();

        BlobContainerClient containerClient = blobServiceClient.createBlobContainer(
                principal.getTokenAttributes().get("upn").toString().replaceAll("[^A-Za-z0-9\\-]", "-"));
        BlobClient blobClient = containerClient.getBlobClient(fileName);
        blobClient.upload(BinaryData.fromString(content));
    }
}
