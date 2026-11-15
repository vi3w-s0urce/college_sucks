import java.util.Locale;

// Diskon juga turunan MenuItem, dengan atribut tambahan berupa persentase promo.
public class Diskon extends MenuItem {
    private final double diskon;

    public Diskon(String nama, double diskon) {
        // Harga item diskon selalu 0 karena promo bukan barang yang dibeli.
        super(nama, 0, "Diskon");
        // Tolak NaN, nilai tak hingga, serta persentase di luar rentang (0, 100].
        if (!Double.isFinite(diskon) || diskon <= 0 || diskon > 100) {
            throw new IllegalArgumentException("Diskon harus lebih dari 0 dan maksimal 100 persen.");
        }
        this.diskon = diskon;
    }

    public double getDiskon() { return diskon; }

    // Tampilkan persentase, bukan harga; %% menghasilkan satu tanda %.
    @Override
    public void tampilMenu() {
        System.out.printf(Locale.forLanguageTag("id-ID"), "%s | %s | %.2f%%%n",
                getNama(), getKategori(), diskon);
    }
}
