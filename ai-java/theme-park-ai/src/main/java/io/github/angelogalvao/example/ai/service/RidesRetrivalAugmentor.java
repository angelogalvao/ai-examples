package io.github.angelogalvao.example.ai.service;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.content.retriever.WebSearchContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.rag.query.router.LanguageModelQueryRouter;
import dev.langchain4j.rag.query.router.QueryRouter;
import dev.langchain4j.store.embedding.chroma.ChromaEmbeddingStore;
import dev.langchain4j.web.search.WebSearchEngine;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@ApplicationScoped
public class RidesRetrivalAugmentor implements Supplier<RetrievalAugmentor> {

    private final RetrievalAugmentor augmentor;

    public RidesRetrivalAugmentor(ChromaEmbeddingStore store, EmbeddingModel model, WebSearchEngine searchEngine, ChatModel languageModel) {

        ContentRetriever webSearchRetriver = WebSearchContentRetriever
                .builder()
                .webSearchEngine(searchEngine)
                .maxResults(3)
                .build();

        EmbeddingStoreContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingModel(model)
                .embeddingStore(store)
                .maxResults(10)
                .minScore(0.7)
                .build();

        Map<ContentRetriever, String> routing = new HashMap<>();

        routing.put(webSearchRetriver, "travel to the theme park");
        routing.put(contentRetriever , "description of a ride or minimum height to access to a ride");

        QueryRouter queryRouter = new LanguageModelQueryRouter(languageModel, routing);

        this.augmentor = DefaultRetrievalAugmentor.builder()
                .queryTransformer(query -> {
                    String original = query.text();

                    String newQuery = original + System.lineSeparator() + "The theme park is in Barcelona";

                    return Collections.singletonList(Query.from(newQuery, query.metadata()));
                })
                .queryRouter(queryRouter)
                //.contentRetriever(contentRetriever)
                .build();
    }
    @Override
    public RetrievalAugmentor get() {
        return this.augmentor;
    }
}
