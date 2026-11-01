import java.util.Scanner;

public class Main {
    static final int KAPASITAS_MENU = 50;
    static final int MAKS_JUMLAH = 99;
    static final int MAKS_HARGA = 1000000;
    static final int PERSEN_PAJAK = 10;
    static final int PERSEN_DISKON = 10;
    static final int BIAYA_PELAYANAN = 20000;
    static final int BATAS_DISKON = 100000;
    static final int BATAS_BONUS = 50000;

    static Scanner in = new Scanner(System.in);
    static String[] kategori = {"makanan", "minuman"};
    static Menu[] daftarMenu = new Menu[KAPASITAS_MENU];
    static int jumlahMenu = 0;
    // jumlahPesan[i] = jumlah pesanan untuk daftarMenu[i]
    static int[] jumlahPesan = new int[KAPASITAS_MENU];

    static void isiDaftarMenu() {
        daftarMenu[0] = new Menu("Nasi Padang", 25000, "makanan");
        daftarMenu[1] = new Menu("Ayam Bakar", 30000, "makanan");
        daftarMenu[2] = new Menu("Mie Goreng", 20000, "makanan");
        daftarMenu[3] = new Menu("Soto Ayam", 22000, "makanan");
        daftarMenu[4] = new Menu("Es Teh", 5000, "minuman");
        daftarMenu[5] = new Menu("Es Jeruk", 8000, "minuman");
        daftarMenu[6] = new Menu("Kopi Susu", 15000, "minuman");
        daftarMenu[7] = new Menu("Jus Alpukat", 18000, "minuman");
        jumlahMenu = 8;
    }

    // ---------- bantuan input dan tampilan ----------

    static String rupiah(int angka) {
        return "Rp " + String.format("%,d", angka).replace(',', '.');
    }

    static String bacaTeks(String pesan) {
        System.out.print(pesan);
        return in.nextLine();
    }

    // mengubah teks menjadi angka, hasilnya -1 bila teks bukan angka
    static int keAngka(String teks) {
        if (teks.length() == 0 || teks.length() > 7) {
            return -1;
        }
        for (int i = 0; i < teks.length(); i++) {
            if (!Character.isDigit(teks.charAt(i))) {
                return -1;
            }
        }
        return Integer.parseInt(teks);
    }

    // terus meminta input sampai angka berada di antara min dan maks
    static int bacaAngka(String pesan, int min, int maks) {
        int angka = keAngka(bacaTeks(pesan));
        while (angka < min || angka > maks) {
            System.out.println("Input tidak valid, masukkan angka "
                    + min + " sampai " + maks + ".");
            angka = keAngka(bacaTeks(pesan));
        }
        return angka;
    }

    // true bila dijawab Ya, false bila Tidak, selain itu diminta lagi
    static boolean konfirmasi(String pesan) {
        String jawab = bacaTeks(pesan + " (Ya/Tidak): ");
        while (!jawab.equalsIgnoreCase("Ya") && !jawab.equalsIgnoreCase("Tidak")) {
            System.out.println("Ketik Ya atau Tidak.");
            jawab = bacaTeks(pesan + " (Ya/Tidak): ");
        }
        return jawab.equalsIgnoreCase("Ya");
    }

    // menu dikelompokkan per kategori: for-each untuk kategori, for untuk menu
    static void tampilkanMenu() {
        System.out.println("=========== DAFTAR MENU ===========");
        for (String jenis : kategori) {
            System.out.println(jenis.toUpperCase());
            for (int i = 0; i < jumlahMenu; i++) {
                if (daftarMenu[i].getKategori().equals(jenis)) {
                    System.out.printf("%2d. %-16s %s%n", i + 1,
                            daftarMenu[i].getNama(),
                            rupiah(daftarMenu[i].getHarga()));
                }
            }
        }
        System.out.println("===================================");
    }

    // ---------- menu pelanggan ----------

    // menerima pesanan sampai pelanggan mengetik 'selesai'
    static void terimaPesanan() {
        System.out.println("Ketik nomor menu untuk memesan,"
                + " 'hapus' untuk menghapus pesanan, 'selesai' untuk mengakhiri.");
        String input = bacaTeks("Nomor menu: ");
        while (!input.equalsIgnoreCase("selesai")) {
            if (input.equalsIgnoreCase("hapus")) {
                hapusPesanan();
            } else {
                int nomor = keAngka(input);
                if (nomor >= 1 && nomor <= jumlahMenu) {
                    Menu m = daftarMenu[nomor - 1];
                    int jumlah = bacaAngka("Jumlah " + m.getNama() + ": ",
                            1, MAKS_JUMLAH);
                    jumlahPesan[nomor - 1] = jumlahPesan[nomor - 1] + jumlah;
                    System.out.println("Dipesan: " + jumlah + " x " + m.getNama());
                } else {
                    System.out.println("Menu tidak ada, coba lagi.");
                }
            }
            input = bacaTeks("Nomor menu (atau 'selesai'): ");
        }
    }

