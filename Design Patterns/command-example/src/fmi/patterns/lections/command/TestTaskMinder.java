/**
 * ������ �� ������� ������� ��� ������������ "������� �� �����������"
 */
package fmi.patterns.lections.command;

public class TestTaskMinder {

	public static void main(String[] args) {
		// ��������� � ���������� �� TaskMinder.
		// ���� TaskMinder ��������� �� ������ �� ����� 500 ms.
		TaskMinder tm = new TaskMinder(500);
		
		// ��������� �� ������ Fortune Teller.
		FortuneTask fortuneTask = new FortuneTask();
		
		// ���������� �� Fortune Teller �� ����� 3 s.
		tm.addTask(fortuneTask, 3000);	
		
		// ��������� �� ������ Fibonacci.
		FibonacciTask fibonacciTask = new FibonacciTask();

		// ���������� �� �������� �� ����� 700 ms.
		tm.addTask(fibonacciTask, 700);
	}
}


