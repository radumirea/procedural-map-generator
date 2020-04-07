package generator;

import java.security.InvalidParameterException;
import java.util.Random;

public class TerrainGenerator {

    private final Random random;

    public TerrainGenerator(long seed) {
        random = new Random(seed);
    }

    public TerrainGenerator() {
        random = new Random();
    }

    private void squareStep(int[][] map, int nrOfSquares, int step, int dimension, int bound) {
        for (int s = 0; s < nrOfSquares; s++) {
            int x = (s * step) / (dimension - 1) * step;
            int y = (s * step) % (dimension - 1);
            int offset = random.nextInt(bound * 2 + 1) - bound;
            int center = (map[x][y] + map[x + step][y] + map[x][y + step] + map[x + step][y + step]) / 4 + offset;
            map[x + step / 2][y + step / 2] = center;
        }
    }

    private void diamondStep(int[][] map, int step, int dimension, int bound) {
        int col = step / 2;
        for (int row = 0; row < dimension; row += step / 2) {
            while (col < dimension) {
                int offset = random.nextInt(bound * 2 + 1) - bound;
                int value = 0;
                int nrVal = 0;
                if (col - step / 2 >= 0) {
                    value += map[row][col - step / 2];
                    nrVal++;
                }
                if (row - step / 2 >= 0) {
                    value += map[row - step / 2][col];
                    nrVal++;
                }
                if (col + step / 2 < dimension) {
                    value += map[row][col + step / 2];
                    nrVal++;
                }
                if (row + step / 2 < dimension) {
                    value += map[row + step / 2][col];
                    nrVal++;
                }
                map[row][col] = value / nrVal + offset;
                col += step;
            }
            if (col - step == dimension - 1) {
                col = step / 2;
            } else {
                col = 0;
            }
        }
    }

    public int[][] compute(int dimensionExponent, int bound, double decrease, int[] seed) {
        if (seed.length != 4) {
            throw new InvalidParameterException("Seed must have 4 values");
        }
        int dimension = (int) Math.pow(2, dimensionExponent) + 1;
        int[][] map = new int[dimension][dimension];
        map[0][0] = seed[0];
        map[0][dimension - 1] = seed[1];
        map[dimension - 1][0] = seed[2];
        map[dimension - 1][dimension - 1] = seed[3];
        int nrOfSquares = 1;
        int step = dimension - 1;
        for (int k = 0; k < dimensionExponent; k++) {
            squareStep(map, nrOfSquares, step, dimension, bound);
            diamondStep(map, step, dimension, bound);
            bound *= decrease;
            step /= 2;
            nrOfSquares *= 4;
        }
        return map;
    }

    public void normaliseForGrayscale(int[][] map) {
        int min = 0;
        int max = map[0][0];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (map[i][j] < min) {
                    min = map[i][j];
                } else if (map[i][j] > max) {
                    max = map[i][j];
                }
            }
        }
        max -= min;
        double scale = (double) 255 / max;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                map[i][j] -= min;
                if (max > 255) {
                    map[i][j] *= scale;
                }
            }
        }
    }
}
