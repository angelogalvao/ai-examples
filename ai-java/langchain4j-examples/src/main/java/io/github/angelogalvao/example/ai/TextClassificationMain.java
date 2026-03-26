package io.github.angelogalvao.example.ai;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ResponseFormatType;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.output.JsonSchemas;

/**
 * In this example, this class uses Google's Gemini model to do text classification.
 */
public class TextClassificationMain {

    public enum Label { PERSISTENCE, UI, EVENT, GENERIC }

    public record IssueClassification(Label category) {}

    @SystemMessage("""
            You are a bot in charge of categorizing issues from a bug tracker.
    """)
    public interface LabelDetector {

        /**
         * This is an example of "few-shot prompting", which gives the LLM some examples of how to reasoning.
         */
        @UserMessage("""
            Analyze the provided issue and categorize into one of the category.
            
            The issues opened are for Java projects so you can expect some Java acronyms,
            use them to categorize the issues as well.

            The possible values for a category must be PERSISTENCE, UI, EVENT or GENERIC.

            In case of not knowing how to categorize use the GENERIC label.

            Some examples of you might find:

            INPUT: Entity is not persisted
            OUTPUT: PERSISTENCE

            INPUT: JPA is failing to configure entities
            OUTPUT: PERSISTENCE

            INPUT: The element is not visible in the web
            OUTPUT: UI

            INPUT: The event is sent but never received
            OUTPUT: EVENT

            INPUT: Kafka streaming is failing in some circumstances
            OUTPUT: EVENT

            INPUT: java.lang.NullPointerException in a request
            OUTPUT: GENERIC

            INPUT: {{issueTitle}}
            OUTPUT:
        """)
        IssueClassification categorizeIssue(@V("issueTitle") String issueTitle);
    }

    static void main() {

        ChatModel model = GoogleAiGeminiChatModel.builder()
                .apiKey(Config.GOOGLE_API_KEY)
                .modelName("gemini-2.5-flash")
                .responseFormat(
                        ResponseFormat.builder()
                                .type(ResponseFormatType.JSON)
                                .jsonSchema(
                                        JsonSchemas.jsonSchemaFrom(IssueClassification.class).get()
                                ).build()
                )
                .build();

        LabelDetector labelDetector = AiServices.builder(LabelDetector.class)
                .chatModel(model)
                .build();

        IssueClassification label1 = labelDetector.categorizeIssue("When storing a user in the database, it throws an exception.");

        System.out.println(label1);

        IssueClassification label2 = labelDetector.categorizeIssue("JDBC connection exception thrown.");

        System.out.println(label2);

        IssueClassification label3 = labelDetector.categorizeIssue("Math operations fails when dividing by 0.");

        System.out.println(label3);




    }
}
