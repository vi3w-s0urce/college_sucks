import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Scanner;

public class tugas_3 {
    public static void main(String[] args) {

        // Inisialisasi Scanner dan DecimalFormat
        Scanner input = new Scanner(System.in);
        Locale localeID = Locale.of("id", "ID");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(localeID);
        DecimalFormat df = new DecimalFormat("#,##0.00", symbols);

        // Array gaji pokok sesuai indeks:
        // 0 = Golongan A, 1 = Golongan B, 2 = Golongan C
        int[] gajiPokok = {5000000, 6500000, 9500000};

        // Array persentase lembur
        int[] persenLembur = {30, 32, 34, 36, 38};

        System.out.print("Masukkan Golongan (A/B/C): ");
        String golongan = input.next().toUpperCase();

        System.out.print("Masukkan Jam Lembur: ");
        int jamLembur = input.nextInt();

        int indexGaji = -1;

        // Tentukan indeks array berdasarkan golongan
        if (golongan.equals("A")) {
            indexGaji = 0;
        } else if (golongan.equals("B")) {
            indexGaji = 1;
        } else if (golongan.equals("C")) {
            indexGaji = 2;
        } else {
            System.out.println("Golongan tidak valid!");
            return;
        }

        // Ambil gaji pokok dari array
        int gaji = gajiPokok[indexGaji];

        // Tentukan persentase lembur berdasarkan jam lembur
        int persen;
        if (jamLembur == 1) {
            persen = persenLembur[0];
        } else if (jamLembur == 2) {
            persen = persenLembur[1];
        } else if (jamLembur == 3) {
            persen = persenLembur[2];
        } else if (jamLembur == 4) {
            persen = persenLembur[3];
        } else if (jamLembur >= 5) {
            persen = persenLembur[4];
        } else {
            persen = 0; // Tidak lembur
        }

        // Hitung gaji lembur
        double gajiLembur = gaji * (persen / 100.0);

        // Total penghasilan
        double total = gaji + gajiLembur;

        // Output hasil
        System.out.println("============================");
        System.out.println("Golongan        : " + golongan);
        System.out.println("Gaji Pokok      : Rp " + df.format(gaji));
        System.out.println("Jam Lembur      : " + jamLembur);
        System.out.println("Persentase      : " + persen + "%");
        System.out.println("Gaji Lembur     : Rp " + df.format((int)gajiLembur));
        System.out.println("Total Gaji      : Rp " + df.format((int)total));
        System.out.println("============================");
    }
}
