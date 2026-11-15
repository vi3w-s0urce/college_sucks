// Inheritance: Makanan mewarisi data dan method umum dari MenuItem.
public class Makanan extends MenuItem {
    // Atribut khusus makanan, misalnya Nasi atau Lauk.
    private final String jenisMakanan;

    public Makanan(String nama, double harga, String jenisMakanan) {
        // Isi data induk, lalu validasi harga positif dan jenis makanan.
        super(nama, harga, "Makanan");
        if (harga <= 0) throw new IllegalArgumentException("Harga makanan harus lebih dari nol.");
        this.jenisMakanan = validasiTeks(jenisMakanan);
    }

    public String getJenisMakanan() { return jenisMakanan; }

    // Override memberi isi pada method abstrak tampilMenu() milik induk.
    @Override
    public void tampilMenu() {
        System.out.println(getNama() + " | " + getKategori() + " | "
                + jenisMakanan + " | " + rupiah(getHarga()));
    }
}
