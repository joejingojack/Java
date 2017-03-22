/**
 * ������ �� ������� ������� ��� ������������ "������� �� �����������"
 */
package fmi.patterns.lections.command;

public class TaskEntry {
	/**
	 * ��������, ����� ������ �� ���� ���������. ���� � ����� �������!
	 */
	private Task task;

	/**
	 * ����� ����� ������ �� ���� ����������� ��������
	 */
	private long repeatInterval;

	/**
	 * ���� �� �������� � ���� ��������� ��������
	 */
	private long timeLastDone;

	public TaskEntry(Task task, long repeatInterval) {
		this.task = task;
		this.repeatInterval = repeatInterval;
		this.timeLastDone = System.currentTimeMillis();
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public long getRepeatInterval() {
		return repeatInterval;
	}

	public void setRepeatInterval(long ri) {
		this.repeatInterval = ri;
	}

	public long getTimeLastDone() {
		return timeLastDone;
	}

	public void setTimeLastDone(long t) {
		this.timeLastDone = t;
	}

	public String toString() {
		return (task + " ������ �� ���� ����������� �� ����� " + repeatInterval
				+ " ms; �� �������� � ��������� �� " + timeLastDone);
	}
}



