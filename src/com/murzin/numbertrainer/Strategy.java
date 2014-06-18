/**
 * 
 */
package com.murzin.numbertrainer;

import android.view.View;

/**
 * @author Andrei V. Murzin
 *
 */
public interface Strategy {
	public void prepareTaskView(Task task);
	public View doTaskView();
	public void release();
}
