# AI Java projects

This folder contains multiple java projects running some aspect of AI. 


The list bellow contain projects that I created when I was studying LangChain4J, and DJL based in the book **Applied AI for Enterprise Java Development** of the authors *Alex Soto Bueno*, *Markus Eisele*, and *Natale Vinto* (the book is based on LangChain4J version 1.0.1, but this code is based on 1.12.2).

1. **fraud-client-inference:** This is a simple project that uses DJL API for inference (It doesn't integrate with any LLM).
2. **fraud-client:** This is a simple project that integrates with **fraud-client-inference**, acting as a client  (It doesn't integrate with any LLM).
3. **langchain4j-examples:** This is a collection of basic examples on how to use LangChain4J
4. **optical-character-recognition:** This is an application that reads a image and provide a text description of it.
5. **sentiment-analysis-chatbot-quarkus:** This is a simple chatbot that runs sentiment analysis developed using Langchain4J and Quarkus.
6. **theme-park-ai:** This is a complete chatbot that calls tools and it is developed using Langchain4J and Quarkus.
7. **triage-service-spring-boot** This is a service that runs sentiment analysis developed using Langchain4J and Spring Boot.



Make sure that you configure the API keys of the following LLM provider as environment variables:

| Provider | API Key env variable |
| -------- | -------------------- |
| Open AI  | OPENAI_API_KEY       |
| Gemini   | GOOGLE_API_KEY       |