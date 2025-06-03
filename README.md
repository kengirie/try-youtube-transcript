# YouTube Transcript API Tutorial

[YouTube Transcript API](https://github.com/trldvix/youtube-transcript-api) の使い方を学ぶためのシンプルなチュートリアル

### 依存関係

```gradle
dependencies {
    implementation 'io.github.thoroldvix:youtube-transcript-api:0.3.6'
}
```

## チュートリアル内容

### 1. 基本的な字幕取得
```java
YoutubeTranscriptApi api = TranscriptApiFactory.createDefault();
TranscriptContent content = api.getTranscript(videoId);
```

### 2. 字幕リストの操作
```java
TranscriptList transcriptList = api.listTranscripts(videoId);
for (Transcript transcript : transcriptList) {
    System.out.println(transcript.getLanguage());
}
```

### 3. 言語選択とフォールバック
```java
// 英語を優先、なければドイツ語、フランス語の順で取得
Transcript transcript = transcriptList.findTranscript("en", "de", "fr");
```

### 4. 字幕の翻訳
```java
if (transcript.isTranslatable()) {
    Transcript translatedTranscript = transcript.translate("ja");
}
```

### 5. 異なる出力フォーマット
- プレーンテキスト
- JSON
- WebVTT
- SRT

### 6. 字幕メタデータ
- 動画ID
- 言語情報
- 自動生成かどうか
- 翻訳可能かどうか
- 利用可能な翻訳言語

## 実行方法

```bash
# プロジェクトをビルド
./gradlew build

# チュートリアルを実行
./gradlew run
```

## 実行結果例

```
=== YouTube Transcript API Tutorial ===

1. Basic Transcript Retrieval
=============================
Retrieved transcript with 61 fragments
First 3 fragments:
  [1.36s] [♪♪♪]
  [18.64s] ♪ We're no strangers to love ♪
  [22.64s] ♪ You know the rules and so do I ♪

2. Working with Transcript Lists
================================
Available transcripts:
  - German (Germany) (de-DE) - Manual
  - Portuguese (Brazil) (pt-BR) - Manual
  - Japanese (ja) - Manual
  - English (en) - Manual
  - Spanish (Latin America) (es-419) - Manual
  - English (auto-generated) (en) - Auto-generated
```

## 主要クラス

- `YoutubeTranscriptApi`: メインのAPIクラス
- `TranscriptList`: 利用可能な字幕のリスト
- `Transcript`: 個別の字幕オブジェクト
- `TranscriptContent`: 字幕の内容
- `TranscriptFormatter`: 字幕のフォーマット変換
- `TranscriptFormatters`: 組み込みフォーマッター

## 参考リンク

- [GitHub Repository](https://github.com/trldvix/youtube-transcript-api)
- [Maven Central](https://search.maven.org/artifact/io.github.thoroldvix/youtube-transcript-api)
- [JavaDoc](https://thoroldvix.github.io/youtube-transcript-api/javadoc/)
