import os

OPENAI_API_KEY = "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"
GOOGLE_API_KEY = "AIzaSyChrnShxggw8xfoAIfq9vopuH799JumZCY"
ANTHROPIC_API_KEY = "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"

def set_environment():
    variable_dict = globals().items()

    for key, value in variable_dict:
        if "API" in key or "ID" in key:
            os.environ[key] = value