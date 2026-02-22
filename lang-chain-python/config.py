import os

OPENAI_API_KEY = "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"
ANTHROPIC_API_KEY = "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"
LANGSMITH_API_KEY = "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"

GOOGLE_API_KEY = "AIzaSyChrnShxggw8xfoAIfq9vopuH799JumZCY"
GOOGLE_APPLICATION_CREDENTIALS = "/Users/asouza/.config/gcloud/application_default_credentials.json"
GOOGLE_CLOUD_PROJECT = "gen-lang-client-0569659535"

def set_environment():
    variable_dict = globals().items()

    for key, value in variable_dict:
        if "API" in key or "ID" in key or "GOOGLE" in key:
            os.environ[key] = value