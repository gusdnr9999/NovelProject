package vo;
/*
 * 
RNO     NOT NULL NUMBER         
NO               NUMBER         
ID               VARCHAR2(20)   
STAR    NOT NULL NUMBER         
CONTENT          CLOB           
REGDATE          DATE           
UP               NUMBER         
DOWN             NUMBER         
PROFILE          VARCHAR2(4000) 
 */
import java.util.*;
import lombok.Data;
@Data
public class ReviewVO {
	private int 	rno,no,up,down;
	private double 	star;
	private String 	id,content,profile;
	private Date	regdate; 
}
