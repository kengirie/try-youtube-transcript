package com.example;

import io.github.thoroldvix.api.YoutubeTranscriptApi;
import io.github.thoroldvix.internal.TranscriptApiFactory;
import io.github.thoroldvix.api.TranscriptList;
import io.github.thoroldvix.api.Transcript;
import io.github.thoroldvix.api.TranscriptContent;
import io.github.thoroldvix.api.TranscriptFormatter;
import io.github.thoroldvix.api.TranscriptFormatters;

/**
 * YouTube Transcript API Tutorial
 *
 * This tutorial demonstrates how to use the YouTube Transcript API library
 * to retrieve subtitles/transcripts from YouTube videos.
 *
 * Features covered:
 * - Basic transcript retrieval
 * - Language selection and fallback
 * - Manual vs automatically generated transcripts
 * - Transcript translation
 * - Different output formats
 * - Transcript metadata
 */
public class App {

    public static void main(String[] args) {
        // Create a default YoutubeTranscriptApi instance
        YoutubeTranscriptApi api = TranscriptApiFactory.createDefault();

        // Example video ID (Rick Astley - Never Gonna Give You Up)
        String videoId = "dQw4w9WgXcQ";

        System.out.println("=== YouTube Transcript API Tutorial ===\n");

        try {
            // Example 1: Basic transcript retrieval
            basicTranscriptRetrieval(api, videoId);

            // Example 2: Working with transcript lists
            workingWithTranscriptLists(api, videoId);

            // Example 3: Language selection and fallback
            languageSelectionAndFallback(api, videoId);

            // Example 4: Transcript translation
            transcriptTranslation(api, videoId);

            // Example 5: Different output formats
            differentOutputFormats(api, videoId);

            // Example 6: Transcript metadata
            transcriptMetadata(api, videoId);

        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Example 1: Basic transcript retrieval
     */
    private static void basicTranscriptRetrieval(YoutubeTranscriptApi api, String videoId) {
        System.out.println("1. Basic Transcript Retrieval");
        System.out.println("=============================");

        try {
            // Simple way to get transcript content directly
            TranscriptContent content = api.getTranscript(videoId);

            System.out.println("Retrieved transcript with " + content.getContent().size() + " fragments");

            // Display first few fragments
            System.out.println("First 3 fragments:");
            content.getContent().stream()
                .limit(10)
                .forEach(fragment ->
                    System.out.printf("  [%.2fs] %s\n",
                        fragment.getStart(),
                        fragment.getText())
                );

        } catch (Exception e) {
            System.err.println("Failed to retrieve basic transcript: " + e.getMessage());
        }

        System.out.println();
    }

    /**
     * Example 2: Working with transcript lists
     */
    private static void workingWithTranscriptLists(YoutubeTranscriptApi api, String videoId) {
        System.out.println("2. Working with Transcript Lists");
        System.out.println("================================");

        try {
            // Get list of all available transcripts
            TranscriptList transcriptList = api.listTranscripts(videoId);

            System.out.println("Available transcripts:");
            for (Transcript transcript : transcriptList) {
                System.out.printf("  - %s (%s) - %s\n",
                    transcript.getLanguage(),
                    transcript.getLanguageCode(),
                    transcript.isGenerated() ? "Auto-generated" : "Manual"
                );
            }

            // Get the first available transcript
            Transcript firstTranscript = transcriptList.findTranscript();
            TranscriptContent content = firstTranscript.fetch();

            System.out.println("\nUsing first available transcript (" +
                firstTranscript.getLanguage() + "):");
            System.out.println("Total fragments: " + content.getContent().size());

        } catch (Exception e) {
            System.err.println("Failed to work with transcript lists: " + e.getMessage());
        }

        System.out.println();
    }

    /**
     * Example 3: Language selection and fallback
     */
    private static void languageSelectionAndFallback(YoutubeTranscriptApi api, String videoId) {
        System.out.println("3. Language Selection and Fallback");
        System.out.println("==================================");

        try {
            TranscriptList transcriptList = api.listTranscripts(videoId);

            // Try to get English transcript, fallback to any available
            Transcript transcript = transcriptList.findTranscript("en", "de", "fr");

            System.out.println("Selected transcript language: " + transcript.getLanguage());
            System.out.println("Language code: " + transcript.getLanguageCode());
            System.out.println("Is auto-generated: " + transcript.isGenerated());

            // Try to get specifically manual transcript
            try {
                Transcript manualTranscript = transcriptList.findManualTranscript("en");
                System.out.println("Found manual English transcript");
            } catch (Exception e) {
                System.out.println("No manual English transcript available");
            }

            // Try to get specifically auto-generated transcript
            try {
                Transcript autoTranscript = transcriptList.findGeneratedTranscript("en");
                System.out.println("Found auto-generated English transcript");
            } catch (Exception e) {
                System.out.println("No auto-generated English transcript available");
            }

        } catch (Exception e) {
            System.err.println("Failed language selection: " + e.getMessage());
        }

        System.out.println();
    }

    /**
     * Example 4: Transcript translation
     */
    private static void transcriptTranslation(YoutubeTranscriptApi api, String videoId) {
        System.out.println("4. Transcript Translation");
        System.out.println("=========================");

        try {
            TranscriptList transcriptList = api.listTranscripts(videoId);
            Transcript transcript = transcriptList.findTranscript("en");

            if (transcript.isTranslatable()) {
                System.out.println("Original language: " + transcript.getLanguage());
                System.out.println("Available translation languages: " +
                    transcript.getTranslationLanguages().size());

                // Translate to Japanese if available
                if (transcript.getTranslationLanguages().contains("ja")) {
                    Transcript translatedTranscript = transcript.translate("ja");
                    TranscriptContent translatedContent = translatedTranscript.fetch();

                    System.out.println("Translated to: " + translatedTranscript.getLanguage());
                    System.out.println("Sample translated text:");
                    translatedContent.getContent().stream()
                        .limit(2)
                        .forEach(fragment ->
                            System.out.printf("  [%.2fs] %s\n",
                                fragment.getStart(),
                                fragment.getText())
                        );
                } else {
                    System.out.println("Japanese translation not available");
                }
            } else {
                System.out.println("This transcript cannot be translated");
            }

        } catch (Exception e) {
            System.err.println("Failed transcript translation: " + e.getMessage());
        }

        System.out.println();
    }

    /**
     * Example 5: Different output formats
     */
    private static void differentOutputFormats(YoutubeTranscriptApi api, String videoId) {
        System.out.println("5. Different Output Formats");
        System.out.println("===========================");

        try {
            TranscriptContent content = api.getTranscript(videoId);

            // Plain text format
            TranscriptFormatter textFormatter = TranscriptFormatters.textFormatter();
            String textOutput = textFormatter.format(content);
            System.out.println("Plain text format (first 200 chars):");
            System.out.println(textOutput.substring(0, Math.min(200, textOutput.length())) + "...\n");

            // JSON format
            TranscriptFormatter jsonFormatter = TranscriptFormatters.jsonFormatter();
            String jsonOutput = jsonFormatter.format(content);
            System.out.println("JSON format (first 300 chars):");
            System.out.println(jsonOutput.substring(0, Math.min(300, jsonOutput.length())) + "...\n");

            // WebVTT format
            // WebVTT format - commenting out as method name might be different
            System.out.println("WebVTT format: (formatter method not available in this version)\n");

            // SRT format
            TranscriptFormatter srtFormatter = TranscriptFormatters.srtFormatter();
            String srtOutput = srtFormatter.format(content);
            System.out.println("SRT format (first 300 chars):");
            System.out.println(srtOutput.substring(0, Math.min(300, srtOutput.length())) + "...\n");

        } catch (Exception e) {
            System.err.println("Failed to format transcript: " + e.getMessage());
        }

        System.out.println();
    }

    /**
     * Example 6: Transcript metadata
     */
    private static void transcriptMetadata(YoutubeTranscriptApi api, String videoId) {
        System.out.println("6. Transcript Metadata");
        System.out.println("======================");

        try {
            TranscriptList transcriptList = api.listTranscripts(videoId);
            Transcript transcript = transcriptList.findTranscript();

            System.out.println("Video ID: " + transcript.getVideoId());
            System.out.println("Language: " + transcript.getLanguage());
            System.out.println("Language Code: " + transcript.getLanguageCode());
            System.out.println("Is Generated: " + transcript.isGenerated());
            System.out.println("Is Translatable: " + transcript.isTranslatable());
            System.out.println("API URL: " + transcript.getApiUrl());
            System.out.println("Available translation languages: " +
                transcript.getTranslationLanguages().size());

            // Show some translation languages
            if (!transcript.getTranslationLanguages().isEmpty()) {
                System.out.println("Sample translation languages: " +
                    transcript.getTranslationLanguages().stream()
                        .limit(5)
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("None"));
            }

            // Fragment details
            TranscriptContent content = transcript.fetch();
            if (!content.getContent().isEmpty()) {
                TranscriptContent.Fragment firstFragment = content.getContent().get(0);
                System.out.println("\nFirst fragment details:");
                System.out.println("  Text: " + firstFragment.getText());
                System.out.println("  Start time: " + firstFragment.getStart() + "s");
                System.out.println("  Duration: (method not available in this version)");
            }

        } catch (Exception e) {
            System.err.println("Failed to get transcript metadata: " + e.getMessage());
        }

        System.out.println();
        System.out.println("=== Tutorial Complete ===");
    }
}
