// Name: Taha Anwar
// A program to convert plain text to HTML format.


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class TextToHtmlConverter {

    /**
     * Converts a plain text file to an HTML file.
     *
     * @param inputFileName  The name of the input file.
     * @throws FileNotFoundException If the input file is not found.
     */

    public static void convertToHtml(String inputFileName) throws FileNotFoundException {
        File inputFile = new File(inputFileName);

        if (!inputFile.exists()) {
            System.err.println("Input file does not exist.");
            return;
        }

        String outputFileName = getOutputFileName(inputFileName);

        try (Scanner input = new Scanner(inputFile);
             PrintWriter output = new PrintWriter(outputFileName)) {

            output.println("<html>");
            output.println("<body>");

            while (input.hasNextLine()) {
                String line = input.nextLine();
                if (line.matches("^_[^_]*_$")) { // header
                    output.println("<h1>" + line.substring(1, line.length() - 1) + "</h1>");
                } else if (line.trim().isEmpty()) { // blank line
                    output.println("<p>");
                } else if (line.matches("^-.*$")) { // unordered list item
                    output.println("<ul>");
                    while (line != null && line.matches("^-.*$")) {
                        output.println("<li>" + line.substring(1).trim() + "</li>");
                        if (input.hasNextLine()) {
                            line = input.nextLine();
                        } else {
                            line = null;
                        }
                    }
                    output.println("</ul>");
                    if (line != null) {
                        line = processHyperlinks(line);
                    }
                } else { // normal line
                    line = processHyperlinks(line);
                }
                output.println(lineBreak(line));
            }

            output.println("</body>");
            output.println("</html>");
        }
    }

    /**
     * Handles hyperlinks in the format [[X][Y]] and converts them to HTML anchor tags.
     *
     * @param line The line of text to process.
     * @return The processed line with hyperlinks converted to HTML anchor tags.
     */

    public static String processHyperlinks(String line) {
        String pattern = "\\[\\[(.*?)\\]\\[(.*?)\\]\\]";
        String replacement = "<a href=\"$1\">$2</a>";
        return line.replaceAll(pattern, replacement);
    }

    /**
     * Generates an output file name based on the input file name.
     *
     * @param inputFileName The name of the input file.
     * @return The output file name.
     */

    public static String getOutputFileName(String inputFileName) {
        int index = inputFileName.lastIndexOf('.');
        if (index == -1) {
            return inputFileName + ".html";
        } else {
            return inputFileName.substring(0, index) + ".html";
        }
    }

    public static void main(String[] args) {
        try (Scanner console = new Scanner(System.in)) {
            System.out.print("Input file: ");
            String inputFileName = console.next();
            convertToHtml(inputFileName);
            System.out.println("Conversion complete.");
        } catch (FileNotFoundException e) {
            System.err.println("Input file not found.");
        }
    }

    public static String lineBreak(String line) {
        return line + "<br />";
    }
}

