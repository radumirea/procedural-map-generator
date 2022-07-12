import generator.TerrainGenerator;
import graphics.Painter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws IOException {
        Random r;
        int dimensionExponent = 11;
        try {
            if (args.length > 0) {
                dimensionExponent = Integer.parseInt(args[0]);
            }
            if (args.length > 1) {
                r = new Random(Long.parseLong(args[1]));
            } else {
                r = new Random();
            }
        }catch (NumberFormatException e){
            System.out.println("Invalid input");
            return;
        }
        int bound = r.nextInt(200) + 55;
        TerrainGenerator terrainGenerator = new TerrainGenerator(r.nextLong());
        int[] seedMap = {r.nextInt(255), r.nextInt(255), r.nextInt(255), r.nextInt(255)};
        int[] seedBiome = {r.nextInt(255 * 2) - 255, r.nextInt(255 * 2) - 255, r.nextInt(255 * 2) - 255, r.nextInt(255 * 2) - 255};
        int[][] map = terrainGenerator.compute(dimensionExponent, bound, 0.7, seedMap);
        int[][] biome = terrainGenerator.compute(dimensionExponent, bound, 0.68, seedBiome);
        terrainGenerator.normaliseForGrayscale(map);
        Painter painter = new Painter();
        BufferedImage image = painter.paintMap(map, biome);
        File output = new File(new Date().getTime() + ".png");
        ImageIO.write(image, "png", output);
    }
}
