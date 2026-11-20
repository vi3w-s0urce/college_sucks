import java.util.Scanner;

public class Main {
    static final int MAKS_MENU = 4;
    static final int PERSEN_PAJAK = 10;
    static final int BIAYA_PELAYANAN = 20000;
    static final int BATAS_DISKON = 100000;
    static final int BATAS_BONUS = 50000;

    static Scanner in = new Scanner(System.in);
    static Menu[] daftarMenu = new Menu[8];
    static Menu[] pesanMenu = new Menu[MAKS_MENU];
    static int[] pesanJumlah = new int[MAKS_MENU];

    // nomor 1-4 makanan, nomor 5-8 minuman
    static void isiDaftarMenu() {
        daftarMenu[0] = new Menu("Nasi Padang", 25000, "makanan");
        daftarMenu[1] = new Menu("Ayam Bakar", 30000, "makanan");
        daftarMenu[2] = new Menu("Mie Goreng", 20000, "makanan");
        daftarMenu[3] = new Menu("Soto Ayam", 22000, "makanan");
        daftarMenu[4] = new Menu("Es Teh", 5000, "minuman");
        daftarMenu[5] = new Menu("Es Jeruk", 8000, "minuman");
        daftarMenu[6] = new Menu("Kopi Susu", 15000, "minuman");
        daftarMenu[7] = new Menu("Jus Alpukat", 18000, "minuman");
    }

    static String rupiah(long angka) {
        return "Rp " + String.format("%,d", angka).replace(',', '.');
    }

    static void cetakMenu(int indeks) {
        System.out.printf("%d. %-13s %s%n", indeks + 1,
                daftarMenu[indeks].getNama(),
                rupiah(daftarMenu[indeks].getHarga()));
    }

    // menu ditampilkan per kategori
    static void tampilkanMenu() {
        System.out.println("=========== DAFTAR MENU ===========");
        System.out.println("MAKANAN");
        cetakMenu(0);
        cetakMenu(1);
        cetakMenu(2);
        cetakMenu(3);
        System.out.println("MINUMAN");
        cetakMenu(4);
        cetakMenu(5);
        cetakMenu(6);
        cetakMenu(7);
        System.out.println("===================================");
    }

    static String bacaBaris() {
        return in.hasNextLine() ? in.nextLine() : "";
    }

