import java.util.Locale;
import java.math.BigDecimal;
import java.math.RoundingMode;

// Kelas induk abstrak untuk makanan, minuman, dan diskon; tidak dibuat objek langsung.
public abstract class MenuItem {
    // Enkapsulasi: atribut private diakses melalui getter; final berarti diisi sekali.
    private final String nama;
    private final double harga;
    private final String kategori;

    // Konstruktor ini dipanggil kelas turunan melalui super(...).
    protected MenuItem(String nama, double harga, String kategori) {
        this.nama = validasiTeks(nama);
        if (!Double.isFinite(harga) || harga < 0) {
            throw new IllegalArgumentException("Harga harus berupa angka yang tidak negatif.");
        }
        // UNNECESSARY menolak harga yang perlu dibulatkan agar menjadi dua desimal.
        try {
            BigDecimal.valueOf(harga).setScale(2, RoundingMode.UNNECESSARY);
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("Harga maksimal dua angka desimal.");
        }
        this.harga = harga;
        this.kategori = kategori;
    }

    public String getNama() { return nama; }
    public double getHarga() { return harga; }
    public String getKategori() { return kategori; }

    // Tanda | dan karakter kontrol ditolak agar format baris file tidak rusak.
    static String validasiTeks(String nilai) {
        if (nilai == null || nilai.isBlank() || nilai.contains("|")
                || nilai.chars().anyMatch(Character::isISOControl)) {
            throw new IllegalArgumentException("Teks tidak boleh kosong, berisi |, atau karakter kontrol.");
        }
        return nilai.trim();
    }

    // Format Indonesia: titik untuk ribuan, koma untuk dua angka desimal.
    public static String rupiah(double nilai) {
        return String.format(Locale.forLanguageTag("id-ID"), "Rp %,.2f", nilai);
    }

    // Setiap kelas turunan wajib menyediakan cara menampilkan itemnya sendiri.
    public abstract void tampilMenu();
}
