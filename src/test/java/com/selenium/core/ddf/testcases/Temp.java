package com.selenium.core.ddf.testcases;

import java.util.Date;

public class Temp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Date d = new Date();
		System.out.println(d.toString().replace(":", "_").replace(" ", "_"));
	}

}