    static int bacaAngka(String teks) {
        try {
            return Integer.parseInt(teks.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // ponytail: periksa delapan slot tetap. Pakai perulangan jika jumlah menu dinamis.
    static Menu cariMenu(String nama) {
        if (daftarMenu[0].getNama().equalsIgnoreCase(nama)) {
            return daftarMenu[0];
        } else if (daftarMenu[1].getNama().equalsIgnoreCase(nama)) {
            return daftarMenu[1];
        } else if (daftarMenu[2].getNama().equalsIgnoreCase(nama)) {
            return daftarMenu[2];
        } else if (daftarMenu[3].getNama().equalsIgnoreCase(nama)) {
            return daftarMenu[3];
        } else if (daftarMenu[4].getNama().equalsIgnoreCase(nama)) {
            return daftarMenu[4];
        } else if (daftarMenu[5].getNama().equalsIgnoreCase(nama)) {
            return daftarMenu[5];
        } else if (daftarMenu[6].getNama().equalsIgnoreCase(nama)) {
            return daftarMenu[6];
        } else if (daftarMenu[7].getNama().equalsIgnoreCase(nama)) {
            return daftarMenu[7];
        }
        return null;
    }

    static void bacaPesanan(int urutan) {
        System.out.print("Pesanan ke-" + (urutan + 1) + " (contoh Nasi Padang = 2): ");
        String[] bagian = bacaBaris().split("=", -1);
        Menu menu = null;
        int jumlah = 0;

        if (bagian.length == 2) {
            menu = cariMenu(bagian[0].trim());
            jumlah = bacaAngka(bagian[1]);
        } else if (bagian.length == 1) {
            int nomor = bacaAngka(bagian[0]);
            if (nomor >= 1 && nomor <= daftarMenu.length) {
                menu = daftarMenu[nomor - 1];
                System.out.print("Jumlah: ");
                jumlah = bacaAngka(bacaBaris());
            }
        }

        if (menu != null && jumlah > 0) {
            pesanMenu[urutan] = menu;
            pesanJumlah[urutan] = jumlah;
        } else {
            System.out.println("Pesanan dilewati. Periksa nama atau nomor menu dan jumlah.");
        }
    }

    static long hitungTotalItem(int urutan) {
        if (pesanMenu[urutan] == null) {
            return 0;
        } else {
            return (long) pesanMenu[urutan].getHarga() * pesanJumlah[urutan];
        }
    }

    static long hitungSubtotal() {
        return hitungTotalItem(0) + hitungTotalItem(1)
                + hitungTotalItem(2) + hitungTotalItem(3);
    }

    // diskon 10% hanya jika subtotal melebihi Rp 100.000
    static long hitungDiskon(long subtotal) {
        if (subtotal > BATAS_DISKON) {
            return subtotal / 10;
        } else {
            return 0;
        }
    }

    static boolean adalahMinuman(int urutan) {
        return pesanMenu[urutan] != null
                && pesanMenu[urutan].getKategori().equals("minuman");
    }

    // urutan minuman yang pertama dipesan, -1 bila tidak ada minuman
    static int cariMinumanPertama() {
        if (adalahMinuman(0)) {
            return 0;
        } else if (adalahMinuman(1)) {
            return 1;
        } else if (adalahMinuman(2)) {
            return 2;
        } else if (adalahMinuman(3)) {
            return 3;
        } else {
            return -1;
        }
    }

    static void cetakBarisPesanan(int urutan) {
        if (pesanMenu[urutan] != null) {
            System.out.printf("%-13s %3d %10s %11s%n",
                    pesanMenu[urutan].getNama(), pesanJumlah[urutan],
                    rupiah(pesanMenu[urutan].getHarga()),
                    rupiah(hitungTotalItem(urutan)));
        }
    }

    static void cetakBaris(String label, String nilai) {
        System.out.printf("%-20s %19s%n", label, nilai);
    }

    static void cetakStruk() {
        long subtotal = hitungSubtotal();
        long pajak = subtotal * PERSEN_PAJAK / 100;
        long diskon = hitungDiskon(subtotal);
        long sebelumDiskon = subtotal + pajak + BIAYA_PELAYANAN;

        System.out.println();
        System.out.println("========================================");
        System.out.println("        RUMAH MAKAN NUSANTARA");
        System.out.println("          STRUK PEMESANAN");
        System.out.println("========================================");
        System.out.printf("%-13s %3s %10s %11s%n",
                "Item", "Qty", "Harga", "Total");
        System.out.println("----------------------------------------");
        cetakBarisPesanan(0);
        cetakBarisPesanan(1);
        cetakBarisPesanan(2);
        cetakBarisPesanan(3);
        System.out.println("----------------------------------------");
        cetakBaris("Total biaya pesanan", rupiah(subtotal));
        cetakBaris("Pajak 10%", rupiah(pajak));
        cetakBaris("Biaya pelayanan", rupiah(BIAYA_PELAYANAN));
        cetakBaris("Total sebelum diskon", rupiah(sebelumDiskon));
        System.out.println("----------------------------------------");

        // diskon 10%
        if (diskon > 0) {
            cetakBaris("Diskon 10%", "-" + rupiah(diskon));
            cetakBaris("Harga setelah diskon",
                    rupiah(sebelumDiskon - diskon));
        } else {
            cetakBaris("Diskon 10%", "Tidak ada");
        }

        // beli 1 gratis 1 (nested if)
        if (subtotal > BATAS_BONUS) {
            int urutan = cariMinumanPertama();
            if (urutan >= 0) {
                cetakBaris("Beli 1 gratis 1",
                        "Bonus 1 " + pesanMenu[urutan].getNama());
            } else {
                cetakBaris("Beli 1 gratis 1", "Tanpa minuman");
            }
        } else {
            cetakBaris("Beli 1 gratis 1", "Tidak ada");
        }

        System.out.println("----------------------------------------");
        cetakBaris("TOTAL BAYAR", rupiah(sebelumDiskon - diskon));
        System.out.println("========================================");
    }

    public static void main(String[] args) {
        isiDaftarMenu();
        tampilkanMenu();
        System.out.print("Jumlah menu yang dipesan (1-4): ");
        int jumlahMenu = bacaAngka(bacaBaris());

        // switch case: jumlah menu yang boleh hanya 1 sampai 4
        boolean valid;
        switch (jumlahMenu) {
            case 1:
            case 2:
            case 3:
            case 4:
                valid = true;
                break;
            default:
                valid = false;
        }

        if (!valid) {
            System.out.println("Jumlah menu harus 1 sampai 4.");
        } else {
            bacaPesanan(0);
            if (jumlahMenu >= 2) {
                bacaPesanan(1);
            }
            if (jumlahMenu >= 3) {
                bacaPesanan(2);
            }
            if (jumlahMenu >= 4) {
                bacaPesanan(3);
            }

            if (hitungSubtotal() == 0) {
                System.out.println("Tidak ada pesanan, struk batal.");
            } else {
                cetakStruk();
            }
        }
    }
}
