package graphics;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.NavigableMap;
import java.util.TreeMap;

public class Painter {

    NavigableMap<Integer, Color> colors = new TreeMap<>();
    NavigableMap<Integer, Color> altColors = new TreeMap<>();

    public Painter() {
        colors.put(70, Colors.BLUE);
        colors.put(80, brighterColor(Colors.BLUE, 0.35));
        colors.put(90, darkerColor(Colors.SAND, 0.085));
        colors.put(100, Colors.PERSIAN);
        colors.put(120, darkerColor(Colors.HUNTER, 0.062));
        colors.put(155, Colors.FOREST);
        colors.put(170, Colors.SACRAMENTO);
        colors.put(180, Colors.ARMY);
        colors.put(190, Colors.DARK_BROWN);
        colors.put(210, Colors.FOSSIL);
        colors.put(230, Colors.STONE);
        colors.put(256, Colors.WHITE);

        altColors.put(70, Colors.BLUE);
        altColors.put(80, brighterColor(Colors.BLUE, 0.35));
        altColors.put(90, Colors.SAND);
        altColors.put(100, Colors.PERSIAN);
        altColors.put(120, Colors.HUNTER);
        altColors.put(155, brighterColor(Colors.FOREST, 0.062));
        altColors.put(170, brighterColor(Colors.SACRAMENTO, 0.062));
        altColors.put(180, brighterColor(Colors.ARMY, 0.062));
        altColors.put(190, Colors.DARK_BROWN);
        altColors.put(210, Colors.FOSSIL);
        altColors.put(230, Colors.STONE);
        altColors.put(256, Colors.WHITE);
    }

    public BufferedImage paintMap(int[][] map, int[][] biome) {
        BufferedImage image = new BufferedImage(map[0].length, map.length, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map.length; x++) {
                image.setRGB(x, y, getPositionColor(map[y][x], biome[y][x]).getRGB());
            }
        }
        return image;
    }

    private Color getPositionColor(int mapValue, int biomeValue) {
        NavigableMap<Integer, Color> palette;
        if (isAltBiome(biomeValue)) {
            palette = altColors;
        } else {
            palette = colors;
        }
        Integer top = palette.ceilingKey(mapValue);
        if (top == null) {
            return palette.get(palette.floorKey(mapValue));
        } else {
            return palette.get(top);
        }
    }

    private boolean isAltBiome(int value) {
        return (value < -128 || (value >= 0 && value < 128));
    }

    private Color brighterColor(Color color, double factor) {
        int r = (int) ((255 - color.getRed()) * factor) + color.getRed();
        int g = (int) ((255 - color.getGreen()) * factor) + color.getGreen();
        int b = (int) ((255 - color.getBlue()) * factor) + color.getBlue();
        if (r > 255) r = 255;
        if (g > 255) g = 255;
        if (b > 255) b = 255;
        return new Color(r, g, b);
    }

    private Color darkerColor(Color color, double factor) {
        factor = 1 - factor;
        int r = (int) (color.getRed() * factor);
        int g = (int) (color.getGreen() * factor);
        int b = (int) (color.getBlue() * factor);
        if (r < 0) r = 0;
        if (g < 0) g = 0;
        if (b < 0) b = 0;
        return new Color(r, g, b);
    }
}
