package org.example.service;

import org.springframework.stereotype.Component;

@Component
public class MutantDetector {


    public boolean isMutant(String[] dna) {
        // Validación del input
        if (dna == null || dna.length == 0) {
            throw new IllegalArgumentException("ADN tiene que existir ");
        }

        int n = dna.length;

        // Validar que sea una matriz cuadrada
        for (String row : dna) {
            if (row == null || row.length() != n) {
                throw new IllegalArgumentException("ADN la matriz tiene que ser cuadrada(NxN)");
            }
            // Validar caracteres permitidos
            if (!row.matches("[ATCG]+")) {
                throw new IllegalArgumentException("ADN solo puede contener A, T, C, G ");
            }
        }

        int sequencesFound = 0;

        // Buscar secuencias horizontales
        sequencesFound += findHorizontalSequences(dna, n);
        if (sequencesFound > 1) return true;

        // Buscar secuencias verticales
        sequencesFound += findVerticalSequences(dna, n);
        if (sequencesFound > 1) return true;

        // Buscar secuencias diagonales (de izquierda a derecha, descendente)
        sequencesFound += findDiagonalSequences(dna, n, true);
        if (sequencesFound > 1) return true;

        // Buscar secuencias diagonales (de derecha a izquierda, descendente)
        sequencesFound += findDiagonalSequences(dna, n, false);

        return sequencesFound > 1;
    }

    private int findHorizontalSequences(String[] dna, int n) {
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= n - 4; j++) {
                if (hasSequence(dna[i].charAt(j), dna[i].charAt(j + 1),
                        dna[i].charAt(j + 2), dna[i].charAt(j + 3))) {
                    count++;
                    break; // Evitar contar múltiples secuencias en la misma fila
                }
            }
        }
        return count;
    }

    private int findVerticalSequences(String[] dna, int n) {
        int count = 0;
        for (int j = 0; j < n; j++) {
            for (int i = 0; i <= n - 4; i++) {
                if (hasSequence(dna[i].charAt(j), dna[i + 1].charAt(j),
                        dna[i + 2].charAt(j), dna[i + 3].charAt(j))) {
                    count++;
                    break; // Evitar contar múltiples secuencias en la misma columna
                }
            }
        }
        return count;
    }

    private int findDiagonalSequences(String[] dna, int n, boolean leftToRight) {
        int count = 0;

        // Diagonales desde la primera fila
        for (int col = 0; col <= n - 4; col++) {
            if (leftToRight) {
                count += checkDiagonal(dna, 0, col, n, 1, 1);
            } else {
                count += checkDiagonal(dna, 0, n - 1 - col, n, 1, -1);
            }
        }

        // Diagonales desde la primera columna (excluyendo [0,0])
        for (int row = 1; row <= n - 4; row++) {
            if (leftToRight) {
                count += checkDiagonal(dna, row, 0, n, 1, 1);
            } else {
                count += checkDiagonal(dna, row, n - 1, n, 1, -1);
            }
        }

        return count;
    }

    private int checkDiagonal(String[] dna, int startRow, int startCol, int n, int rowInc, int colInc) {
        int row = startRow;
        int col = startCol;

        while (row <= n - 4 && col >= 0 && col <= n - 4 * Math.abs(colInc)) {
            if (hasSequence(dna[row].charAt(col),
                    dna[row + 1].charAt(col + colInc),
                    dna[row + 2].charAt(col + 2 * colInc),
                    dna[row + 3].charAt(col + 3 * colInc))) {
                return 1;
            }
            row += rowInc;
            col += colInc;
        }
        return 0;
    }

    private boolean hasSequence(char c1, char c2, char c3, char c4) {
        return c1 == c2 && c2 == c3 && c3 == c4;
    }
}
