import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    // Scanner membaca input; menu menyimpan daftar; pesanan menyimpan transaksi aktif.
    private static final Scanner input = new Scanner(System.in);
    private static final Menu menu = new Menu();
    private static Pesanan pesanan = new Pesanan();

    // ---------- bantuan input ----------

    // Baca satu baris dan buang spasi di awal/akhir sebelum diproses.
    private static String bacaTeks(String pesan) {
        System.out.print(pesan);
        return input.nextLine().trim();
    }

    // Ulangi input sampai berupa bilangan bulat dalam batas min sampai maks.
    private static int bacaAngka(String pesan, int min, int maks) {
        while (true) {
            try {
                int angka = Integer.parseInt(bacaTeks(pesan));
                if (angka >= min && angka <= maks) return angka;
            } catch (NumberFormatException e) {
                System.out.println("Masukan harus berupa bilangan bulat.");
                continue;
            }
            System.out.println("Masukkan angka " + min + " sampai " + maks + ".");
        }
    }

    // Terima angka desimal positif; NaN dan nilai tak hingga ditolak.
    private static double bacaDesimal(String pesan) {
        while (true) {
            try {
                double nilai = Double.parseDouble(bacaTeks(pesan));
                if (Double.isFinite(nilai) && nilai > 0) return nilai;
            } catch (NumberFormatException e) {
                System.out.println("Gunakan angka tanpa pemisah ribuan, contoh 25000 atau 12.5.");
                continue;
            }
            System.out.println("Nilai harus berupa angka positif yang terbatas.");
        }
    }

    // ---------- menambah item restoran ----------

    private static void tambahItem() {
        int jenis = bacaAngka("Jenis (1 Makanan, 2 Minuman, 3 Diskon): ", 1, 3);
        String nama = bacaTeks("Nama: ");
        // Referensi induk dapat menunjuk objek dari salah satu dari tiga kelas turunan.
        MenuItem item;
        if (jenis == 3) {
            item = new Diskon(nama, bacaDesimal("Persentase diskon (contoh 10): "));
        } else {
            double harga = bacaDesimal("Harga (contoh 25000): ");
            String detail = bacaTeks("Jenis makanan/minuman (contoh Nasi atau Dingin): ");
            if (jenis == 1) item = new Makanan(nama, harga, detail);
            else item = new Minuman(nama, harga, detail);
        }
        menu.tambah(item);
        System.out.println("Item ditambahkan: " + item.getNama());
    }

    // ---------- membuat pesanan ----------

    private static void buatPesanan() {
        if (menu.jumlah() == 0) throw new IllegalStateException("Menu masih kosong.");
        // Isi pesanan sementara agar pesanan aktif tidak langsung tertimpa.
        Pesanan baru = new Pesanan();
        menu.tampilkan();
        // Pilih makanan/minuman dan jumlahnya berulang kali; 0 mengakhiri pemilihan.
        while (true) {
            int nomor = bacaAngka("Nomor makanan/minuman (0 selesai): ", 0, Integer.MAX_VALUE);
            if (nomor == 0) break;
            try {
                MenuItem item = menu.ambil(nomor);
                if (item instanceof Diskon) throw new IllegalArgumentException("Diskon bukan makanan/minuman.");
                int jumlah = bacaAngka("Jumlah porsi (1-99): ", 1, 99);
                baru.tambah(item, jumlah);
                System.out.println("Dicatat: " + item.getNama() + " x " + jumlah);
            } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
                // Nomor atau jenis salah ditolak tanpa menghentikan pemesanan.
                System.out.println("Pesanan ditolak: " + e.getMessage());
            }
        }
        // Membatalkan pesanan kosong mempertahankan pesanan aktif sebelumnya.
        if (baru.kosong()) {
            System.out.println("Pesanan kosong dibatalkan; pesanan sebelumnya tetap tersimpan di memori.");
            return;
        }
        // Pilih satu promo setelah item selesai dipesan; 0 berarti tanpa promo.
        while (true) {
            int nomor = bacaAngka("Nomor promo diskon (0 tanpa promo): ", 0, Integer.MAX_VALUE);
            if (nomor == 0) break;
            try {
                MenuItem item = menu.ambil(nomor);
                if (!(item instanceof Diskon)) throw new IllegalArgumentException("Nomor itu bukan promo diskon.");
                // Cast aman karena jenis objek sudah diperiksa dengan instanceof.
                baru.setDiskon((Diskon) item);
                break;
            } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
                System.out.println("Promo ditolak: " + e.getMessage());
            }
        }
        // Pesanan aktif baru diganti setelah seluruh proses pemesanan selesai.
        pesanan = baru;
        System.out.println("Pesanan baru tercatat. Pilih 4 untuk melihat struk.");
    }

    // ---------- program utama ----------

    public static void main(String[] args) {
        try {
            // Saat mulai selalu baca data awal; menu.txt dimuat lewat pilihan 8.
            menu.muat(Path.of("menu_awal.txt"));
            System.out.println("Menu awal dimuat: " + menu.jumlah() + " item.");
        } catch (IOException e) {
            System.out.println("Menu awal gagal dimuat: " + e.getMessage());
        }
        // do-while menampilkan menu minimal sekali; switch menjalankan pilihan pengguna.
        int pilihan;
        do {
            System.out.println("\n=== MANAJEMEN RESTORAN T3 ===");
            System.out.println("1. Tambah item (makanan/minuman/diskon)");
            System.out.println("2. Tampilkan menu restoran");
            System.out.println("3. Buat pesanan baru (ganti pesanan aktif setelah selesai)");
            System.out.println("4. Hitung total dan tampilkan struk");
            System.out.println("5. Simpan/ganti struk.txt");
            System.out.println("6. Muat dan tampilkan struk.txt");
            System.out.println("7. Simpan/ganti menu.txt");
            System.out.println("8. Muat menu.txt (ganti daftar menu aktif)");
            System.out.println("0. Keluar");
            pilihan = bacaAngka("Pilihan: ", 0, 8);
            // Tangani kegagalan file/data agar menu utama tetap bisa digunakan.
            try {
                switch (pilihan) {
                    case 1: tambahItem(); break;
                    case 2: menu.tampilkan(); break;
                    case 3: buatPesanan(); break;
                    case 4: System.out.print(pesanan.buatStruk()); break;
                    case 5:
                        pesanan.simpanStruk(Path.of("struk.txt"));
                        System.out.println("Struk disimpan ke struk.txt."); break;
                    case 6: System.out.print(Pesanan.muatStruk(Path.of("struk.txt"))); break;
                    case 7:
                        menu.simpan(Path.of("menu.txt"));
                        System.out.println("Menu disimpan ke menu.txt."); break;
                    case 8:
                        menu.muat(Path.of("menu.txt"));
                        System.out.println("Menu dimuat: " + menu.jumlah() + " item."); break;
                    default: System.out.println("Terima kasih. Menu baru tersimpan hanya jika memilih 7.");
                }
            } catch (IOException e) {
                System.out.println("Operasi file gagal: " + e.getMessage());
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Operasi ditolak: " + e.getMessage());
            }
        } while (pilihan != 0);
    }
}