    static void hapusPesanan() {
        int nomor = bacaAngka("Nomor menu yang dihapus dari pesanan: ", 1, jumlahMenu);
        if (jumlahPesan[nomor - 1] == 0) {
            System.out.println("Menu itu belum dipesan.");
        } else {
            jumlahPesan[nomor - 1] = 0;
            System.out.println(daftarMenu[nomor - 1].getNama() + " dihapus dari pesanan.");
        }
    }

    static int hitungSubtotal() {
        int total = 0;
        for (int i = 0; i < jumlahMenu; i++) {
            total = total + daftarMenu[i].getHarga() * jumlahPesan[i];
        }
        return total;
    }

    // diskon 10% hanya jika subtotal melebihi Rp 100.000
    static int hitungDiskon(int subtotal) {
        if (subtotal > BATAS_DISKON) {
            return subtotal * PERSEN_DISKON / 100;
        } else {
            return 0;
        }
    }

    // nomor urut minuman pertama (menurut daftar menu) yang dipesan, -1 bila tidak ada
    static int cariMinumanBonus() {
        for (int i = 0; i < jumlahMenu; i++) {
            if (jumlahPesan[i] > 0 && daftarMenu[i].getKategori().equals("minuman")) {
                return i;
            }
        }
        return -1;
    }

    static void cetakBaris(String label, String nilai) {
        System.out.printf("%-22s %20s%n", label, nilai);
    }

    static void cetakStruk() {
        int subtotal = hitungSubtotal();
        int pajak = subtotal * PERSEN_PAJAK / 100;
        int diskon = hitungDiskon(subtotal);
        int sebelumDiskon = subtotal + pajak + BIAYA_PELAYANAN;

        System.out.println();
        System.out.println("===========================================");
        System.out.println("            RUMAH MAKAN NUSANTARA");
        System.out.println("              STRUK PEMESANAN");
        System.out.println("===========================================");
        System.out.printf("%-16s %3s %10s %11s%n", "Item", "Qty", "Harga", "Total");
        System.out.println("-------------------------------------------");
        for (int i = 0; i < jumlahMenu; i++) {
            if (jumlahPesan[i] > 0) {
                int totalItem = daftarMenu[i].getHarga() * jumlahPesan[i];
                System.out.printf("%-16s %3d %10s %11s%n",
                        daftarMenu[i].getNama(), jumlahPesan[i],
                        rupiah(daftarMenu[i].getHarga()), rupiah(totalItem));
            }
        }
        System.out.println("-------------------------------------------");
        cetakBaris("Total biaya pesanan", rupiah(subtotal));
        cetakBaris("Pajak 10%", rupiah(pajak));
        cetakBaris("Biaya pelayanan", rupiah(BIAYA_PELAYANAN));
        cetakBaris("Total sebelum diskon", rupiah(sebelumDiskon));
        System.out.println("-------------------------------------------");

        // diskon 10%
        if (diskon > 0) {
            cetakBaris("Diskon 10%", "-" + rupiah(diskon));
            cetakBaris("Harga setelah diskon", rupiah(sebelumDiskon - diskon));
        } else {
            cetakBaris("Diskon 10%", "Tidak ada");
        }

        // beli 1 gratis 1 untuk minuman (nested if)
        if (subtotal > BATAS_BONUS) {
            int bonus = cariMinumanBonus();
            if (bonus >= 0) {
                cetakBaris("Beli 1 gratis 1", "Bonus 1 porsi");
                cetakBaris("Minuman gratis", daftarMenu[bonus].getNama());
            } else {
                cetakBaris("Beli 1 gratis 1", "Tanpa minuman");
            }
        } else {
            cetakBaris("Beli 1 gratis 1", "Tidak ada");
        }

        System.out.println("-------------------------------------------");
        cetakBaris("TOTAL BAYAR", rupiah(sebelumDiskon - diskon));
        System.out.println("===========================================");
    }

