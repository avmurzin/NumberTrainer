package com.murzin.numbertrainer;

import com.murzin.numbertrainer.TaskContent.Operation;

public class TaskMult implements Task {
	
	private TaskContent content;
	private long limit = 0;
	
	TaskMult() {
		generate(limit);
	}
	TaskMult(long limit) {
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
			operand_2=Math.round(Math.random() * limit);
		} while (operand_2 == 0);
		
		content = null;
		content = new TaskContent(operand_1, operand_2, Operation.Mult, operand_1 * operand_2);	
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
