package com.example;

import io.github.thoroldvix.api.YoutubeTranscriptApi;
import io.github.thoroldvix.internal.TranscriptApiFactory;
import io.github.thoroldvix.api.TranscriptContent;
import io.github.thoroldvix.api.TranscriptList;
import io.github.thoroldvix.api.Transcript;

/**
 * YouTube Transcript API - Simple Example
 *
 * This is a minimal example showing how to get started with the YouTube Transcript API.
 */
public class SimpleExample {

    public static void main(String[] args) {
        // Create API instance
        YoutubeTranscriptApi api = TranscriptApiFactory.createDefault();

        // Example video ID - try a few different ones
        String[] videoIds = {
            "dQw4w9WgXcQ",  // Rick Astley - Never Gonna Give You Up
            "9bZkp7q19f0",  // PSY - GANGNAM STYLE
            "kJQP7kiw5Fk"   // Luis Fonsi - Despacito
        };

        try {
            System.out.println("=== Simple YouTube Transcript Example ===\n");

            // Try different video IDs until one works
            String workingVideoId = null;
            TranscriptList workingTranscriptList = null;

            for (String testVideoId : videoIds) {
                try {
                    System.out.println("Trying video ID: " + testVideoId);
                    TranscriptList transcriptList = api.listTranscripts(testVideoId);
                    workingVideoId = testVideoId;
                    workingTranscriptList = transcriptList;
                    System.out.println("✓ Successfully found transcripts for video: " + testVideoId);
                    break;
                } catch (Exception e) {
                    System.out.println("✗ Failed for video " + testVideoId + ": " + e.getMessage());
                }
            }

            if (workingVideoId == null) {
                System.out.println("Could not find a working video. The API might be having issues.");
                return;
            }

            System.out.println("\n=== Working with video: " + workingVideoId + " ===\n");

            // Method 1: List available transcripts
            System.out.println("Method 1: List available transcripts");
            System.out.println("Available languages:");
            for (Transcript transcript : workingTranscriptList) {
                System.out.printf("  - %s (%s) %s\n",
                    transcript.getLanguage(),
                    transcript.getLanguageCode(),
                    transcript.isGenerated() ? "[Auto]" : "[Manual]"
                );
            }

            // Method 2: Get transcript metadata
            System.out.println("\nMethod 2: Transcript metadata");
            try {
                Transcript firstTranscript = workingTranscriptList.findTranscript();
                System.out.println("First available transcript:");
                System.out.println("  Language: " + firstTranscript.getLanguage());
                System.out.println("  Language Code: " + firstTranscript.getLanguageCode());
                System.out.println("  Is auto-generated: " + firstTranscript.isGenerated());
                System.out.println("  Is translatable: " + firstTranscript.isTranslatable());

                if (firstTranscript.isTranslatable()) {
                    System.out.println("  Available translations: " +
                        firstTranscript.getTranslationLanguages().size() + " languages");

                    // Show some available translation languages
                    System.out.println("  Sample languages: " +
                        firstTranscript.getTranslationLanguages().stream()
                            .limit(5)
                            .reduce((a, b) -> a + ", " + b)
                            .orElse("None"));
                }
            } catch (Exception e) {
                System.out.println("Could not get transcript metadata: " + e.getMessage());
            }

            // Method 3: Try to get transcript content
            System.out.println("\nMethod 3: Get transcript content");
            try {
                TranscriptContent content = api.getTranscript(workingVideoId);
                System.out.println("✓ Successfully retrieved " + content.getContent().size() + " fragments");

                // Show first few fragments
                System.out.println("First 3 fragments:");
                content.getContent().stream()
                    .limit(3)
                    .forEach(fragment ->
                        System.out.printf("  [%.2fs] %s\n",
                            fragment.getStart(),
                            fragment.getText())
                    );
            } catch (Exception e) {
                System.out.println("✗ Could not retrieve transcript content: " + e.getMessage());
                System.out.println("This might be due to YouTube API changes or video restrictions.");
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
