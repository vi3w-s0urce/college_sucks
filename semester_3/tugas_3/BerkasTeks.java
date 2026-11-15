import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

// Dipakai bersama oleh Menu.simpan() dan Pesanan.simpanStruk().
public class BerkasTeks {
    public static void simpan(Path tujuan, String isi) throws IOException {
        Path target = tujuan.toAbsolutePath();
        // File sementara dibuat di folder tujuan agar bisa dipindahkan secara atomik.
        Path sementara = Files.createTempFile(target.getParent(), ".restoran-", ".tmp");
        try {
            // File lama diganti setelah seluruh teks berhasil ditulis.
            Files.writeString(sementara, isi, StandardCharsets.UTF_8);
            try {
                // ATOMIC_MOVE mengganti file dalam satu operasi jika didukung sistem file.
                Files.move(sementara, target, StandardCopyOption.ATOMIC_MOVE,
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException e) {
                // Gunakan penggantian biasa jika perpindahan atomik tidak didukung.
                Files.move(sementara, target, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            // Bersihkan file sementara bila masih ada, baik saat berhasil maupun gagal.
            Files.deleteIfExists(sementara);
        }
    }
}
