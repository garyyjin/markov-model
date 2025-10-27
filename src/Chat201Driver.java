import java.io.IOException;

public class Chat201Driver {
	
	private static int TEXT_SIZE = 10000;
	private static int MODEL_ORDER = 2;
	private static int RANDOM_SEED = 1234;

	public static BaseMarkovModel getModel(int order){
		//return new BaseMarkovModel();
		//return new SimpleMarkovModel(order);
		return new HashMarkovModel(order);
	}
	public static void main(String[] args) throws IOException {
		// Can change the training text here
		String dirName = "data/onefile";

		// change code in getModel as warranted
		BaseMarkovModel model = getModel(MODEL_ORDER);
		
		model.setSeed(RANDOM_SEED);
		double beforeTraining = System.nanoTime();
		model.trainDirectory(dirName);
		double afterTraining = System.nanoTime();
		double trainingTime = (afterTraining - beforeTraining)/1e9; 	
		System.out.printf("\nTrained on text %s with # tokens = %d\n", dirName, model.tokenSize());
		System.out.printf("Training time = %2.3f seconds\n", trainingTime);

		double beforeGenerating = System.nanoTime();
		String randomText = model.generate(TEXT_SIZE);
		double afterGenerating = System.nanoTime();
		double generatingTime = (afterGenerating - beforeGenerating)/1e9; 

		System.out.printf("Generated N=%d random words with order %d Markov Model\n", TEXT_SIZE, MODEL_ORDER);
		System.out.printf("Generating time = %2.3f seconds\n", generatingTime);
		printNicely(randomText.substring(0,100),50);
		printNicely(randomText.substring(randomText.length()-100),50);
	}

	private static void printNicely(String random, int screenWidth) {
		String[] words = random.split("\\s+");
		int psize = 0;
		System.out.println("----------------------------------");
		for(int k=0; k < words.length; k++){
			System.out.print(words[k]+ " ");
			psize += words[k].length() + 1;
			if (psize > screenWidth) {
				System.out.println();
				psize = 0;
			}
		}
		System.out.println("\n----------------------------------");
	}

}
