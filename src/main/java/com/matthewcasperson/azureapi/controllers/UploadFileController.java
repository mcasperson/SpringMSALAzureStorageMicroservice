package com.matthewcasperson.azureapi.controllers;

import com.azure.core.credential.AccessToken;
import com.azure.core.credential.TokenCredential;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.specialized.BlockBlobClient;
import com.matthewcasperson.azureapi.model.Audit;
import com.matthewcasperson.azureapi.repository.AuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    AuditRepository auditRepository;

    @PutMapping("/upload/{fileName}")
    public void upload(@RequestBody String content,
                       @PathVariable("fileName") String fileName,
                       BearerTokenAuthentication principal,
                       @RegisteredOAuth2AuthorizedClient("storage") OAuth2AuthorizedClient client) {
        System.out.println("\n" + client.getAccessToken().getTokenValue() + "\n");

        uploadFile(client, generateContainerName(principal), fileName, content);
        auditRepository.saveAndFlush(new Audit("Uploaded file " + fileName + "for user " + getPrincipalEmail(principal)));
    }

    private void uploadFile(OAuth2AuthorizedClient client, String container, String fileName, String content) {
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .credential(createTokenCredential(client))
                .endpoint("https://msaldemostorageaccount.blob.core.windows.net")
                .buildClient();

        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(container);
        if (!containerClient.exists()) containerClient.create();

        BlockBlobClient blockBlobClient = containerClient.getBlobClient(fileName).getBlockBlobClient();

        if (blockBlobClient.exists()) blockBlobClient.delete();

        try (ByteArrayInputStream dataStream = new ByteArrayInputStream(content.getBytes())) {
            blockBlobClient.upload(dataStream, content.length());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TokenCredential createTokenCredential(OAuth2AuthorizedClient client) {
        return request -> Mono.just(new AccessToken(
                client.getAccessToken().getTokenValue(),
                client.getAccessToken().getExpiresAt().atOffset(ZoneOffset.UTC)));
    }

    private String generateContainerName(BearerTokenAuthentication principal) {
        return getPrincipalEmail(principal).replaceAll("[^A-Za-z0-9\\-]", "-");
    }

    private String getPrincipalEmail(BearerTokenAuthentication principal) {
        return principal.getTokenAttributes().get("upn").toString();
    }
}
