import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Pesanan {
    private final ArrayList<MenuItem> itemPesanan = new ArrayList<>();
    // Satu pesanan menyimpan satu promo; null berarti tanpa diskon.
    private Diskon promo;

    public boolean kosong() { return itemPesanan.isEmpty(); }

    // Hanya makanan/minuman yang boleh dibeli, dengan jumlah 1 sampai 99 per input.
    public void tambah(MenuItem item, int jumlah) {
        if (!(item instanceof Makanan) && !(item instanceof Minuman)) {
            throw new IllegalArgumentException("Pilih makanan/minuman; diskon dipilih setelah pesanan selesai.");
        }
        if (jumlah < 1 || jumlah > 99) throw new IllegalArgumentException("Jumlah harus 1 sampai 99.");
        double tambahan = item.getHarga() * jumlah;
        if (!Double.isFinite(hitungSubtotal() + tambahan)) {
            throw new IllegalArgumentException("Nilai pesanan terlalu besar.");
        }
        // Satu elemen mewakili satu porsi, sehingga jumlah tidak perlu disimpan terpisah.
        for (int i = 0; i < jumlah; i++) itemPesanan.add(item);
    }

    // Promo baru mengganti promo sebelumnya, bukan menumpuk potongan.
    public void setDiskon(Diskon promo) { this.promo = promo; }

    // BigDecimal menjumlahkan harga desimal; hasil tetap double sesuai tipe harga pada soal.
    public double hitungSubtotal() {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (MenuItem item : itemPesanan) subtotal = subtotal.add(BigDecimal.valueOf(item.getHarga()));
        return subtotal.doubleValue();
    }

    public double hitungPotongan() {
        double persen = promo == null ? 0 : promo.getDiskon();
        // Subtotal x persen / 100, dibulatkan dua desimal dengan aturan HALF_UP.
        return BigDecimal.valueOf(hitungSubtotal()).multiply(BigDecimal.valueOf(persen))
                .movePointLeft(2).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    // Total hanya subtotal dikurangi potongan; T3 tidak memakai pajak/layanan dari T2.
    public double hitungTotal() {
        return BigDecimal.valueOf(hitungSubtotal()).subtract(BigDecimal.valueOf(hitungPotongan()))
                .setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    // StringBuilder menyusun teks struk; satu baris item mewakili satu porsi.
    public String buatStruk() {
        if (kosong()) throw new IllegalStateException("Belum ada pesanan.");
        StringBuilder teks = new StringBuilder("=== STRUK RESTORAN ===\n");
        for (int i = 0; i < itemPesanan.size(); i++) {
            MenuItem item = itemPesanan.get(i);
            teks.append(i + 1).append(". ").append(item.getNama()).append(" | ")
                    .append(MenuItem.rupiah(item.getHarga())).append('\n');
        }
        teks.append("Subtotal: ").append(MenuItem.rupiah(hitungSubtotal())).append('\n');
        teks.append("Promo: ").append(promo == null ? "Tidak ada" : promo.getNama()).append('\n');
        teks.append("Potongan: ").append(MenuItem.rupiah(hitungPotongan())).append('\n');
        teks.append("TOTAL BAYAR: ").append(MenuItem.rupiah(hitungTotal())).append('\n');
        return teks.toString();
    }

    // buatStruk() memeriksa pesanan sebelum file tujuan ditulis atau diganti.
    public void simpanStruk(Path file) throws IOException {
        BerkasTeks.simpan(file, buatStruk());
    }

    // Baca struk sebagai teks untuk ditampilkan, tanpa mengganti objek pesanan aktif.
    public static String muatStruk(Path file) throws IOException {
        return Files.readString(file, StandardCharsets.UTF_8);
    }
}
