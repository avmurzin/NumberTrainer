package com.murzin.numbertrainer;

import com.murzin.numbertrainer.TaskContent.Operation;

public class TaskSub implements Task {
	
	TaskContent content;
	long limit = 0;
	
	TaskSub() {
		generate(limit);
	}
	TaskSub(long limit) {
		this.limit = limit;
		generate(limit);
	}
	
	@Override
	public void generate(long limit) {
		long operand_1;
		long operand_2;
		
		do {
			operand_1=Math.round(Math.random() * limit);
		} while (operand_1 == 0);
		do {
			operand_2 = Math.round(Math.random() * limit);
		} while (operand_2 == 0);
		
		content = null;
		if (operand_1 > operand_2) {
			content = new TaskContent(operand_1, operand_2, Operation.Sub, operand_1 - operand_2);			
		} else {
			content = new TaskContent(operand_2, operand_1, Operation.Sub, operand_2 - operand_1);
		}
		
	}

	@Override
	public void refresh() {
		content = null;
		generate(limit);
	}

	@Override
	public TaskContent getTaskContent() {
		return content;
	}

}