/**
 * Пример за шаблона Команда към дисциплината "Шаблони за проектиране"
 */
package fmi.patterns.lections.command;

public class TaskEntry {
	/**
	 * Задачата, която трябва да бъде изпълнена. Това е обект Команда!
	 */
	private Task task;

	/**
	 * Колко често трябва да бъде изпълнявана задачата
	 */
	private long repeatInterval;

	/**
	 * Кога за последно е била изпълнена задачата
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
		return (task + " трябва да бъде изпълнявана на всеки " + repeatInterval
				+ " ms; за последно е изпълнена на " + timeLastDone);
	}
}



