import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Simple standalone utility to generate a list of 10k pseudo-words (2-15 letters)
 * and write them to src/main/resources/words/general.txt. Run once to populate the list.
 */
public class GenerateWordList {
    private static final String OUTPUT = "src/main/resources/words/general.txt";

    public static void main(String[] args) throws IOException {
        int target = 10_000;
        if (args.length > 0) {
            try { target = Integer.parseInt(args[0]); } catch (NumberFormatException ignored) {}
        }
        ensureParentDir(OUTPUT);
        Set<String> words = new HashSet<>(target * 2);
        Random rnd = new Random(42L);
        String[] syllables = {
                "ba","be","bi","bo","bu","ca","ce","ci","co","cu","da","de","di","do","du",
                "fa","fe","fi","fo","fu","ga","ge","gi","go","gu","ha","he","hi","ho","hu",
                "ja","je","ji","jo","ju","ka","ke","ki","ko","ku","la","le","li","lo","lu",
                "ma","me","mi","mo","mu","na","ne","ni","no","nu","pa","pe","pi","po","pu",
                "ra","re","ri","ro","ru","sa","se","si","so","su","ta","te","ti","to","tu",
                "va","ve","vi","vo","vu","wa","we","wi","wo","wu","xa","xe","xi","xo","xu",
                "ya","ye","yi","yo","yu","za","ze","zi","zo","zu","lo","ra","mi","ti","an",
                "al","ar","en","el","or","on","in","un","ul","ir","ion","ent","est","ist","ous",
                "ment","ful","less","ness","ship","craft","ward","hood","able","ible","ary","ory"
        };

        while (words.size() < target) {
            int parts = 1 + rnd.nextInt(4); // 1-4 syllables
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < parts; i++) {
                sb.append(syllables[rnd.nextInt(syllables.length)]);
                if (sb.length() > 15) break;
            }
            String w = sb.toString().toLowerCase().replaceAll("[^a-z]", "");
            if (w.length() >= 2 && w.length() <= 15) {
                words.add(w);
            }
        }

        // Keep any existing curated words at the top (if present)
        List<String> existing = Files.exists(Paths.get(OUTPUT)) ? Files.readAllLines(Paths.get(OUTPUT), StandardCharsets.UTF_8) : List.of();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT, false))) {
            int written = 0;
            for (String line : existing) {
                String s = line.trim();
                if (!s.isEmpty() && s.length() >= 2 && s.length() <= 15 && s.chars().allMatch(Character::isLetter)) {
                    bw.write(s);
                    bw.newLine();
                    written++;
                    if (written >= target) break;
                }
            }
            for (String w : words) {
                if (written >= target) break;
                bw.write(w);
                bw.newLine();
                written++;
            }
        }
        System.out.println("Generated " + target + " words into " + OUTPUT);
    }

    private static void ensureParentDir(String path) throws IOException {
        File f = new File(path).getParentFile();
        if (f != null && !f.exists()) {
            if (!f.mkdirs()) throw new IOException("Failed to create directory: " + f);
        }
    }
}

