import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class BookApp {
    static final String BIO =
        "Rhethim Das is a Commerce student of Class 11 at Children's Foundation School and a young aspiring author with a passion for emotional and meaningful storytelling. Inspired by real-life experiences, imagination, and deep human emotions, he believes that stories have the power to connect people in ways ordinary conversations cannot.\n\n"
      + "Unlike most modern writers who draft their books on laptops with dozens of tabs open, Rhethim chose a simpler path: writing his book entirely on his phone, proving that creativity does not depend on expensive setups but on genuine passion and dedication. His writing style is influenced by introspective and philosophical works such as The Alchemist, blending emotion, realism, and self-discovery into his stories.\n\n"
      + "Apart from writing, he enjoys music, observing life deeply, and turning ordinary thoughts into meaningful ideas. Through his work, Rhethim hopes to create stories that stay in readers' hearts long after the final page.";

    static List<String> titles = new ArrayList<>();
    static List<List<String>> chapters = new ArrayList<>();
    static Scanner in = new Scanner(System.in, StandardCharsets.UTF_8);

    static void load(String path) throws IOException {
        String text = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
        for (String block : text.split("\n\n")) {
            block = block.trim();
            if (block.startsWith("## ")) {
                titles.add(block.substring(3));
                chapters.add(new ArrayList<>());
            } else if (!block.isEmpty() && !chapters.isEmpty()) {
                chapters.get(chapters.size() - 1).add(block);
            }
        }
    }

    static String wrap(String s, int width) {
        StringBuilder out = new StringBuilder(), line = new StringBuilder();
        for (String w : s.split("\\s+")) {
            if (line.length() + w.length() + 1 > width) { out.append(line).append("\n"); line.setLength(0); }
            if (line.length() > 0) line.append(" ");
            line.append(w);
        }
        return out.append(line).toString();
    }

    static void read() {
        int i = 0;
        while (true) {
            System.out.println("\n===== Chapter " + (i + 1) + ": " + titles.get(i) + " =====\n");
            List<String> paras = chapters.get(i);
            for (int n = 1; n <= paras.size(); n++) {
                System.out.println(wrap(paras.get(n - 1), 72) + "\n");
                if (n % 10 == 0 && n < paras.size()) {
                    System.out.print("-- Enter to continue, q to stop -- ");
                    if (in.nextLine().trim().equalsIgnoreCase("q")) break;
                }
            }
            System.out.print("[n]ext  [p]revious  [q] back  (chapter " + (i + 1) + "/" + chapters.size() + "): ");
            String cmd = in.nextLine().trim().toLowerCase();
            if (cmd.equals("n") && i < chapters.size() - 1) i++;
            else if (cmd.equals("p") && i > 0) i--;
            else if (cmd.equals("q")) return;
        }
    }

    public static void main(String[] args) throws IOException {
        load("book.txt");
        System.out.println("\nTHE LIFE WHEN SHE DISAPPEARED  |  by Rhethim Das");
        while (true) {
            System.out.println("\nKi chai tor, kya chahiye tumko beh?");
            System.out.println("  1) Book?        (first 4 chapters)");
            System.out.println("  2) Whole book?");
            System.out.println("  3) Writer MC?");
            System.out.println("  0) Exit");
            System.out.print("> ");
            String c = in.nextLine().trim();
            if (c.equals("1")) read();
            else if (c.equals("2")) {
                System.out.println("\nNahi likha maine, kyunki tum logo jaisa berozgar nahi hu mai.");
                System.out.println("Agle saal phir try karna, tab complete hogi. Ab bhago.");
            } else if (c.equals("3")) {
                System.out.println("\nRHETHIM DAS\n");
                for (String p : BIO.split("\n\n")) System.out.println(wrap(p, 72) + "\n");
            } else if (c.equals("0")) break;
        }
    }
}
