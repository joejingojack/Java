/**
 * Пример за шаблона Команда към дисциплината "Шаблони за проектиране"
 */
package fmi.patterns.lections.command;

public class FibonacciTask implements Task {
	int n1 = 1;
	int n2 = 0;
	int num;

	public FibonacciTask() {
	}

	@Override
	public void performTask() {
		num = n1 + n2;
		System.out.println("Следващото число на Фибоначи е: " + num);
		n1 = n2;
		n2 = num;
	}

	public String toString() {
		return ("Task: числа на Фибоначи");
	}
}

