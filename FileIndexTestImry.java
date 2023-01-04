package il.ac.tau.cs.sw1.ex8.tfIdf;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
 

class FileIndexTestImry {
	private static final double EPSILON = 0.00001;
	public String folderPath = "./resources/hw8/kendrick_input";
	public FileIndex kenDex = new FileIndex();
	public int a = 3;
	@BeforeEach
	void setUp() throws Exception {
		this.kenDex.indexDirectory(this.folderPath); //Indexation of directory	

	}

	@Test
	void checkGetCountInFile() throws FileIndexException {
		assertEquals(kenDex.getCountInFile("walls", "these_walls.txt"), 29);
		assertEquals(kenDex.getCountInFile("LOVE", "these_walls.txt"), 6);
		assertEquals(kenDex.getCountInFile("KANYE", "these_walls.txt"), 0); //Obviously
		assertEquals(kenDex.getCountInFile("humble", "humble.txt"), 16);
		assertEquals(kenDex.getCountInFile("bitch", "humble.txt"), 32);
	}
	@Test
	void checkGetNumOfUniqueWordsInFile() throws FileIndexException {
		assertEquals(kenDex.getNumOfUniqueWordsInFile("crown.txt"), 113);
		assertEquals(kenDex.getNumOfUniqueWordsInFile("father_time.txt"), 287);
		assertEquals(kenDex.getNumOfUniqueWordsInFile("these_walls.txt"),273);
	}
	
	@Test
	void checkGetNumOfFilesInIndex() {
		assertEquals(kenDex.getNumOfFilesInIndex(), 5);
	}
	
	@Test
	void checkGetTF() throws FileIndexException{ //CHECK ORDERING OF FILENAME AND WORD
		assertEquals(kenDex.getTF("walls","these_walls.txt"), (double)29 / 691, EPSILON);
		assertEquals(kenDex.getTF("nazareth","these_walls.txt"), (double)1 / 691, EPSILON);
		assertEquals(kenDex.getTF("bitch","humble.txt"), (double)32 / 578, EPSILON);
		assertEquals(kenDex.getTF("Rosalia", "humble.txt"), 0.0, EPSILON); //Rosalia doesn't appear in a Kendrick song (yet)
		
	}
	@Test
	void checkGetIDF() throws FileIndexException{
		int numOfDocs = 5;
		assertEquals(kenDex.getIDF("talk"), Math.log((double)numOfDocs/3), EPSILON); //"Talk" appears in These Walls, Humble, Father Time
		assertEquals(kenDex.getIDF("dAdDy"), Math.log((double)numOfDocs/2), EPSILON); //"Daddy" appears in These Walls, Father Time
		assertEquals(kenDex.getIDF("Counterfeits"), Math.log((double)numOfDocs/1), EPSILON); //"Counterfeits" appears in Humble
		assertEquals(kenDex.getIDF("I"), Math.log((double)numOfDocs/numOfDocs), EPSILON); //"I" obviously appears in all songs

	}
	
