package com.company;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class Main {

    private static final DateFormat dateFormat = new SimpleDateFormat("MM/dd//yyyy HH:mm:ss");
    private static final Calendar cal = Calendar.getInstance();

    public static void main(String[] args) {
    Date date = new Date();
    cal.setTime(new Date());
    cal.add(Calendar.HOUR_OF_DAY, 20);
    cal.getTime();

    Scanner reader = new Scanner(System.in);
    Scanner reader2 = new Scanner(System.in);
    Scanner reader3 = new Scanner(System.in);
    System.out.println("Enter a number: ");
    System.out.println("Enter a second number: ");
    System.out.println("Enter a third number: ");
    int n = reader.nextInt();
    int m = n+10;
    int k = reader2.nextInt();
    int l = reader3.nextInt();
    int j = k*l;

    String name = "Stoyan";
	int age = 23;
	double height = 1.80;
	System.out.println("My name is " + name);

	int x = 555556;
	int y = 666665;
	int result = x + y;
	System.out.println("This is the result: " + result);

	double a = 5.666;
	double b = 6.555;
	System.out.println("\n Current date: " + dateFormat.format(date));  //-	Print on the console the current date
    System.out.println(" Date after 20 hours: " + cal.getTime()); //-	Print on the console the date after 20 hours
    System.out.println(" Product of 'a' and 'b' is: " + a*b); //- -	Print on the console the product of two “double” variables declared by you
    System.out.println(" The number you entered plus 10 in addition is: " + m); //-	Read from the console an user input which should be numeric. Sum it with 10 and print on the console the sum.
    System.out.println(" The Product of your second and third number is: " + j); //-	Read from the console two numbers. Print on the console their product (e.g. Read “4” and “20”, Print “80”)
    System.out.println("Printing the numbers from 1 to 1000:"); //-	Print on the console the numbers from 1 to 1000. You may need to use loops.

        for(int i = 1; i <= 1000; i++)

        {
            System.out.println(i);
        }
    }

}