/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peptidedigestor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 *
 * @author arne
 */
public class TrypsinDigestor implements Digestor {

    /**
     * contains the minimal length a peptide should have.
     */
    private final Integer minimalLength;

    /**
     * Initiates the class.
     *
     * @param minLength
     */
    public TrypsinDigestor(final Integer minLength) {
        this.minimalLength = minLength;
    }

    @Override
    public final ArrayList<String> digest(final String peptide) {
        // Trypsin
        // The order for the sites is:
        // p4   p3  p2  p1  p1F p2F p3F p4F
        ArrayList<Character> aa = new ArrayList<>(Arrays.asList('K', 'R'));
        ArrayList<Integer> indices = new ArrayList<>(Arrays.asList(-1, peptide.length() - 1));
        for (Character amino : aa) {
            for (int index = peptide.indexOf(amino); index >= 0 && index <= peptide.length() - 2;
                    index = peptide.indexOf(amino, index + 1)) {
                String p1 = peptide.substring(index, index + 1);
                String p1F = peptide.substring(index + 1, index + 2);
                if (index >= 1) {
                    String p2 = peptide.substring(index - 1, index);
                    if (amino.equals('K') && p2.equals("W") && p1F.equals("P")) {
                        indices.add(index);
                    } else if (amino.equals('R') && p2.equals("M") && p1F.equals("P")) {
                        indices.add(index);
                    } else {
                        if (amino.equals('K') && p2.equals("C") && p1F.equals("D") || p1F.equals("H") ||
                                p1F.equals("Y")) {
                        } else if (amino.equals('K') && p2.equals("D") && p1F.equals("D")) {
                        } else if (amino.equals('R') && p2.equals("C") && p1F.equals("K")) {
                        } else if (amino.equals('R') && p2.equals("R") && p1F.equals("R") || p1F.equals("H")) {
                        } else if (!p1F.equals("P")) {
                            indices.add(index);
                        }
                    }
                } else {
                    if (!p1F.equals('P')) {
                        indices.add(index);
                    }
                }

            }
        }
       return getDigestionArray(peptide, indices);
    }

    @Override
    public ArrayList<String> getDigestionArray(final String peptide, final ArrayList<Integer> indices) {
        Collections.sort(indices);
        ArrayList<String> digestedPeptide = new ArrayList<>();
        for (int i = 0; i < indices.size() - 1; i++) {
            if (indices.get(i + 1) - indices.get(i) >= this.minimalLength) {
                digestedPeptide.add(peptide.substring(indices.get(i) + 1, indices.get(i + 1) + 1));
            }
        }
        return digestedPeptide;
    }
}