	@Test
	void checkGetTopFiveMostSignificantHumble() throws FileIndexException{
		List<Map.Entry<String,Double>> topFiveHumble = kenDex.getTopKMostSignificantWords("humble.txt", 5);
		List<String> words = listOfKStrings(topFiveHumble);
		List<Double> TFIDFS = listOfKTFIDFS(topFiveHumble);
		
		assertEquals(words, Arrays.asList(new String[]{"hol","sit","lil","humble","down"}));
		assertEquals(TFIDFS, Arrays.asList(new Double[] {0.15871619551685764,0.06961236645476211,0.06682787179657164,0.04455191453104776,0.041217230153508706}));
	
	}
	@Test
	void checkGetTopEightMostSignificantMoney() throws FileIndexException{
		List<Map.Entry<String,Double>> topEightMoney = kenDex.getTopKMostSignificantWords("money_trees.txt", 8);
		List<String> words = listOfKStrings(topEightMoney);
		List<Double> TFIDFS = listOfKTFIDFS(topEightMoney);
	
		assertEquals(words, Arrays.asList(new String[]{"bish","ya","dollar","day","nah","feel","how","one"}));
		assertEquals(TFIDFS, Arrays.asList(new Double[]{0.04184887312938072,0.04184887312938072,0.0174370304705753,0.012205921329402711,0.011912772245384465,0.01092004122493576,0.01092004122493576,0.01092004122493576}));

	}
	@Test
	void checkGetTopFourMostSignificantWalls() throws FileIndexException{
		List<Map.Entry<String,Double>> topFourWalls = kenDex.getTopKMostSignificantWords("these_walls.txt", 4);
		List<String> words = listOfKStrings(topFourWalls);
		List<Double> TFIDFS = listOfKTFIDFS(topFourWalls);
		assertEquals(words, Arrays.asList(new String[]{"these","walls","could","her"}));
		assertEquals(TFIDFS, Arrays.asList(new Double[]{0.05124114916577453,0.038455037951303186,0.02121657266278796,0.016304002007291898}));

	}
	
	@Test
	void checkGetCosineSimilarity() throws FileIndexException{
		assertEquals(kenDex.getCosineSimilarity("father_time.txt", "crown.txt"),0.02813121395755953);
		assertEquals(kenDex.getCosineSimilarity("money_trees.txt", "these_walls.txt"),0.036476855408337186);
		assertEquals(kenDex.getCosineSimilarity("humble.txt", "money_trees.txt"),0.0073478682703409974);
	}
	@Test
	void checkSimilarFatherTime() throws FileIndexException{
		
		//THE TWO CLOSEST SONGS TO FATHER TIME ARE THESE WALLS AND MONEY TREES
		List<Map.Entry<String,Double>> twoClosestToFatherTime = kenDex.getTopKClosestDocuments("father_time.txt",2);
		List<String> fileNames = listOfKStrings(twoClosestToFatherTime);
		List<Double> TFIDFS = listOfKTFIDFS(twoClosestToFatherTime);
		assertEquals(fileNames, Arrays.asList(new String[] {"these_walls.txt","money_trees.txt"}));
		List<Double> expectedVals =  Arrays.asList(new Double[] {0.053257961222427146,0.03245096795720893});
		
		for (int i=0; i < TFIDFS.size(); i++) {
			assertEquals(TFIDFS.get(i),expectedVals.get(i), EPSILON);
		}

	}
	@Test
	void checkSimilarCrown() throws FileIndexException {
		//THE TWO CLOSEST SONGS TO CROWN ARE FATHER TIME (WHICH MAKES SENSE) AND MONEY TREES
		List<Map.Entry<String,Double>> twoClosestToCrown = kenDex.getTopKClosestDocuments("crown.txt",2);
		List<String> fileNames = listOfKStrings(twoClosestToCrown);
		List<Double> TFIDFS = listOfKTFIDFS(twoClosestToCrown);
		
		assertEquals(fileNames, Arrays.asList(new String[] {"father_time.txt","money_trees.txt"}));
		List<Double> expectedVals =  Arrays.asList(new Double[] {0.02813121395755953, 0.015574629197492888});
		
		for (int i=0; i < TFIDFS.size(); i++) {
			assertEquals(TFIDFS.get(i),expectedVals.get(i), EPSILON);
		}
	}

	///PRIVATE METHODS
	private List<String> listOfKStrings(List<Map.Entry<String, Double>> topK) {
		List<String> words = new ArrayList<String>();
		for (Map.Entry<String,Double> entry: topK) {
			words.add(entry.getKey());
		}
		return words;
	}
	private List<Double> listOfKTFIDFS(List<Map.Entry<String, Double>> topK) { //Schichpul code shel ha'hayim
		List<Double> TFIDFS = new ArrayList<Double>();
		for (Map.Entry<String,Double> entry: topK) {
			TFIDFS.add(entry.getValue());
		}
		return TFIDFS;
	}
	
	

	

}
