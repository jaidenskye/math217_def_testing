import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class RandomNumberGenerator {

    public static void main(String[] args) {

        ArrayList<Integer> excludedNumbers = new ArrayList<>();

        // Read excludedNumbers from file "excluded.txt"
        try (Scanner scanner = new Scanner(new File("excluded.txt"))) {
            while (scanner.hasNextInt()) {
                excludedNumbers.add(scanner.nextInt());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ArrayList<Integer> chosenNumbers = new ArrayList<>();
        Random rand = new Random();

        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            fileWriter = new FileWriter("scrap.txt");
            bufferedWriter = new BufferedWriter(fileWriter);

            while (chosenNumbers.size() < 4) {
                int randomNum = rand.nextInt(49) + 1; // Generates a random number between 1 and 49

                if (!excludedNumbers.contains(randomNum) && !chosenNumbers.contains(randomNum)) {
                    chosenNumbers.add(randomNum);
                }
            }

            Collections.sort(chosenNumbers);

            bufferedWriter.write("Randomly chosen numbers and their corresponding entries:\n");

            for (int num : chosenNumbers) {
                System.out.print(num + " ");
                bufferedWriter.write(num + " ");

                String csvFile = "terms.csv"; // Replace this with your CSV file path
                String line;
                try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                    int lineNumber = 1;
                    while ((line = br.readLine()) != null) {
                        if (lineNumber == num) {
                            System.out.println("|  " + line);
                            bufferedWriter.write("|  " + line + ": \n" + "\n" + "\n");
                            break;
                        }
                        lineNumber++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            bufferedWriter.close();

            // Prompt the user for terms they are done with
            Scanner input = new Scanner(System.in);
            System.out.print("What terms are you done with: ");
            String userInput = input.nextLine();

            // Split the user input and add numbers to excludedNumbers list and write to excluded.txt
            String[] terms = userInput.split(" ");
            for (String term : terms) {
                excludedNumbers.add(Integer.parseInt(term));
            }

            // Write updated excludedNumbers to excluded.txt
            try (FileWriter excludedFileWriter = new FileWriter("excluded.txt")) {
                for (int number : excludedNumbers) {
                    excludedFileWriter.write(number + " ");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
                if (fileWriter != null) {
                    fileWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
