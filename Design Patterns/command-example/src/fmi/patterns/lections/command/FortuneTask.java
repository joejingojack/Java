/**
 * ������ �� ������� ������� ��� ������������ "������� �� �����������"
 */
package fmi.patterns.lections.command;

public class FortuneTask implements Task {

	int nextFortune = 0;
	String[] fortunes = { "����� ��� ��� �� �������",
						  "������� ��������� � ����� ��������",
						  "�������� ������� ������� �������" };

	public FortuneTask() {
	}

	@Override
	public void performTask() {
		System.out.println("������ ������ �: " + fortunes[nextFortune]);
		nextFortune = (nextFortune + 1) % fortunes.length;
	}

	public String toString() {
		return ("Task: ������������");
	}

}

