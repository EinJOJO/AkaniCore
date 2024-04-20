package it.einjojo.akani.core.api.message;

public enum Language {
    GERMAN("de"),
    ENGLISH("en");
    private final String langKey;

    Language(String langKey) {
        this.langKey = langKey;
    }

    public String langKey() {
        return langKey;
    }
}
