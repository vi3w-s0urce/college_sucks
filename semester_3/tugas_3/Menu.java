import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Menu {
    // Satu ArrayList<MenuItem> menampung ketiga jenis item dan bisa bertambah ukurannya.
    private final ArrayList<MenuItem> daftarMenu = new ArrayList<>();

    // Item ditambahkan ke akhir daftar setelah dipastikan bukan null.
    public void tambah(MenuItem item) {
        if (item == null) throw new IllegalArgumentException("Item tidak boleh kosong.");
        daftarMenu.add(item);
    }

    public int jumlah() { return daftarMenu.size(); }

    public MenuItem ambil(int nomor) {
        if (nomor < 1 || nomor > daftarMenu.size()) {
            throw new IndexOutOfBoundsException("Nomor " + nomor + " tidak ada dalam menu.");
        }
        // Nomor yang dilihat pengguna mulai dari 1, indeks ArrayList mulai dari 0.
        return daftarMenu.get(nomor - 1);
    }

    public void tampilkan() {
        if (daftarMenu.isEmpty()) System.out.println("Menu masih kosong.");
        for (int i = 0; i < daftarMenu.size(); i++) {
            System.out.print((i + 1) + ". ");
            // Polymorphism: method yang berjalan sesuai objek Makanan, Minuman, atau Diskon.
            daftarMenu.get(i).tampilMenu();
        }
    }

    // Satu baris file: kategori|nama|harga|detail; detail berisi jenis atau persen diskon.
    public void simpan(Path file) throws IOException {
        if (daftarMenu.isEmpty()) throw new IllegalStateException("Menu kosong tidak disimpan.");
        StringBuilder teks = new StringBuilder();
        for (MenuItem item : daftarMenu) {
            String detail;
            // instanceof menentukan jenis objek sebelum cast untuk membaca atribut khusus.
            if (item instanceof Makanan) detail = ((Makanan) item).getJenisMakanan();
            else if (item instanceof Minuman) detail = ((Minuman) item).getJenisMinuman();
            else detail = Double.toString(((Diskon) item).getDiskon());
            teks.append(item.getKategori()).append('|').append(item.getNama()).append('|')
                    .append(item.getHarga()).append('|').append(detail).append('\n');
        }
        BerkasTeks.simpan(file, teks.toString());
    }

    public void muat(Path file) throws IOException {
        // Baca ke daftar sementara; daftar aktif baru diganti setelah semua baris valid.
        ArrayList<MenuItem> hasil = new ArrayList<>();
        int nomorBaris = 0;
        for (String baris : Files.readAllLines(file, StandardCharsets.UTF_8)) {
            nomorBaris++;
            try {
                // \\| berarti pemisah | literal; -1 mempertahankan kolom kosong di akhir.
                String[] kolom = baris.split("\\|", -1);
                if (kolom.length != 4) throw new IllegalArgumentException("Harus ada 4 kolom.");
                double harga = Double.parseDouble(kolom[2]);
                // Kategori pada file menentukan kelas objek yang dibuat kembali.
                switch (kolom[0]) {
                    case "Makanan": hasil.add(new Makanan(kolom[1], harga, kolom[3])); break;
                    case "Minuman": hasil.add(new Minuman(kolom[1], harga, kolom[3])); break;
                    case "Diskon":
                        if (harga != 0) throw new IllegalArgumentException("Harga item diskon harus 0.");
                        hasil.add(new Diskon(kolom[1], Double.parseDouble(kolom[3]))); break;
                    default: throw new IllegalArgumentException("Kategori tidak dikenal.");
                }
            } catch (IllegalArgumentException e) {
                // Sertakan nomor baris agar penyebab file gagal dimuat mudah ditemukan.
                throw new IOException("Baris " + nomorBaris + " tidak valid: " + e.getMessage(), e);
            }
        }
        if (hasil.isEmpty()) throw new IOException("File menu kosong.");
        // Menu aktif dipertahankan jika ada satu saja baris yang tidak valid.
        daftarMenu.clear();
        daftarMenu.addAll(hasil);
    }
}
