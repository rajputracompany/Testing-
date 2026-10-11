import textwrap

BIO = ("Rhethim Das is a Commerce student of Class 11 at Children's Foundation School and a young "
       "aspiring author with a passion for emotional and meaningful storytelling. Inspired by real-life "
       "experiences, imagination, and deep human emotions, he believes that stories have the power to "
       "connect people in ways ordinary conversations cannot.\n\n"
       "Unlike most modern writers who draft their books on laptops with dozens of tabs open, Rhethim "
       "chose a simpler path: writing his book entirely on his phone, proving that creativity does not "
       "depend on expensive setups but on genuine passion and dedication. His writing style is influenced "
       "by introspective and philosophical works such as The Alchemist, blending emotion, realism, and "
       "self-discovery into his stories.\n\n"
       "Apart from writing, he enjoys music, observing life deeply, and turning ordinary thoughts into "
       "meaningful ideas. Through his work, Rhethim hopes to create stories that stay in readers' hearts "
       "long after the final page.")

def load(path="book.txt"):
    chapters = []
    with open(path, encoding="utf-8") as f:
        for block in f.read().split("\n\n"):
            block = block.strip()
            if block.startswith("## "):
                chapters.append([block[3:], []])
            elif block and chapters:
                chapters[-1][1].append(block)
    return chapters

def show(text):
    for para in text.split("\n\n"):
        print(textwrap.fill(para, 72) + "\n")

def read(chapters):
    i = 0
    while True:
        title, paras = chapters[i]
        print(f"\n===== Chapter {i+1}: {title} =====\n")
        for n, p in enumerate(paras, 1):
            print(textwrap.fill(p, 72) + "\n")
            if n % 10 == 0 and n < len(paras):
                if input("-- Enter to continue, q to stop -- ").lower() == "q":
                    break
        cmd = input(f"[n]ext  [p]revious  [q] back  (chapter {i+1}/{len(chapters)}): ").lower()
        if cmd == "n" and i < len(chapters) - 1:
            i += 1
        elif cmd == "p" and i > 0:
            i -= 1
        elif cmd == "q":
            return

def main():
    chapters = load()
    print("\nTHE LIFE WHEN SHE DISAPPEARED  |  by Rhethim Das")
    while True:
        print("\nKi chai tor, kya chahiye tumko beh?")
        print("  1) Book?        (first 4 chapters)")
        print("  2) Whole book?")
        print("  3) Writer MC?")
        print("  0) Exit")
        c = input("> ").strip()
        if c == "1":
            read(chapters)
        elif c == "2":
            print("\nNahi likha maine, kyunki tum logo jaisa berozgar nahi hu mai.")
            print("Agle saal phir try karna, tab complete hogi. Ab bhago.")
        elif c == "3":
            print("\nRHETHIM DAS\n")
            show(BIO)
        elif c == "0":
            break

if __name__ == "__main__":
    main()
