// Inheritance: Minuman memakai kelas induk yang sama dengan Makanan.
public class Minuman extends MenuItem {
    // Atribut khusus minuman, misalnya Dingin atau Hangat.
    private final String jenisMinuman;

    public Minuman(String nama, double harga, String jenisMinuman) {
        // Isi data induk, lalu validasi harga positif dan jenis minuman.
        super(nama, harga, "Minuman");
        if (harga <= 0) throw new IllegalArgumentException("Harga minuman harus lebih dari nol.");
        this.jenisMinuman = validasiTeks(jenisMinuman);
    }

    public String getJenisMinuman() { return jenisMinuman; }

    // Override menampilkan detail minuman melalui method yang sama, tampilMenu().
    @Override
    public void tampilMenu() {
        System.out.println(getNama() + " | " + getKategori() + " | "
                + jenisMinuman + " | " + rupiah(getHarga()));
    }
}
