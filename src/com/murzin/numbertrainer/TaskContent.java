package com.murzin.numbertrainer;

public class TaskContent {
    public static enum Operation {
    	Add("+"),
    	Sub("-"),
    	Mult("*"),
    	Div("/");
    	private String mark;
    	Operation(String m) {
    		mark = m;
    	}
    	String getString() {
    		return mark;
    	}
    }
    private Operation op;
    private long operands[];
    private long answer;
    
    public TaskContent(long operand_1, long operand_2, Operation op, long answer) {
        operands = new long[2];
    	this.operands[0] = operand_1;
    	this.operands[1] = operand_2;
    	this.op = op;
    	this.answer = answer;
    }
    
    public Operation getOperation() {
    	return op;
    }
    
    public long[] getOperands() {
    	return operands;
    }
    
    public long getAnswer() {
    	return answer;
    }
}
