/**
 * @author xiaomou wang
 * 
 * Read the string from command line;
 * Hit enter to quit then it will log error  Wrong Input Formation and display the result.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SaleTax {
	private HashSet<String> hash;// contain exception goods;
	private double totalTax = 0;
	private double total = 0;
	private ArrayList<Goods> res = new ArrayList<Goods>();// resul set to contain the compute result.
	
	/**
	 * Initialize the exception set for the good that not include the basic tax.
	 */
	public SaleTax() {
		hash = new HashSet<String>();
		hash.add("book");
		//hash.add("music CD");
		hash.add("chocolate bar");
		hash.add("box of chocolates");
		//hash.add("bottle of perfume");
		hash.add("packet of headache pills");
	}

	/**
	 * 
	 * @throws IOException
	 * Compute the tax and price after tax.
	 */
	public void Compute() throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		String s;

		try {
			while (!(s = reader.readLine()).equals(null)) {
				double taxRate = 0.05;
				int num = 0;
				double price = 0.0;
				boolean isImported = false;
				
				// remove "imported".
				if (s.indexOf("imported") != -1) {
					isImported = true;
					s = s.substring(0, s.indexOf("imported")) + s.substring(s.indexOf("imported") + 9);
				}
				String name = null;
				
				// extract quantity, good name and price.
				Pattern p = Pattern
						.compile("^(\\d+)\\s* (.+)\\s+at\\s+(.*)");
				Matcher m = p.matcher(s);

				if (m.find()) {
					num = Integer.parseInt(m.group(1).trim());
					name = m.group(2).trim();
					price = Double.parseDouble(m.group(3).trim());
				} else {
					System.err.println("Wrong Input Formation");
					break;
				}

				taxRate = caculateTax(name, isImported);

				// caculate and insert into the result set.
				double tax =  roundUp(price * taxRate);
				totalTax += tax;
				price += tax;
				price = roundUp(price);
				total += price;
						
				Goods tmp_res = new Goods(num, name, price, isImported);
				res.add(tmp_res);

			}
		} catch (IOException e) {
			return;
		}
	}
	
	/**
	 * 
	 * @param name the goods' name.
	 * @param isImported is the good is imported.
	 * @return the tax rate of that good.
	 */
public double caculateTax(String name, boolean isImported) {
		double rate = 0.0;
		
		if (!check(name))
			rate += 0.1;
		if (isImported)
			rate += 0.05;
			
		return rate;
	}
	
/**
 * 
 * @param name the goods' name.
 * @return if the good in the exception set.
 */
public boolean check(String name) {
		if (hash.contains(name))
			return true;
		return false;
		
}
/**
 * Print the price after tax
 */
public void printRes() {
	for(int i = 0; i  < res.size(); ++i)
		System.out.println(res.get(i));
}

/**
 * print the total price.
 */
public void printTotal() {
	BigDecimal tmp = new BigDecimal(total);
	
    double ret = Double.parseDouble( tmp.setScale(2, RoundingMode.HALF_UP).toString());
	System.out.println("Total: " + ret);
}

/**
 *Print the total tax.
 */
public void printTax() {
	double ret =  roundUp(totalTax);	
	Double a = ret;
    String str = a.toString();
    
    if ((str.charAt(str.length() - 1) - '0' > 5)) {
    	BigDecimal tmp = new BigDecimal(ret);
    	ret = Double.parseDouble( tmp.setScale(1, RoundingMode.HALF_UP).toString());
    }
    
	System.out.println("Sales Taxes: " + ret);
}

/**
 * 
 * @param d the price or tax we want round.
 * @return the number after round.
 */
public double roundUp(Double d) {
	BigDecimal tmp = new BigDecimal(d);
	
    double ret = Double.parseDouble( tmp.setScale(2, RoundingMode.HALF_UP).toString());
    Double a = ret;
    String str = a.toString();
    int i = str.indexOf('.');;
    if (i != -1 && str.length() > (i + 2)) {
    	if ((str.charAt(i + 2) - '0') < 5) {
    		str = str.substring(0, i + 2)  + '5';
    		ret = Double.parseDouble(str);
    	}
    }
    return ret;
}

/**
 * 
 * @param argv null.
 * @throws IOException.
 */
public static void main(String[] argv) throws IOException {
		System.out.println("Please input like 1 book at 12.49 ");
		SaleTax tmp = new SaleTax();
		tmp.Compute();
		tmp.printRes();
		tmp.printTax();
		tmp.printTotal();
	} 

}

/**
 * 
 * @author xiaomou wang
 * The data structure to store the compute result.
 */
class Goods {
	int num;
	String name;
	double price;
	boolean ifImported;
	
	/**
	 * 
	 * @param _num the good quantity. 
	 * @param _name the good name.
	 * @param _price the good price.
	 * @param _ifImported is the good am imported good.
	 */
	Goods(int _num, String _name, double _price, 	boolean _ifImported) {
		num = _num;
		name = _name;
		price = _price;
		ifImported = _ifImported;
	}
	
	/**
	 * Standardize output
	 */
	public String toString() {
		StringBuilder ret = new StringBuilder();
		ret.append(num);
		ret.append(" ");
		if (ifImported) {
			ret.append("imported ");
		}
		ret.append(name);
		ret.append(": ");
		ret.append(price);
		
		return ret.toString();
	}
	
}
