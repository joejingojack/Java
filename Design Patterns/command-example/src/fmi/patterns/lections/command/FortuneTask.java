/**
 * Пример за шаблона Команда към дисциплината "Шаблони за проектиране"
 */
package fmi.patterns.lections.command;

public class FortuneTask implements Task {

	int nextFortune = 0;
	String[] fortunes = { "Който учи той ще сполучи",
						  "Опознай шаблоните и научи истината",
						  "Сговорна дружина планина повдига" };

	public FortuneTask() {
	}

	@Override
	public void performTask() {
		System.out.println("Твоето бъдеще е: " + fortunes[nextFortune]);
		nextFortune = (nextFortune + 1) % fortunes.length;
	}

	public String toString() {
		return ("Task: Предсказател");
	}

}

