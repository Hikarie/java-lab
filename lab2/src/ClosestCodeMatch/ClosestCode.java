package ClosestCodeMatch;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public interface ClosestCode {
	void match(ArrayList<String> urls, boolean removeComments) throws FileNotFoundException;
}
