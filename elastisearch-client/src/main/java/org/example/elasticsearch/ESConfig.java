package org.example.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.TransportUtils;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;

@Configuration
public class ESConfig {
    @Bean
    ElasticsearchClient elasticsearchClient() throws IOException {
        // if use CA cert file
        File certFile = new File("/home/ฅ^｡ꞈ｡^ฅ/http_ca.crt");
        SSLContext sslContext = TransportUtils
                .sslContextFromHttpCaCrt(certFile);

        // username/pw
        BasicCredentialsProvider credsProv = new BasicCredentialsProvider();
        credsProv.setCredentials(
                AuthScope.ANY, new UsernamePasswordCredentials("elastic", "jfYsA7=-j_dSUWfmp6HK")
        );

        // Create the low-level client
        RestClient restClient = RestClient
                .builder(new HttpHost("localhost", 9200, "https"))    // host:string, port: int, protocol: string
                .setHttpClientConfigCallback(hc -> hc
                        .setSSLContext(sslContext)
                        .setDefaultCredentialsProvider(credsProv)
                )
                .build();
        // Create the transport and the API client
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());

        ElasticsearchClient esClient = new ElasticsearchClient(transport);
        return esClient;
    }
}
