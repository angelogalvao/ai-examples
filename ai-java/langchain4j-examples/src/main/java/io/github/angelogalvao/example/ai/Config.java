package io.github.angelogalvao.example.ai;

/**
 * This class contains the configuration for accessing external models, like Open AI, Gemini, etc.
 */
public class Config {

    public static final String OPENAI_API_KEY = System.getenv("OPENAI_API_KEY");

    public static final String GOOGLE_API_KEY = System.getenv("GOOGLE_API_KEY");
}
