package com.murzin.numbertrainer;

import com.murzin.numbertrainer.TaskContent.Operation;


public class TaskAdd implements Task {
	
	private TaskContent content;
	private long limit = 0;
	
	TaskAdd() {
		generate(limit);
	}
	TaskAdd(long limit) {
		this.limit = limit;
		generate(limit);
	}
	
	@Override
	public void generate(long limit) {
		long operand_1=Math.round(Math.random() * limit);
		long operand_2=Math.round(Math.random() * limit);
		content = null;
		content = new TaskContent(operand_1, operand_2, Operation.Add, operand_1 + operand_2);		
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
