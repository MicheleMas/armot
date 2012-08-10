package tools;

import console.TChooseInterface;

public class Loader {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		ChooseInterface ci;
		TChooseInterface tci;
		if (args.length > 0 && "-t".equals(args[0]))
			tci = new TChooseInterface();
		else
			ci = new ChooseInterface();
	}

}
