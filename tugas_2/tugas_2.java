import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Scanner;

public class tugas_2 {

    public static void main(String[] args) {

        // Inisialisasi Scanner dan DecimalFormat
        Scanner input = new Scanner(System.in);
        Locale localeID = Locale.of("id", "ID");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(localeID);
        DecimalFormat df = new DecimalFormat("#,##0.00", symbols);

        // Deklarasi variable
        String golongan;
        int jamLembur;
        double gajiPokok = 0, gajiLembur = 0;

        // Input
        System.out.print("Masukkan Golongan (A/B/C): ");
        golongan = input.nextLine().toUpperCase();

        System.out.print("Masukkan Jam Lembur: ");
        jamLembur = input.nextInt();

        // Menentukan gaji pokok berdasarkan golongan
        switch (golongan) {
            case "A" ->
                gajiPokok = 5000000;
            case "B" ->
                gajiPokok = 6500000;
            case "C" ->
                gajiPokok = 9500000;
            default -> {
                System.out.println("Golongan tidak valid.");
                System.exit(0);
            }
        }

        // Menentukan gaji lembur berdasarkan jam
        if (jamLembur == 1) {
            gajiLembur = 0.30 * gajiPokok;
        } else if (jamLembur == 2) {
            gajiLembur = 0.32 * gajiPokok;
        } else if (jamLembur == 3) {
            gajiLembur = 0.34 * gajiPokok;
        } else if (jamLembur == 4) {
            gajiLembur = 0.36 * gajiPokok;
        } else if (jamLembur >= 5) {
            gajiLembur = 0.38 * gajiPokok;
        } else {
            gajiLembur = 0;
        }

        // Total Gaji
        double totalGaji = gajiPokok + gajiLembur;

        // Output
        System.out.println("=====================================");
        System.out.println("Gaji Pokok : Rp " + df.format(gajiPokok));
        System.out.println("Gaji Lembur: Rp " + df.format(gajiLembur));
        System.out.println("Total Gaji : Rp " + df.format(totalGaji));
        System.out.println("=====================================");
    }
}
