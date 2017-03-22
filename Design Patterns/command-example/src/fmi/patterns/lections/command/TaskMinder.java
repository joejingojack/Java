/**
 * Пример за шаблона Команда към дисциплината "Шаблони за проектиране"
 */
package fmi.patterns.lections.command;

import java.util.ArrayList;
import java.util.Iterator;

public class TaskMinder extends Thread {
	/**
	 * Колко често TaskMinder трябва да проверява за изпълнение на задачи
	 */
	private long sleepInterval;
	
	/**
	 * Списък със задачи
	 */
	private ArrayList<TaskEntry> taskList; 

	public TaskMinder(long sleepInterval) {
		this.sleepInterval = sleepInterval;
		taskList = new ArrayList<TaskEntry>();
		start();
	}

	public void addTask(Task task, long repeatInterval) {
		long ri = (repeatInterval > 0) ? repeatInterval : 0;
		TaskEntry te = new TaskEntry(task, ri);
		taskList.add(te);
	}

	public Iterator<TaskEntry> getTasks() {
		return taskList.iterator();
	}

	public long getSleepInterval() {
		return sleepInterval;
	}

	public void setSleepInterval(long si) {
		this.sleepInterval = si;
	}

	public void run() {
		while (true) {
			try {
				sleep(sleepInterval);
				long now = System.currentTimeMillis();
				Iterator<TaskEntry> it = taskList.iterator();
				while (it.hasNext()) {
					TaskEntry te = (TaskEntry) it.next();
					
					if (te.getRepeatInterval() + te.getTimeLastDone() < now) {
						te.getTask().performTask();
						te.setTimeLastDone(now);
					}
				}
			} catch (Exception e) {
				System.out.println("Interrupted sleep: " + e);
			}
		}
	}
}



