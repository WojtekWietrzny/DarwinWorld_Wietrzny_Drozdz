package agh.ics.oop;

import agh.ics.oop.model.Gene;
import agh.ics.oop.model.enums.BehaviourType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class GeneTest {

    @Test
    public void testGenerateRandomGene() {
        Gene gene = Gene.generateRandomGene(5, BehaviourType.CompletePredestination);

        assertNotNull(gene);
        assertEquals(5, gene.getDna().size());
    }

    @Test
    public void testGetCurrent() {
        Gene gene = new Gene(3, BehaviourType.CompletePredestination);

        assertEquals(gene.getDna().get(0), gene.getCurrent());
        assertEquals(gene.getDna().get(1), gene.getCurrent());
        assertEquals(gene.getDna().get(2), gene.getCurrent());
        assertEquals(gene.getDna().get(0), gene.getCurrent()); // Wraps around to the beginning
    }

    @Test
    public void testMutate() {
        Gene gene = new Gene(5, BehaviourType.CompletePredestination);
        ArrayList<Integer> originalDna = new ArrayList<>(gene.getDna());

        gene.mutate(1, 1);

        assertNotEquals(originalDna, gene.getDna());
    }

    @Test
    public void testAdvance() {
        Gene gene = new Gene(4, BehaviourType.CompletePredestination);

        assertEquals(gene.getDna().get(1), gene.getCurrent());
        assertEquals(gene.getDna().get(2), gene.getCurrent());

        gene.advance();

        assertEquals(gene.getDna().get(3), gene.getCurrent());
        assertEquals(gene.getDna().get(0), gene.getCurrent()); // Wraps around to the beginning
    }

    @Test
    public void testCreateChild() {
        Gene gene1 = new Gene(6, BehaviourType.BitofCraziness);
        Gene gene2 = new Gene(6, BehaviourType.BitofCraziness);

        ArrayList<Integer> dna1 = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5));
        ArrayList<Integer> dna2 = new ArrayList<>(Arrays.asList(5, 4, 3, 2, 1, 0));

        gene1.setDna(dna1);
        gene2.setDna(dna2);

        Gene child = gene1.createChild(gene2, 10, 8, 1, 2);

        assertNotNull(child);
        assertEquals(6, child.getDna().size());
        assertNotEquals(dna1, child.getDna());
        assertNotEquals(dna2, child.getDna());
    }
}
