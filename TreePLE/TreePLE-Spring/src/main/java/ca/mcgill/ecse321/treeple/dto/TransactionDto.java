package ca.mcgill.ecse321.treeple.dto;

import java.sql.Date;
import java.sql.Time;

import ca.mcgill.ecse321.treeple.model.Tree.TreeStatus;

public class TransactionDto {
	Time time;
	Date date;
	ResidentDto resident;
	TreeDto tree;
	ca.mcgill.ecse321.treeple.model.Transaction.TreeStatus status;
	
	public ca.mcgill.ecse321.treeple.model.Transaction.TreeStatus getStatus() {
		return status;
	}
	
	public void setStatus(ca.mcgill.ecse321.treeple.model.Transaction.TreeStatus treeStatus) {
		this.status = treeStatus;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public ResidentDto getResident() {
		return resident;
	}

	public void setResident(ResidentDto resident) {
		this.resident = resident;
	}

	public TreeDto getTree() {
		return tree;
	}

	public void setTree(TreeDto tree) {
		this.tree = tree;
	}

	public TransactionDto(Time time, Date date, ResidentDto resident, TreeDto tree) {
		super();
		this.time = time;
		this.date = date;
		this.resident = resident;
		this.tree = tree;
	}

	public TransactionDto() {
		// TODO Auto-generated constructor stub
	}
}
