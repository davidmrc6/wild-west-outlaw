package wildwestoutlaw;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

// Loads each image once and keeps it, so nothing is read from disk twice
public final class Assets {

    private static final Map<String, BufferedImage> images = new ConcurrentHashMap<>();

    private Assets() {
    }

    public static BufferedImage image(String path) {
        return images.computeIfAbsent(path, key -> read(path));
    }

    // Scaled to size x size with sharp (nearest neighbour) pixels
    public static BufferedImage image(String path, int size) {
        return images.computeIfAbsent(path + "@" + size, key -> scale(read(path), size));
    }

    // Numbered frames, e.g. frames("/mobs/cactus/left %d.png", 10, 64) loads "left 1.png" to "left 10.png"
    public static BufferedImage[] frames(String pattern, int count, int size) {
        BufferedImage[] frames = new BufferedImage[count];
        for (int i = 0; i < count; i++) {
            frames[i] = image(String.format(pattern, i + 1), size);
        }
        return frames;
    }

    public static Font font(String path) {
        try (InputStream in = open(path)) {
            return Font.createFont(Font.TRUETYPE_FONT, in);
        } catch (Exception e) {
            throw new IllegalStateException("Could not load font " + path, e);
        }
    }

    private static BufferedImage read(String path) {
        try (InputStream in = open(path)) {
            BufferedImage image = ImageIO.read(in);
            if (image == null) {
                throw new IllegalStateException("Not an image: " + path);
            }
            return image;
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read " + path, e);
        }
    }

    private static InputStream open(String path) {
        InputStream in = Assets.class.getResourceAsStream(path);
        if (in == null) {
            throw new IllegalStateException("Missing game file: " + path);
        }
        return in;
    }

    private static BufferedImage scale(BufferedImage original, int size) {
        BufferedImage scaled = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = scaled.createGraphics();
        g2.drawImage(original, 0, 0, size, size, null);
        g2.dispose();
        return scaled;
    }
}
