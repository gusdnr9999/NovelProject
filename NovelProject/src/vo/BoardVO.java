package vo;
import java.util.*;
import lombok.Data;
@Data
public class BoardVO {
	private int no,hit; //
    private String name,subject,content,pwd;
    private Date regdate;
  
}