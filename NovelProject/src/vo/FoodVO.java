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
public class FoodVO {

  private int fno, hit;
  private double score;
  private String name, type, phone, address, theme, poster, images, time, parking, content, price;

}
