package com.khomenko;

/**
 * Created by Privat on 06.09.2016.
 */
public class GroupUtil {

    /**
     * Group rectangle regions in binary matrix.
     *
     * @param matrix The binary matrix
     * @param threshold The threshold number of neighbor elements of matrix
     */
    public static final void groupRegions(int[][] matrix, int threshold) {
        int regioncount = 2;
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                if (matrix[row][col] == 1) {
                    joinGroup(matrix, row, col, regioncount, threshold);
                    regioncount++;
                }
            }
        }
    }

    private static void joinGroup(int[][] matrix, int row, int col, int regioncount, int threshold) {
        if (row < 0 || row >= matrix.length) {
            return;
        }
        if (col < 0 || col >= matrix[row].length) {
            return;
        }
        if (matrix[row][col] != 1) {
            return;
        }
        matrix[row][col] = regioncount;

        for (int i = 0; i < threshold; i++) {
            joinGroup(matrix, row - 1 - i, col, regioncount, threshold);
            joinGroup(matrix, row + 1 + i, col, regioncount, threshold);
            joinGroup(matrix, row, col - 1 - i, regioncount, threshold);
            joinGroup(matrix, row, col + 1 + i, regioncount, threshold);

            joinGroup(matrix, row - 1 - i, col - 1 - i, regioncount, threshold);
            joinGroup(matrix, row + 1 + i, col - 1 - i, regioncount, threshold);
            joinGroup(matrix, row - 1 - i, col + 1 + i, regioncount, threshold);
            joinGroup(matrix, row + 1 + i, col + 1 + i, regioncount,threshold);
        }

    }
}
