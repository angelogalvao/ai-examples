package io.github.angelogalvao.example.ai;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import dev.langchain4j.model.output.structured.Description;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.UserMessage;

import java.time.LocalDate;

import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_1_MINI;
import static io.github.angelogalvao.example.ai.Config.OPENAI_API_KEY;

/**
 * In this example, this class uses OpenAI model to extract unstructured text into a Java Object via tha AI service approach.
 */
public class UnstructuredTextMain {

    /**
     * This record holds the information from the unstructured text. Notice that the annotation @Description to explain
     * exactly the purpose of the filed to the model.
     *
     * @param name
     * @param iban
     * @param transactionDate
     * @param amount
     */
    public record TransactionInfo(
            @Description("full name") String name,
            @Description("IBAN value") String iban,
            @Description("Date of the transaction") LocalDate transactionDate,
            @Description("Amount in dollars of the transaction") double amount
    ) {}

    /**
     * This is the AI Service class.
     */
    public interface Transaction {

        /**
         * The {{it}} means the only parameter.
         */
        @UserMessage("Extract information about a transaction from {{it}}")
        TransactionInfo extract(String message);
    }

    static void main() {

        System.out.println("Unstructured text - Calling Open AI with the key: " + OPENAI_API_KEY);

        ChatModel model = OpenAiChatModel.builder()
                .apiKey(OPENAI_API_KEY)
                .modelName(GPT_4_1_MINI)
                .build();

        Transaction tx = AiServices.builder(Transaction.class)
                .chatModel(model)
                .build();

        TransactionInfo transactionInfo = tx.extract("My name is Alex; I did a transaction on July 4th, 2023 from my account with IBAN 123456789 of $25.5");

        System.out.println(transactionInfo);
    }
}
