package com.efreight.base.module.one.record.neone.config;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.File;

@Configuration
public class GraphDBConfig {

    private static final Logger log = LoggerFactory.getLogger(GraphDBConfig.class);

    @Value("${graphdb.repository-type:http}")
    private String repositoryType;

    @Value("${graphdb.http.url:}")
//    @Value("${graphdb.http.url:http://192.168.6.230:7200/repositories/neone}")
    private String httpRepositoryUrl;

    @Value("${graphdb.http.username:}")
    private String repositoryUsername;

    @Value("${graphdb.http.password:}")
    private String repositoryPassword;

    @Value("${graphdb.local.data-dir:}")
    private String repositoryDataDir;

    @Value("${graphdb.sparql.query-endpoint:}")
    private String sparqlQueryEndpoint;

    @Value("${graphdb.sparql.update-endpoint:}")
    private String sparqlUpdateEndpoint;

    @Bean(name = "rdf4jRepository")
    @Primary
    public Repository rdfRepository() {
        log.info("Initializing RDF Repository with type: {}", repositoryType);
        // 先统一转小写，避免大小写问题
        String type = repositoryType.toLowerCase();

        // 传统switch语句（JDK 1.8支持）
        switch (type) {
            case "http":
                return createHttpRepository();
            case "native":
                return createNativeRepository();
            case "sparql":
                return createSparqlRepository();
            default:
                return createInMemoryRepository();
        }
    }

    private Repository createHttpRepository() {
        if (StringUtils.isBlank(httpRepositoryUrl)) {
            throw new IllegalArgumentException(
                    "HTTP repository URL must be configured when repository-type is 'http'"
            );
        }
        log.info("Using HTTP repository at: {}", httpRepositoryUrl);
        HTTPRepository repository = new HTTPRepository(httpRepositoryUrl);

        if (StringUtils.isNotBlank(repositoryUsername)
                && StringUtils.isNotBlank(repositoryPassword)) {
            repository.setUsernameAndPassword(repositoryUsername, repositoryPassword);
        }
        return repository;
    }

    private Repository createInMemoryRepository() {
        log.warn("Using in-memory RDF store - data will not persist after restart");
        return new SailRepository(new MemoryStore());
    }

    private Repository createNativeRepository() {
        if (StringUtils.isBlank(repositoryDataDir)) {
            throw new IllegalArgumentException(
                    "Repository data directory must be configured when repository-type is 'native'"
            );
        }

        log.info("Using native RDF store at: {}", repositoryDataDir);
        File dataDir = new File(repositoryDataDir);
        return new SailRepository(new NativeStore(dataDir));
    }

    private Repository createSparqlRepository() {
        if (StringUtils.isBlank(sparqlQueryEndpoint)
                || StringUtils.isBlank(sparqlUpdateEndpoint)) {
            throw new IllegalArgumentException(
                    "Both SPARQL query and update endpoints must be configured when repository-type is 'sparql'"
            );
        }

        log.info("Using SPARQL repository - Query: {}, Update: {}",
                sparqlQueryEndpoint, sparqlUpdateEndpoint);

        SPARQLRepository repository = new SPARQLRepository(
                sparqlQueryEndpoint,
                sparqlUpdateEndpoint
        );

        if (StringUtils.isNotBlank(repositoryUsername)
                && StringUtils.isNotBlank(repositoryPassword)) {
            repository.setUsernameAndPassword(repositoryUsername, repositoryPassword);
        }

        return repository;
    }
}
