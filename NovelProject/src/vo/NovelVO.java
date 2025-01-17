package vo;

import lombok.Data;

/*
 * 
NO      NOT NULL NUMBER         
GENRE   NOT NULL VARCHAR2(500)  
TITLE   NOT NULL VARCHAR2(500)  
POSTER           VARCHAR2(4000) 
AUTHOR  NOT NULL VARCHAR2(200)  
STORY            CLOB           
AVGSTAR          NUMBER         
SERIAL           VARCHAR2(500)  
ISCP             CHAR(1)  
 */
@Data
public class NovelVO {
	private int 	no;
	private double	avgstar;
	private String 	genre,title,poster,author,story,serial,iscp;
}
