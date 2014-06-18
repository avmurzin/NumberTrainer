/**
 * 
 */
package com.murzin.numbertrainer;

import android.view.View;

/**
 * 
 * @author murzin
 *
 */
public interface StrategyScore {
	/**
	 * Return score View for attach to UI in MainActivity. 
	 * @return View
	 */
	public View getScoreView();
	/**
	 * Update score View at UI and store score in persistent store, network, etc.
	 * @param answer - result of answer checking.
	 */
	public void updateScore(boolean answer);
	/**
	 * Release all strategy resources if required. 
	 */
	public void release();
	/**
	 * Clear all score data.
	 */
	public void clear();
}
