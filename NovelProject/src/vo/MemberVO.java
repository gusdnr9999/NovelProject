package vo;

import java.util.*;
import lombok.Data;

@Data
public class MemberVO {

  private String id, nickname, pw, name, gender, email, address1, address2, phone, is_admin, last_login, msg;
  private Date reg_date, birth;

}
