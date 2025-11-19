package dev.agiro.matriarch.suppliers.text;

import dev.agiro.matriarch.suppliers.base.RandomSupplier;

/**
 * Supplier that generates random words.
 */
public class WordSupplier extends RandomSupplier<String> {

    private static final String[] WORDS = {
            "alpha", "bravo", "charlie", "delta", "echo", "foxtrot", "golf", "hotel",
            "india", "juliet", "kilo", "lima", "mike", "november", "oscar", "papa", "quebec",
            "romeo", "sierra", "tango", "uniform", "victor", "whiskey", "xray", "yankee", "zulu",
            "able", "baker", "easy", "dog", "fox", "george", "how", "item", "jig", "king", "love",
            "nan", "oboe", "peter", "queen", "roger", "sugar", "tare", "uncle", "william", "yoke",
            "zebra", "account", "action", "address", "ancient", "animal", "answer", "appeal", "appear",
            "approach", "arrange", "attempt", "attract", "balance", "battery", "benefit", "brother",
            "business", "cabinet", "captain", "century", "chamber", "chapter", "chicken", "command",
            "company", "compare", "compete", "compete", "concept", "concern", "conduct", "confirm",
            "connect", "consent", "contact", "contain", "content", "context", "control", "convert",
            "correct", "council", "country", "courage", "culture", "current", "station", "strategy",
            "student", "success", "suggest", "support", "surface", "surprise", "survive", "suspect",
            "sustain", "teacher", "tension", "theatre", "therapy", "thought", "tonight", "tourist",
            "towards", "traffic", "trouble", "usually", "variety", "vehicle", "version", "village",
            "violent", "virtual", "Visible", "visitor", "voltage", "volume", "warrior", "weather",
            "website", "wedding", "weekend", "welcome", "western", "whereas", "whether", "whisper",
            "willing", "window", "winning", "wireless", "without", "witness", "wonderful", "workshop",
            "worried", "worship", "writing", "absolute", "academic", "accident", "accurate", "achieve",
            "acknowledge", "acquire", "activity", "actually", "addition", "adequate", "adjacent",
            "adjust", "admission", "advance", "advantage", "advocate", "aesthetic", "affection",
            "afternoon", "aggregate", "aircraft", "slightly", "alliance", "allocate", "alongside",
            "although", "aluminum", "amazing", "ambition", "amendment", "analysis", "ancient",
            "announce", "anything", "anywhere", "apparent", "appendix", "appetite", "applause",
            "apple", "approach", "approval", "arbitrary", "architect", "argument", "arrange",
            "arrest", "arrival", "article", "artistic", "assembly", "assessment", "assignment",
            "assistant", "associate", "assume", "assure", "athletic", "atmosphere", "attach",
            "attack", "attempt", "attend", "attention", "attitude", "attract", "auction",
            "audience", "august", "author", "authority", "automatic", "autonomy", "autumn",
            "available", "average", "aviation", "avoid", "awake", "award", "awesome", "awful"
    };

    @Override
    public String get() {
        return randomElement(WORDS);
    }
}

