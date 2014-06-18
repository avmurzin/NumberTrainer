package com.murzin.numbertrainer;
/**
 * Interface for Tasks family.
 * @author Andrei V. Murzin
 *
 */
public interface Task {
	/**
	 * Make a new content;
	 * @param limit mean a maximum operand value
	 */
    void generate(long limit);
	/**
     * Make another assignment.
     */
    void refresh();
    /**
     * Get assignment content for visualization classes.
     * @return object that contains task.
     */
    TaskContent getTaskContent();
}
