package org.example.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.DistanceUnit;
import co.elastic.clients.elasticsearch._types.GeoDistanceSort;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Suggestion;
import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import co.elastic.clients.util.NamedValue;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.elasticsearch.client.RequestOptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.Set;

// ref: https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/current/getting-started-java.html#_deleting_documents
@SpringBootTest(classes = ESConfig.class)
public class ElasticsearchTest {
    @Autowired
    private ElasticsearchClient esClient;

    @Test
    void testIndexMapperCreation() throws IOException{
        final String INDEX_STRING = "{\"mappings\":{\"properties\":{\"age\":{\"type\":\"integer\"},\"email\":{\"type\":\"keyword\",\"index\":false},\"name\":{\"properties\":{\"firstName\":{\"type\":\"keyword\"},\"lastName\":{\"type\":\"keyword\"}}},\"employee-id\":{\"type\":\"keyword\",\"index\":false},\"info\":{\"type\":\"text\",\"analyzer\":\"standard\"}}}}";
        Reader input = new StringReader(INDEX_STRING);
        IndexRequest<JsonData> request = IndexRequest.of(i -> i
                .index("my-index")
                .withJson(input)
        );
        IndexResponse response = esClient.index(request);
        System.out.println("Indexed with version " + response.version());
    }

    @Test
    void testIndexExistAndDeletion() throws IOException {
        BooleanResponse existsResponse = esClient.indices().exists(i->i.index("my-index"));
        if (existsResponse.value()) {
            System.out.println("index exist, now execute deletion");
            DeleteIndexResponse deleteResponse = esClient.indices().delete(i->i.index("my-index"));
            System.out.println("deleted index: " + deleteResponse.toString());
        }
    }
    @Test
    void testGetDocument () throws IOException {
        GetResponse<ObjectNode> response = esClient.get(g -> g
                        .index("customer")
                        .id("1"),
                ObjectNode.class
        );

        if (response.found()) {
            ObjectNode json = response.source();
            String name = json.get("firstname").asText();
            System.out.println("firstname " + name);
        } else {
            System.out.println("Product not found");
        }
    }

    @Test
    void testQuery() throws  IOException {
        String searchText = "bike";
        double maxPrice = 200.0;

        // Search by term
        Query byTerm = TermQuery.of(t -> t
                .field("made_country")
                .value("Russia")
        )._toQuery();

        // Search by text
        Query byName = MatchQuery.of(m -> m
                .field("name")
                .query(searchText)
        )._toQuery();

        // Search by max price
        Query byMaxPrice = RangeQuery.of(r -> r
                .field("price")
                .gte(JsonData.of(maxPrice))
        )._toQuery();

        SearchResponse<ObjectNode> response = esClient.search(s -> s
                        .index("products")
                        .query(q -> q
                                .bool(b -> b
                                        .must(byName)
                                        .must(byMaxPrice)
                                )
                        ), ObjectNode.class
        );
    }
    @Test
    void testAggregation() throws IOException{
        Query query = MatchQuery.of(m -> m
                .field("name")
                .query("bike")
        )._toQuery();

        GeoDistanceSort geoDistanceSort = GeoDistanceSort.of(g->g
                .field("location")
                .location(l->l.latlon(f->f.lat(31.21).lon(121.5)))
                .unit(DistanceUnit.Kilometers)
                .order(SortOrder.Asc)
        );

        SearchResponse<Void> response = esClient.search(b -> b
                        .index("products")
                        .query(query)
                        .sort(s->s.field(f->f.field("price").order((SortOrder.Asc))))
                        .sort(s->s.geoDistance(geoDistanceSort))
                        .from(0)
                        .size(0)
                        .aggregations("bucketAgg", a -> a
                                .terms(t->t
                                        .field("Nation")
                                        .size(10)
                                        .order(NamedValue.of("scoreAgg.avg", SortOrder.Asc))
                                )
                                .aggregations("scoreAgg", agg->agg
                                        .stats(s-> s.field("price")))
                        ),
                Void.class
        );
    }

    @Test
    void testCompletion() throws IOException{
        SearchResponse<ObjectNode> response = esClient.search(b->b
                    .suggest(s ->s.suggesters("my-suggest", suggest->suggest
                                                    .prefix("s")
                                                    .completion(c->c.field("title").skipDuplicates(true).size(10))
                    )), ObjectNode.class
        );

        Set<Map.Entry<String, List<Suggestion<ObjectNode>>>> entries = response.suggest().entrySet();
    }

    @Test
    void testHighlighter() throws IOException {
        SearchResponse<ObjectNode> response = esClient.search(s->s
                .index("products")
                .query(q->q
                        .match(m->m
                            .query("bike")
                            .field("product"))
                )
                .highlight(h->h
                        .fields("name", f->f
                                .preTags("<em>")
                                .postTags("</em>")
                                .requireFieldMatch(false)
                        )
                )
                , ObjectNode.class);
    }
}