    static void menuPelanggan() {
        if (jumlahMenu == 0) {
            System.out.println("Menu belum tersedia.");
            return;
        }
        // pesanan baru: semua jumlah dimulai dari 0
        for (int i = 0; i < jumlahPesan.length; i++) {
            jumlahPesan[i] = 0;
        }
        tampilkanMenu();
        terimaPesanan();
        if (hitungSubtotal() == 0) {
            System.out.println("Tidak ada pesanan, struk batal.");
        } else {
            cetakStruk();
        }
    }

    // ---------- menu pemilik ----------

    // boleh menambah beberapa menu sekaligus (do-while)
    static void tambahMenu() {
        boolean lagi;
        do {
            if (jumlahMenu == KAPASITAS_MENU) {
                System.out.println("Daftar menu penuh.");
                return;
            }
            String nama = bacaTeks("Nama menu baru (maks. 16 huruf): ");
            while (nama.length() == 0 || nama.length() > 16) {
                System.out.println("Nama harus 1 sampai 16 huruf.");
                nama = bacaTeks("Nama menu baru (maks. 16 huruf): ");
            }
            int jenis = bacaAngka("Kategori (1 = makanan, 2 = minuman): ", 1, 2);
            int harga = bacaAngka("Harga (Rp): ", 1, MAKS_HARGA);
            daftarMenu[jumlahMenu] = new Menu(nama, harga, kategori[jenis - 1]);
            jumlahMenu++;
            System.out.println("Menu " + nama + " ditambahkan.");
            lagi = konfirmasi("Tambah menu lain?");
        } while (lagi);
        tampilkanMenu();
    }

    static void ubahHarga() {
        if (jumlahMenu == 0) {
            System.out.println("Daftar menu kosong.");
            return;
        }
        tampilkanMenu();
        int nomor = bacaAngka("Nomor menu yang diubah: ", 1, jumlahMenu);
        Menu m = daftarMenu[nomor - 1];
        int hargaBaru = bacaAngka("Harga baru " + m.getNama()
                + " (sekarang " + rupiah(m.getHarga()) + "): ", 1, MAKS_HARGA);
        if (konfirmasi("Yakin ubah harga " + m.getNama() + " menjadi "
                + rupiah(hargaBaru) + "?")) {
            m.setHarga(hargaBaru);
            System.out.println("Harga berhasil diubah.");
            tampilkanMenu();
        } else {
            System.out.println("Perubahan dibatalkan.");
        }
    }

    static void hapusMenu() {
        if (jumlahMenu == 0) {
            System.out.println("Daftar menu kosong.");
            return;
        }
        tampilkanMenu();
        int nomor = bacaAngka("Nomor menu yang dihapus: ", 1, jumlahMenu);
        String nama = daftarMenu[nomor - 1].getNama();
        if (konfirmasi("Yakin hapus menu " + nama + "?")) {
            // geser menu di belakangnya satu tempat ke depan
            for (int i = nomor - 1; i < jumlahMenu - 1; i++) {
                daftarMenu[i] = daftarMenu[i + 1];
            }
            jumlahMenu--;
            daftarMenu[jumlahMenu] = null;
            System.out.println("Menu " + nama + " dihapus.");
            tampilkanMenu();
        } else {
            System.out.println("Penghapusan dibatalkan.");
        }
    }

    static void menuPemilik() {
        int pilihan = -1;
        while (pilihan != 0) {
            System.out.println();
            System.out.println("---- MENU PEMILIK ----");
            System.out.println("1. Tambah menu baru");
            System.out.println("2. Ubah harga menu");
            System.out.println("3. Hapus menu");
            System.out.println("4. Lihat daftar menu");
            System.out.println("0. Kembali ke menu utama");
            pilihan = bacaAngka("Pilih: ", 0, 4);
            if (pilihan == 1) {
                tambahMenu();
            } else if (pilihan == 2) {
                ubahHarga();
            } else if (pilihan == 3) {
                hapusMenu();
            } else if (pilihan == 4) {
                tampilkanMenu();
            }
        }
    }

    // ---------- program utama ----------

    public static void main(String[] args) {
        isiDaftarMenu();
        int pilihan;
        do {
            System.out.println();
            System.out.println("===== APLIKASI RESTORAN =====");
            System.out.println("1. Menu Pelanggan (memesan)");
            System.out.println("2. Menu Pemilik (kelola menu)");
            System.out.println("0. Keluar");
            pilihan = bacaAngka("Pilih: ", 0, 2);
            switch (pilihan) {
                case 1:
                    menuPelanggan();
                    break;
                case 2:
                    menuPemilik();
                    break;
                default:
                    System.out.println("Terima kasih.");
            }
        } while (pilihan != 0);
    }
}
