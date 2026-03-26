package io.github.angelogalvao.example.ai.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.github.angelogalvao.example.ai.model.Evaluation;
import io.quarkiverse.langchain4j.RegisterAiService;

import java.util.List;

@RegisterAiService
@SystemMessage("""
    Analyze the sentiment of the text bellow.
    Respond only with one word to describe the sentiment.
""")
public interface SentimentAnalysis {

    @UserMessage("""

        Your task is to process the review delimited by ---.

         The possible sentiment values are:
         {#for s in sentiments}
            {s.name()}
         {/for}

         ---
         {review}
         ---
    """)
    Evaluation triage(List<Evaluation> sentiments, String review);
}
