package io.github.angelogalvao.example.ai;

import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import static io.github.angelogalvao.example.ai.Config.GOOGLE_API_KEY;

/**
 * In this example, this class uses Google's Gemini model to describe images.
 */
public class ImageDescriptorMain {

    void main() throws Exception {
        ChatModel model = GoogleAiGeminiChatModel.builder()
                .apiKey(GOOGLE_API_KEY)
                .modelName("gemini-2.5-flash")
                .build();

        try(InputStream is = ImageDescriptorMain.class.getResourceAsStream("/capybara.png")) {
            final String base64Img = readImageInBase64(is);

            final ImageContent imageContent = ImageContent.from(base64Img, "image/png");

            final TextContent question = TextContent.from("What do you see from the image?");

            final UserMessage userMessage = UserMessage.from(question, imageContent);

            final ChatResponse chatResponse = model.chat(userMessage);

            System.out.println(chatResponse.aiMessage().text());
        }
    }


    private static String readImageInBase64(InputStream is) throws IOException {
        final byte[] imageBytes = is.readAllBytes();
        String base64Img = Base64.getEncoder().encodeToString(imageBytes);
        return base64Img;
    }
}
