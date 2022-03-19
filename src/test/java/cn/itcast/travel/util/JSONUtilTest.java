package cn.itcast.travel.util;

import cn.itcast.travel.domain.ResultInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;

import javax.management.relation.RelationSupport;
import java.io.IOException;

public class JSONUtilTest extends TestCase {

    public void testRead() throws IOException {
        ObjectMapper mapper=new ObjectMapper();
        ResultInfo info=mapper.readValue("{flag:true,data:null,errorMsg:’呵呵‘}",ResultInfo.class);
        System.out.println(info.getErrorMsg());

    }
}