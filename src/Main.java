import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String rawData = scanner.nextLine();

        RomeNumber romeNumber = new RomeNumber(rawData);
        romeNumber.printRomeNumber(romeNumber);
    }
}
