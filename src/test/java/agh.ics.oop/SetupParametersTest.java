package agh.ics.oop;

import agh.ics.oop.model.enums.BehaviourType;
import agh.ics.oop.model.enums.MapType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SetupParametersTest {

    @Test
    void testValidSetupParameters() {
        String[] setup = {"Config", "TunnelMap", "CompletePredestination", "10", "10", "20", "1", "5", "10", "15", "20", "2", "5", "8"};

        try {
            SetupParameters parameters = new SetupParameters(setup);
            assertEquals("Config", parameters.getConfigurationName());
            assertEquals(MapType.TunnelMap, parameters.getMapType());
            assertEquals(BehaviourType.CompletePredestination, parameters.getBehaviourType());
            assertEquals(10, parameters.getWidth());
            assertEquals(10, parameters.getHeight());
            assertEquals(20, parameters.getStartingPlantAmount());
            assertEquals(1, parameters.getPlantGrowthRate());
            assertEquals(10, parameters.getStartingAnimalAmount());
            assertEquals(5, parameters.getStartingAnimalEnergy());
            assertEquals(10, parameters.getEnergyToReproduce());
            assertEquals(15, parameters.getEnergyConsumedByReproduction());
            assertEquals(2, parameters.getMinMutations());
            assertEquals(5, parameters.getMaxMutations());
            assertEquals(8, parameters.getGenomeSize());
        } catch (Exception e) {
            fail("Exception should not be thrown for valid parameters");
        }
    }

    @Test
    void testInvalidWidth() {
        String[] setup = {"Config", "TunnelMap", "CompletePredestination", "-1", "10", "20", "1", "5", "10", "15", "20", "2", "5", "8"};

        assertThrows(Exception.class, () -> new SetupParameters(setup));
    }

    // Analogicznie dodaj testy dla innych przypadków błędnych danych wejściowych
}
