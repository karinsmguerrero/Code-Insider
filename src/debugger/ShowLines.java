package debugger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ShowLines {
	// global in ShowLines class
	private ArrayList<String> code;

	public ShowLines(String fileName) {
		code = new ArrayList<String>();
		String line = null;
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(fileName));
			while ((line = in.readLine()) != null)
				code.add(line);
		} catch (IOException ex) {
			System.out.println("Problem reading " + fileName);
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
			}
		}
	} // end of showLines()

	public String show(int lineNum) {
		if (code == null)
			return "No code to show";
		if ((lineNum < 1) || (lineNum > code.size()))
			return "Line no. out of range";
		return ("" + lineNum + ".\t" + code.get(lineNum - 1));// use num -1
	} // end of show()
}
